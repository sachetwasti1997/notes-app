package com.sachet.notes.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sachet.notes.components.ButtonComponent
import com.sachet.notes.components.CustomOutlinedTextFields
import com.sachet.notes.data.LoginRequest
import com.sachet.notes.db.UserCredDao
import com.sachet.notes.util.AddEditNoteEvent
import com.sachet.notes.viewModal.LoginSignUpViewModal

@Composable
fun LoginSignUpScreen(
    loginSignUpViewModal: LoginSignUpViewModal = hiltViewModel(),
) {

    val token = loginSignUpViewModal.state.value.token
    if (token.isNullOrEmpty()){
        LoginScreen(loginSignUpViewModal)
    }else{
        loginSignUpViewModal.getUserToken()
    }

}

@Composable
fun LoginScreen(
    loginSignUpViewModal: LoginSignUpViewModal = hiltViewModel()
){
    var userName = loginSignUpViewModal.loginState.value.userName
    var password = loginSignUpViewModal.loginState.value.password
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
            )
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
            )
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
}














