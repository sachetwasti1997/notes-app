package com.sachet.notes.screen

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sachet.notes.components.ButtonComponent
import com.sachet.notes.components.CustomOutlinedTextFields
import com.sachet.notes.components.CustomRadioButton
import com.sachet.notes.navigation.NotesNavigation
import com.sachet.notes.util.LoginSignUpEvent
import com.sachet.notes.util.NoteState
import com.sachet.notes.viewModal.LoginSignUpViewModal
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginSignUpScreen(
    loginSignUpViewModal: LoginSignUpViewModal = hiltViewModel(),
) {

    val token = loginSignUpViewModal.state.value.token
    val loginSignUp = loginSignUpViewModal.toggleLoginSignupScreen.value
    val state = rememberScaffoldState()

    LaunchedEffect(state.snackbarHostState){
        loginSignUpViewModal.eventFlow.collectLatest {event ->
            when(event){
                is LoginSignUpEvent.ErrorEvent -> {
                    state.snackbarHostState.showSnackbar(
                        message = event.message!!,
                        actionLabel = event.actionMessage
                    )
                }
                is LoginSignUpEvent.SuccessEvent -> {
                    state.snackbarHostState.showSnackbar(
                        message = event.message!!,
                        actionLabel = event.actionMessage
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = state
    ) {
        if (token.isNullOrEmpty()){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomRadioButton(
                        selected = loginSignUpViewModal.toggleLoginSignupScreen.value,
                        onClick = {loginSignUpViewModal.changeLoginSignUpScreen(true)}
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "LOGIN", style = TextStyle(fontWeight = FontWeight.Bold))
                    CustomRadioButton(
                        selected = !loginSignUpViewModal.toggleLoginSignupScreen.value,
                        onClick = {loginSignUpViewModal.changeLoginSignUpScreen(false)}
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "SIGN UP", style = TextStyle(fontWeight = FontWeight.Bold))
                }
                if (loginSignUp) LoginScreen(loginSignUpViewModal)
                else SignUpScreen()
            }
        }else {
            NotesNavigation(
                token
            )
        }
    }

}

@Composable
fun LoginScreen(
    loginSignUpViewModal: LoginSignUpViewModal = hiltViewModel()
){
    var userName = loginSignUpViewModal.loginState.value.userName
    var password = loginSignUpViewModal.loginState.value.password
    var passwordVisibility = loginSignUpViewModal.passwordVisibility

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomOutlinedTextFields(
                text = userName.toString(),
                hint = "Enter your username *",
                onValueChange = {
                    loginSignUpViewModal.changeUserName(it)
                },
                lines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold
                ),
            ){}
            Spacer(modifier = Modifier.height(8.dp))
            CustomOutlinedTextFields(
                text = password.toString(),
                hint = "Enter your password *",
                onValueChange = {
                    loginSignUpViewModal.changePassword(it)
                },
                lines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                ),
                visualTransformation = if (!passwordVisibility.value)PasswordVisualTransformation()
                                       else VisualTransformation.None
            ){
                IconButton(onClick = { loginSignUpViewModal.changeLoginPasswordVisibility() }) {
                    if (passwordVisibility.value) Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "visible"
                    )
                    else Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = "not visible"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            ButtonComponent(
                text = "LOG IN",
                onClick = {
                    println("CLICKED")
                    loginSignUpViewModal.loginUser()
                },
                enabled = userName.isNotEmpty() && password.isNotEmpty()
            )
        }
    }
}
















