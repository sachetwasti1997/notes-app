package com.sachet.notes.viewModal

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.util.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
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
        getAllNoteOfUser("user1")
    }

    private fun getAllNoteOfUser(userId: String){
        viewModelScope.launch {
            try {
                val noteList = notesRepository.getAllNotes(userId)
                _state.value = state.value.copy(
                    notes = noteList,
                    ex = null
                )
            } catch (ex: CancellationException){
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
}