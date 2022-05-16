package com.sachet.notes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sachet.notes.screen.CreateNoteScreen
import com.sachet.notes.screen.LoginSignUpScreen
//import com.sachet.notes.screen.CreateNoteScreen
import com.sachet.notes.screen.NoteScreen

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
            NoteScreen(navController = navController, token = token)
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