package com.sachet.notes.viewModal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.util.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModal
@Inject constructor(
    private val notesRepository: NotesRepository
): ViewModel(){
    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state

    init {
        println("INIT CALLED")
        getAllNoteOfUser("user1")
    }

    private fun getAllNoteOfUser(userId: String){
        viewModelScope.launch {
            _state.value = state.value.copy(
                notes = notesRepository.getAllNotes(userId)
            )
        }
    }
}