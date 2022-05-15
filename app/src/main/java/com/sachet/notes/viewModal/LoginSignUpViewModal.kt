package com.sachet.notes.viewModal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sachet.notes.data.LoginRequest
import com.sachet.notes.data.User
import com.sachet.notes.db.UserCredRepository
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.network.UserRepository
import com.sachet.notes.util.LoginSignUpEvent
import com.sachet.notes.model.LoginState
import com.sachet.notes.model.RegisterState
import com.sachet.notes.model.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _eventFlow = Channel<LoginSignUpEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    val toggleLoginSignupScreen = mutableStateOf(false)
    var passwordVisibility = mutableStateOf(false)

    init {
        try {
            viewModelScope.launch {
                _state.value = state.value.copy(
                    isSearchStarted = true
                )
                userCredRepository.getCred().also {security ->
                    if (security?.authToken.isNullOrEmpty()){
                        _eventFlow.send(LoginSignUpEvent.ErrorEvent("Please Login to continue", ""))
                        _state.value = state.value.copy(
                            isSearchStarted = false
                        )
                    }else{
                        _state.value = state.value.copy(
                            isSearchStarted = false,
                        )
                        _eventFlow.send(LoginSignUpEvent.SuccessEventLogIn("Subscribing"))
                    }

                }
            }
        }catch (ex: CancellationException){
            println(ex.message)
        }catch (ex: Exception){
           println(ex.message)
        }
    }

    fun loginUser(){
        viewModelScope.launch {
            try{
                _state.value = state.value.copy(
                    isSearchStarted = true
                )
                val loginRequest = LoginRequest(loginState.value.userName, loginState.value.password)
                val loginResponse = userRepository.loginUser(loginRequest)
                if (loginResponse.exception == null){
                    userCredRepository.saveToken(loginResponse.token!!)
                    _state.value = state.value.copy(
                        isSearchStarted = false
                    )
                    _eventFlow.send(LoginSignUpEvent.SuccessEventLogIn(message = "Logged In"))
                }else{
                    _state.value = state.value.copy(
                        isSearchStarted = false
                    )
                    _eventFlow.send(LoginSignUpEvent.ErrorEvent(message = loginResponse.exception))
                }
            }catch (ex: CancellationException){
                var message = "There is an error!"
                if (ex.message?.contains("Failed to connect") == true){
                    message = "Looks Like server is down"
                }
                _eventFlow.send(LoginSignUpEvent.ErrorEvent(message = message, actionMessage = "Try Again later!"))
            }catch (ex: Exception){
                var message = "There is an error!"
                if (ex.message?.contains("Failed to connect") == true){
                    message = "Looks Like server is down"
                }
                _eventFlow.send(LoginSignUpEvent.ErrorEvent(message = message, actionMessage = "Try Again later!"))
            }
        }
    }

    fun signUpUser(){
        viewModelScope.launch {
            try {
                val user = User(
                    firstName = signUpState.value.firstName,
                    lastName = signUpState.value.lastName,
                    email = signUpState.value.email,
                    userName = signUpState.value.userName,
                    password = signUpState.value.password
                )
                val signUpResponse = userRepository.saveUser(user)
                if (signUpResponse.code != 200){
                    _eventFlow.send(LoginSignUpEvent.ErrorEvent(message = signUpResponse.message))
                }else{
                    _eventFlow.send(LoginSignUpEvent.SuccessEventSignUp(message = signUpResponse.message))
                    _signUpState.value = signUpState.value.copy(
                        firstName = "",
                        lastName = "",
                        userName = "",
                        email = "",
                        password = ""
                    )
                    toggleLoginSignupScreen.value = true
                }
            }catch (ex: CancellationException){
                var message = "There is an error!"
                if (ex.message?.contains("Failed to connect") == true){
                    message = "Looks Like server is down"
                }
                _eventFlow.send(LoginSignUpEvent.ErrorEvent(message = message, actionMessage = "Try Again later!"))
            }catch (ex: Exception){
                var message = "There is an error!"
                if (ex.message?.contains("Failed to connect") == true){
                    message = "Looks Like server is down"
                }
                _eventFlow.send(LoginSignUpEvent.ErrorEvent(message = message, actionMessage = "Try Again later!"))
            }
        }
    }

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
        passwordVisibility.value = !passwordVisibility.value
    }

    fun changeLoginSignUpScreen(bool: Boolean){
        if (toggleLoginSignupScreen.value != bool){
            toggleLoginSignupScreen.value = bool
        }
    }

    fun changeFirstName(firstName: String){
        _signUpState.value = signUpState.value.copy(
            firstName = firstName
        )
    }

    fun changeLastName(lastName: String){
        _signUpState.value = signUpState.value.copy(
            lastName = lastName
        )
    }

    fun changeEmail(email: String){
        _signUpState.value = signUpState.value.copy(
            email = email
        )
    }

    fun changeUserNameSignUp(userName: String){
        _signUpState.value = signUpState.value.copy(
            userName = userName
        )
    }

    fun changePasswordSignUp(password: String){
        _signUpState.value = signUpState.value.copy(
            password = password
        )
    }

}