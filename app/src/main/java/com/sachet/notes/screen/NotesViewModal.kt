package com.sachet.notes.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.data.Note
import com.sachet.notes.data.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class NotesViewModal
@Inject constructor(
    var notesRepository: NotesRepository,
) : ViewModel(){

    var data = mutableStateOf(
        Response(ArrayList<Note>(), false, Exception(""))
    )

    var newNote = mutableStateOf(false)

    fun saveNote(note: Note){
        viewModelScope.launch {
            notesRepository.saveNotes(note)
            newNote.value = true
            Log.d("NOTEVM", "saveNote: $newNote")
        }
    }

    fun getAllNoteOfUser(userId: String){
        viewModelScope.launch {
            data.value.loading = true
            data.value = notesRepository.getAllNotes(userId)
            Log.d("NotesRepo", "getAllNoteOfUser: ${data.value}")
            if (data.value.data?.isEmpty() == true){
                data.value.data = null
            }
            data.value.loading = false
        }
    }

}