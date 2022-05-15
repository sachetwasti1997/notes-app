package com.sachet.notes.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sachet.notes.components.ButtonComponent
import com.sachet.notes.components.CustomOutlinedTextFields
import com.sachet.notes.components.CustomRadioButton
import com.sachet.notes.navigation.NotesScreen
import com.sachet.notes.util.LoginSignUpEvent
import com.sachet.notes.viewModal.LoginSignUpViewModal
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

@Composable
fun LoginSignUpScreen(
    navController: NavController,
    loginSignUpViewModal: LoginSignUpViewModal = hiltViewModel(),
) {

    val token = loginSignUpViewModal.state.value.token
    val loginSignUp = loginSignUpViewModal.toggleLoginSignupScreen.value
    val isSearchStarted = loginSignUpViewModal.state.value.isSearchStarted
    val state = rememberScaffoldState()

    LaunchedEffect(true) {
        loginSignUpViewModal.eventFlow.collectLatest { event ->
            when (event) {
                is LoginSignUpEvent.ErrorEvent -> {
                    state.snackbarHostState.showSnackbar(
                        message = event.message!!,
                        actionLabel = event.actionMessage
                    )
                }
                is LoginSignUpEvent.SuccessEventSignUp -> {
                    state.snackbarHostState.showSnackbar(
                        message = event.message!!,
                        actionLabel = event.actionMessage
                    )
                }
                is LoginSignUpEvent.SuccessEventLogIn -> {
                    navController.popBackStack()
                    navController.navigate(NotesScreen.HomeScreen.name)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = state
    ) {
//        if (token?.isNotEmpty() == true) {
//            LaunchedEffect(Unit) {
//                navController.popBackStack()
//                navController.navigate(NotesScreen.HomeScreen.name + "?noteId=")
//            }
//        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            color = Color.Cyan
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                elevation = 20.dp,
                shape = RoundedCornerShape(
                    topStartPercent = 20,
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomRadioButton(
                            selected = loginSignUpViewModal.toggleLoginSignupScreen.value,
                            onClick = { loginSignUpViewModal.changeLoginSignUpScreen(true) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "LOGIN", style = TextStyle(fontWeight = FontWeight.Bold))
                        CustomRadioButton(
                            selected = !loginSignUpViewModal.toggleLoginSignupScreen.value,
                            onClick = { loginSignUpViewModal.changeLoginSignUpScreen(false) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "SIGN UP", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    if (isSearchStarted) {
                        CircularProgressIndicator()
                    }
                    if (loginSignUp) LoginScreen(loginSignUpViewModal, navController)
                    else SignUpScreen()
                }
            }
        }
    }
}


@Composable
fun LoginScreen(
    loginSignUpViewModal: LoginSignUpViewModal = hiltViewModel(),
    navController: NavController
) {
    var userName = loginSignUpViewModal.loginState.value.userName
    var password = loginSignUpViewModal.loginState.value.password
    var passwordVisibility = loginSignUpViewModal.passwordVisibility

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
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
            ) {}
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
                visualTransformation = if (!passwordVisibility.value) PasswordVisualTransformation()
                else VisualTransformation.None
            ) {
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
                    loginSignUpViewModal.loginUser()
                },
                enabled = userName.isNotEmpty() && password.isNotEmpty()
            )
        }
}
















