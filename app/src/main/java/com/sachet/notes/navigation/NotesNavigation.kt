package com.sachet.notes.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sachet.notes.data.Note
import com.sachet.notes.screen.CreateNoteScreen
import com.sachet.notes.screen.LoginSignUpScreen
//import com.sachet.notes.screen.CreateNoteScreen
import com.sachet.notes.screen.NoteScreen
import com.sachet.notes.util.NoteState

@Composable
fun NotesNavigation(token: String){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NotesScreen.LoginScreen.name,
    ){
        composable(
            route = NotesScreen.LoginScreen.name
        ){
            LoginSignUpScreen(navController = navController)
        }
        composable(
            route = NotesScreen.HomeScreen.name+"?noteId={noteId}",
            arguments = listOf(
                navArgument(
                    name= "noteId"
                ){
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ){
            val noteId = it.arguments?.getString("noteId")
            NoteScreen(navController = navController, noteId = noteId, token = token)
        }
        composable(
            route = NotesScreen.CreateNotesScreen.name
                    +"?noteId={noteId}&noteColor={noteColor}",
            arguments = listOf(
                navArgument(
                    name= "noteId"
                ){
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(
                    name= "noteColor"
                ){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            val color = it.arguments?.getInt("noteColor")
            var colorInt = -1
            val noteId = it.arguments?.getString("noteId")
            CreateNoteScreen(
                navController = navController,
                credential = token,
                noteColor = colorInt,
                noteId = noteId
            )
        }
    }
}