package com.sachet.notes.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sachet.notes.components.ButtonComponent
import com.sachet.notes.components.CustomOutlinedTextFields
import com.sachet.notes.viewModal.LoginSignUpViewModal

@Composable
fun SignUpScreen(
    loginSignUpViewModal: LoginSignUpViewModal = hiltViewModel()
){
    var passwordVisibility = loginSignUpViewModal.passwordVisibility
    val firstName = loginSignUpViewModal.signUpState.value.firstName
    val lastName = loginSignUpViewModal.signUpState.value.lastName
    val email = loginSignUpViewModal.signUpState.value.email
    val userName = loginSignUpViewModal.signUpState.value.userName
    val password = loginSignUpViewModal.signUpState.value.password
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
        ){
                CustomOutlinedTextFields(
                    text = firstName,
                    hint = "Enter your First Name *",
                    onValueChange = {
                        loginSignUpViewModal.changeFirstName(it)
                    },
                    lines = 1,
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                ){}
                CustomOutlinedTextFields(
                    text = lastName,
                    hint = "Enter your Last Name *",
                    onValueChange = {
                        loginSignUpViewModal.changeLastName(it)
                    },
                    lines = 1,
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                ){}
            CustomOutlinedTextFields(
                text = email,
                hint = "Enter your Email *",
                onValueChange = {
                    loginSignUpViewModal.changeEmail(it)
                },
                lines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold
                ),
            ){}
            CustomOutlinedTextFields(
                text = userName,
                hint = "Enter your User Name *",
                onValueChange = {
                    loginSignUpViewModal.changeUserNameSignUp(it)
                },
                lines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold
                ),
            ){}
            CustomOutlinedTextFields(
                text = password,
                hint = "Enter your password *",
                onValueChange = {
                    loginSignUpViewModal.changePasswordSignUp(it)
                },
                lines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                ),
                visualTransformation = if (!passwordVisibility.value) PasswordVisualTransformation()
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
                text = "SIGN UP",
                onClick = {
                    loginSignUpViewModal.signUpUser()
                },
                enabled = firstName.isNotEmpty() && lastName.isNotEmpty() && userName.isNotEmpty()
                        && password.isNotEmpty() && email.isNotEmpty()
            )
        }
        }
    }