package com.sachet.notes.viewModal

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.data.Note
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.util.NoteState
import com.sachet.notes.util.NotesEvent
import com.sachet.notes.util.orderBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModal
@Inject constructor(
    var notesRepository: NotesRepository,
) : ViewModel(){

    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state

    init {
        getAllNoteOfUser("user1")
    }

    private fun onEvent(event: NotesEvent){
        Log.d("ONEVENT", "onEvent: ${state},,,${_state}")
        when(event){
            is NotesEvent.Order -> {
                if (state.value.notesOrder::class == event.newNotesOrder::class &&
                    state.value.notesOrder.orderType == event.newNotesOrder.orderType
                ){
                    return
                }
                _state.value = state.value.copy(
                    notes = orderBy(state.value.notes, event.newNotesOrder),
                    notesOrder = event.newNotesOrder
                )
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    val result = notesRepository.deleteNote(event.note.noteId!!)
                    println(result)
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
            is NotesEvent.RestoreNotes -> {

            }
            is NotesEvent.ToggleOrderSelection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    fun getAllNoteOfUser(userId: String){
        viewModelScope.launch {
            _state.value = state.value.copy(
                notes = notesRepository.getAllNotes("user1")
            )
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

    fun deleteNote(note: Note){
//        viewModelScope.launch{
            onEvent(NotesEvent.DeleteNote(note))
//        }
    }

}