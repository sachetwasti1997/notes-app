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
    val state = rememberScaffoldState()

    LaunchedEffect(state.snackbarHostState){
        loginSignUpViewModal.eventFlow.collectLatest {event ->
            when(event){
                is LoginSignUpEvent.ErrorEvent -> {
                    state.snackbarHostState.showSnackbar(
                        message = event.message!!,
                        actionLabel = "Please try again later!"
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = state
    ) {
        if (token.isNullOrEmpty()){
            LoginScreen(loginSignUpViewModal)
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
    var passwordVisibility = loginSignUpViewModal.loginState.value.passwordVisibility

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
                visualTransformation = if (!passwordVisibility)PasswordVisualTransformation()
                                       else VisualTransformation.None
            ){
                IconButton(onClick = { loginSignUpViewModal.changeLoginPasswordVisibility() }) {
                    if (passwordVisibility) Icon(
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














