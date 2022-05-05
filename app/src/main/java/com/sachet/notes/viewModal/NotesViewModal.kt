package com.sachet.notes.viewModal

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state

    init {
        savedStateHandle.get<String>("noteId")?.let { noteId ->
            viewModelScope.launch {
                notesRepository.getNoteById(noteId)?.also { note ->
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

    private fun onEvent(event: NotesEvent){
        Log.d("ONEVENT", "onEvent: ${state},,,${_state}")
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

    fun setNotes(noteList: State<NoteState>){
        _state.value = state.value.copy(
            notes = noteList.value.notes,
            notesOrder = noteList.value.notesOrder,
            isOrderSectionVisible = noteList.value.isOrderSectionVisible,
            ex = noteList.value.ex
        )
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