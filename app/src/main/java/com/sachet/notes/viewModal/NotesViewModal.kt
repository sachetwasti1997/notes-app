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

    private val credentialState = mutableStateOf("")

    init {
        viewModelScope.launch {
            try {
                val token = userCredRepository.getCred()
                val allNotes = notesRepository.getAllNotes("Bearer ${token?.authToken}")
                _state.value = state.value.copy(
                    notes = allNotes,
                    notesOrder = NotesOrder.Date(orderType = OrderType.Ascending),
                    ex = null
                )
            }catch (ex: CancellationException){
                _state.value = state.value.copy(
                    ex = ex.localizedMessage
                )
            }catch (ex: Exception){
                _state.value = state.value.copy(
                    ex = ex.localizedMessage
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
            ex = noteList.ex,
            initialStateSet = true
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
                println("NOT AFTER DELETE $newNote")
                _state.value = state.value.copy(
                    notes = newNote
                )
            }
        }
    }

    fun addNewNote(noteId: String, credential: String){
        viewModelScope.launch {
            notesRepository.getNoteById(credential, noteId)?.also { note ->
                println("STATE GET NOTE ${_state.value.notes}")
                val dummyNote = ArrayList(_state.value.notes)
                val previousNote = dummyNote.filter {
                    it.noteId == noteId
                }
                println("PREVIOUS $previousNote")
                if (previousNote.isNullOrEmpty()){
                    val noteListNew = ArrayList(_state.value.notes)
                    noteListNew.add(note)
                    println("BEFOR ADDING $noteListNew")
                    _state.value = state.value.copy(
                        notes = noteListNew,
                        ex = null
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
                        notes = noteListNew,
                        ex = null
                    )
                }
            }
        }
    }

}