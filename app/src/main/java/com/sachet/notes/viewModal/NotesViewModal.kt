package com.sachet.notes.viewModal

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.data.Note
import com.sachet.notes.db.UserCredRepository
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModal
@Inject constructor(
    var notesRepository: NotesRepository,
    var userCredRepository: UserCredRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state

    var logOut = mutableStateOf(false)

    init {
        viewModelScope.launch {
            try {
                _state.value = state.value.copy(
                    isSearchStarted = true
                )
                val token = userCredRepository.getCred()
                val allNotes = notesRepository.getAllNotes("Bearer ${token?.authToken}")
                _state.value = state.value.copy(
                    notes = orderBy(allNotes, NotesOrder.Date(OrderType.Ascending)),
                    notesOrder = NotesOrder.Date(orderType = OrderType.Ascending),
                    ex = null,
                    credential = "Bearer ${token?.authToken}",
                    isSearchStarted = false
                )
            }catch (ex: CancellationException){
                _state.value = state.value.copy(
                    ex = ex.localizedMessage,
                    isSearchStarted = false
                )
            }catch (ex: Exception){
                _state.value = state.value.copy(
                    ex = ex.localizedMessage,
                    isSearchStarted = false
                )
            }
        }
    }

    private fun onEvent(event: NotesEvent){
        Log.d("ONEVENT", "onEvent: ${event}")
        when(event){
            is NotesEvent.Order -> {
                if (state.value.notesOrder::class == event.newNotesOrder::class &&
                    state.value.notesOrder.orderType == event.newNotesOrder.orderType
                ){
                    return
                }
                viewModelScope.launch {
                    _state.value = state.value.copy(
                        notes = orderBy(state.value.notes, event.newNotesOrder),
                        notesOrder = event.newNotesOrder
                    )
                }
            }
            is NotesEvent.ToggleOrderSelection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    fun toggleNotesOrder(notesEvent: NotesEvent){
        viewModelScope.launch {
            onEvent(notesEvent)
        }
    }

    fun toggleOrderSection(){
        viewModelScope.launch {
            onEvent(NotesEvent.ToggleOrderSelection)
        }
    }

    fun deleteNote(credential: String,note: Note){
        viewModelScope.launch {
            val result = notesRepository.deleteNote(state.value.credential, note.noteId)
            if (result.data != null){
                val newNote = _state.value.notes.filter {
                    it.noteId != result.data
                }
                _state.value = state.value.copy(
                    notes = newNote
                )
            }
        }
    }

    fun logOut(){
        viewModelScope.launch {
            userCredRepository.deleteToken()
            logOut.value = true
        }
    }

}