package com.sachet.notes.viewModal

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.data.Note
import com.sachet.notes.db.UserCredRepository
import com.sachet.notes.model.NoteState
import com.sachet.notes.util.NotesEvent
import com.sachet.notes.model.NotesOrder
import com.sachet.notes.model.OrderType
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val _eventFlow = MutableSharedFlow<NotesEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var logOut = mutableStateOf(false)

    init {
        viewModelScope.launch {
            try {
                _state.value = state.value.copy(
                    isSearchStarted = true
                )
                val token = userCredRepository.getCred()
                val allNotes = notesRepository.getAllNotes("Bearer ${token?.authToken}")
                if (allNotes.code == 200){
                    _state.value = state.value.copy(
                        notes = orderBy(allNotes.notes, NotesOrder.Date(OrderType.Ascending)),
                        notesOrder = NotesOrder.Date(orderType = OrderType.Ascending),
                        ex = null,
                        credential = "Bearer ${token?.authToken}",
                        isSearchStarted = false
                    )
                }else{
                    println("FAILURE EVENT $allNotes")
                    _eventFlow.emit(NotesEvent.FetchNotesEventFailure(allNotes.message))
                    _state.value = state.value.copy(
                        isSearchStarted = false
                    )
                }
            }catch (ex: CancellationException){
                if (ex.message?.contains("401") == true){
//                    _state.value = state.value.copy(
//                        hasJwtExpired = true,
//                        isSearchStarted = false
//                    )
                    _eventFlow.emit(NotesEvent.TokenExpiredFailure())
                }else{
                    _state.value = state.value.copy(
                        ex = ex.localizedMessage,
                        isSearchStarted = false
                    )
                }
            }catch (ex: Exception){
                if (ex.message?.contains("401") == true){
//                    _state.value = state.value.copy(
//                        hasJwtExpired = true,
//                        isSearchStarted = false
//                    )
                    _eventFlow.emit(NotesEvent.TokenExpiredFailure())
                }else{
                    _state.value = state.value.copy(
                        ex = ex.localizedMessage,
                        isSearchStarted = false
                    )
                }
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
            try {
                val result = notesRepository.deleteNote(state.value.credential, note.noteId)
                if (result.code == 200){
                    val newNote = _state.value.notes.filter {
                        it.noteId != result.message
                    }
                    _state.value = state.value.copy(
                        notes = newNote
                    )
                }else{

                }
            }catch (ex: CancellationException){
                if (ex.message?.contains("401") == true){
                    _state.value = state.value.copy(
                        hasJwtExpired = true,
                        isSearchStarted = false
                    )
                }else{
                    _state.value = state.value.copy(
                        ex = ex.localizedMessage,
                        isSearchStarted = false
                    )
                }
            }catch (ex: Exception){
                if (ex.message?.contains("401") == true){
                    _state.value = state.value.copy(
                        hasJwtExpired = true,
                        isSearchStarted = false
                    )
                }else{
                    _state.value = state.value.copy(
                        ex = ex.localizedMessage,
                        isSearchStarted = false
                    )
                }
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