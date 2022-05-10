package com.sachet.notes.viewModal

import android.util.JsonToken
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.data.LoginRequest
import com.sachet.notes.data.Note
import com.sachet.notes.data.User
import com.sachet.notes.db.UserCredRepository
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.network.UserRepository
import com.sachet.notes.util.LoginSignUpEvent
import com.sachet.notes.util.LoginState
import com.sachet.notes.util.RegisterState
import com.sachet.notes.util.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSignUpViewModal
@Inject constructor(
    private val userCredRepository: UserCredRepository,
    private val notesRepository: NotesRepository,
    private val userRepository: UserRepository,
): ViewModel(){

    private var _state = mutableStateOf(RegisterState())
    var state: State<RegisterState> = _state

    private var _loginState = mutableStateOf(LoginState())
    var loginState: State<LoginState> = _loginState

    private var _signUpState = mutableStateOf(SignUpState())
    var signUpState: State<SignUpState> = _signUpState

    private val _eventFlow = MutableSharedFlow<LoginSignUpEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val toggleLoginSignupScreen = mutableStateOf(false)

    init {
        getUserToken()
    }

    fun getUserToken(){
        viewModelScope.launch {
            var token = userCredRepository.getCred()
            _state.value = state.value.copy(
                token = token?.authToken
            )
        }
    }

    fun loginUser(){
        viewModelScope.launch {
            try{
                val loginRequest = LoginRequest(loginState.value.userName, loginState.value.password)
                println("$loginRequest LOGINREQ")
                val loginResponse = userRepository.loginUser(loginRequest)
                if (loginResponse.exception == null){
                    userCredRepository.saveToken(loginResponse.token!!)
                    _state.value = state.value.copy(
                        token = loginResponse.token
                    )
                }else{
                    _eventFlow.emit(LoginSignUpEvent.ErrorEvent(message = loginResponse.exception))
                }
            }catch (ex: CancellationException){
                println(ex.localizedMessage)
                var message = "There is an error!"
                if (ex.message?.contains("Failed to connect") == true){
                    message = "Looks Like server is down"
                }
                _eventFlow.emit(LoginSignUpEvent.ErrorEvent(message = message))
            }catch (ex: Exception){
                println(ex.localizedMessage)
                var message = "There is an error!"
                if (ex.message?.contains("Failed to connect") == true){
                    message = "Looks Like server is down"
                }
                _eventFlow.emit(LoginSignUpEvent.ErrorEvent(message = message))
            }
        }
    }

    fun getNotes(token: String){
        viewModelScope.launch {
            try {
                notesRepository.getAllNotes("Bearer $token").also {
                    _state.value = state.value.copy(
                        noteList = it
                    )
                }
            }catch (ex: java.util.concurrent.CancellationException){
//            Log.d("Notes", "saveNotes: $ex")

            }catch (ex: java.lang.Exception){
//            Log.d("Notes", "saveNotes: $ex")
                if(ex.message?.contains("404") == true){
                    _state.value = state.value.copy(
                        ex = "Cannot find notes for the user"
                    )
                }
            }
        }
    }

    fun signUpUser(user: User){}

    fun changeUserName(userName: String){
        _loginState.value = loginState.value.copy(
            userName = userName
        )
    }

    fun changePassword(password: String){
        _loginState.value = loginState.value.copy(
            password = password
        )
    }

    fun changeLoginPasswordVisibility(){
        _loginState.value = loginState.value.copy(
            passwordVisibility = !_loginState.value.passwordVisibility
        )
    }

}