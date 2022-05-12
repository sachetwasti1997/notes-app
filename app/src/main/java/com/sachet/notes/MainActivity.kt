package com.sachet.notes

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sachet.notes.db.UserCredDatabase
import com.sachet.notes.navigation.NotesNavigation
import com.sachet.notes.screen.LoginScreen
import com.sachet.notes.screen.LoginSignUpScreen
import com.sachet.notes.ui.theme.NotesTheme
import com.sachet.notes.viewModal.MainViewModal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.setProperty (DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
        setContent {
            NotesTheme {
                // A surface container using the 'background' color from the theme
                MainApp(application)
            }
        }
    }
}

@Composable
fun MainApp(application: Application,viewModal: MainViewModal = hiltViewModel()){
    NotesNavigation(token = "")
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotesTheme {
        Greeting("Android")
    }
}