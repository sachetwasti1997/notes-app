package com.sachet.notes.viewModal

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.data.Note
import com.sachet.notes.db.UserCredRepository
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.util.NoteState
import com.sachet.notes.util.NotesEvent
import com.sachet.notes.util.orderBy
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val credentialState = mutableStateOf("")

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
            is NotesEvent.DeleteNote -> {
//                viewModelScope.launch {
//                    println("DELETE ${credentialState.value} ${event.note.noteId}")
//                    val result = notesRepository.deleteNote(credentialState.value, event.note.noteId)
//                    if (result.data != null){
//                        val newNote = _state.value.notes.filter {
//                            it.noteId != result.data
//                        }
//                        _state.value = state.value.copy(
//                            notes = newNote
//                        )
//                    }
//                }
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

    fun setNotes(credential: String, noteList: NoteState){
        println("SETTING $credential")
        _state.value = state.value.copy(
            notes = noteList.notes,
            notesOrder = noteList.notesOrder,
            isOrderSectionVisible = noteList.isOrderSectionVisible,
            ex = noteList.ex
        )
        credentialState.value = credential
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
//        viewModelScope.launch{
//            onEvent(NotesEvent.DeleteNote(note))
//        }
        viewModelScope.launch {
            println("DELETE ${credential} ${note.noteId}")
            val result = notesRepository.deleteNote(credential, note.noteId)
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

    fun addNewNote(noteId: String, credential: String){
        viewModelScope.launch {
            notesRepository.getNoteById(credential, noteId)?.also { note ->
                val previousNote = state.value.notes.filter {
                    it.noteId == noteId
                }
                if (previousNote.isNullOrEmpty()){
                    val noteListNew = ArrayList(_state.value.notes)
                    noteListNew.add(note)
                    _state.value = state.value.copy(
                        notes = noteListNew
                    )
                }else{
                    val newNote = previousNote[0]
                    newNote.color = note.color
                    newNote.description = note.description
                    newNote.localDateTime = note.localDateTime
                    newNote.noteId = note.noteId
                    newNote.title = note.title
                    newNote.userId = note.userId
                    val noteListNew = ArrayList(_state.value.notes)
                    noteListNew.remove(previousNote[0])
                    noteListNew.add(note)
                    _state.value = state.value.copy(
                        notes = noteListNew
                    )
                }
            }
        }
    }

}