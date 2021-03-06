package com.sachet.notes.viewModal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.data.Note
import com.sachet.notes.db.UserCredRepository
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.util.AddEditNoteEvent
import com.sachet.notes.data.NotesTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModal
@Inject constructor(
    private val notesRepository: NotesRepository,
    private val userCredRepository: UserCredRepository,
    savedStateHandle: SavedStateHandle
) :ViewModel() {

    private val _noteTitle = mutableStateOf(NotesTextFieldState(hint = "Enter a title*"))
    val noteTitle : State<NotesTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NotesTextFieldState(hint = "Enter the content*"))
    val noteContent : State<NotesTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf<Int>(Note.noteColors.random().toArgb())
    val noteColor : State<Int> = _noteColor

    val str: String ?= null

    val credential = mutableStateOf(str)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentNoteId : String ?= null

    init {
                viewModelScope.launch {
                    val token = userCredRepository.getCred()
                    credential.value = "Bearer ${token?.authToken}"
                        savedStateHandle.get<String>("noteId")?.let { noteId ->
                            if (noteId.isNotEmpty()){
                                try {
                                    notesRepository.getNoteById("Bearer ${token?.authToken}", noteId)?.also { note ->
                                        currentNoteId = note.noteId.toString()
                                        _noteTitle.value = noteTitle.value.copy(
                                            text = note.title,
                                            isHintVisible = false
                                        )
                                        _noteContent.value = noteContent.value.copy(
                                            text = note.description,
                                            isHintVisible = false
                                        )
                                        _noteColor.value = note.color
                                    }
                                }catch (ex: CancellationException){
                                    if (ex.message?.contains("401") == true) {
                                        _eventFlow.emit(UiEvent.LogUserOut(message = "Session has expired, please log in again to continue!"))
                                    }else{
                                        _eventFlow.emit(UiEvent.FailureEvent(message = ex.message))
                                    }
                                }catch (ex: Exception){
                                    if (ex.message?.contains("401") == true) {
                                        _eventFlow.emit(UiEvent.LogUserOut(message = "Session has expired, please log in again to continue!"))
                                    }else{
                                        _eventFlow.emit(UiEvent.FailureEvent(message = ex.message))
                                    }
                                }
                    }
                }
            }
        }


    fun setCredential(token: String){
        credential.value = token
    }

    open class UiEvent(var noteId: String?) {
        class SaveNote(noteId: String?) : UiEvent(noteId)
        class LogUserOut(message: String?): UiEvent(message)
        class FailureEvent(message: String?): UiEvent(message)
    }

    fun onEvent(event: AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        notesRepository.saveNotes(
                            credential.value,
                            Note(
                                noteId = if (currentNoteId != null) currentNoteId else null,
                                title = noteTitle.value.text,
                                description = noteContent.value.text,
                                userId = "user1",
                                color = _noteColor.value
                            )
                        ).also {
                            _eventFlow.emit(UiEvent.SaveNote(it.noteId))
                        }
                    }catch (ex: CancellationException){
                        if (ex.message?.contains("401") == true) {
                            _eventFlow.emit(UiEvent.LogUserOut(message = "Session has expired, please log in again to continue!"))
                        }else{
                            _eventFlow.emit(UiEvent.FailureEvent(message = ex.message))
                        }
                    }catch (ex: Exception){
                        if (ex.message?.contains("401") == true){
                            _eventFlow.emit(UiEvent.LogUserOut(message = "Session has expired, please log in again to continue!"))
                        }else{
                            _eventFlow.emit(UiEvent.FailureEvent(message = ex.message))
                        }
                    }
                }
            }
        }
    }

    fun logOut(){
        viewModelScope.launch {
            userCredRepository.deleteToken()
        }
    }

}