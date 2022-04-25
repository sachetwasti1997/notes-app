package com.sachet.notes.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sachet.notes.data.Note
import com.sachet.notes.screen.CreateNoteScreen
import com.sachet.notes.screen.NoteScreen

@Composable
fun NotesNavigation(noteList: MutableList<Note>?, onAddNote: (Note) -> Unit = {}, onDeleteNot: (id: String) -> Unit = {}){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NotesScreen.HomeScreen.name,
    ){
        composable(NotesScreen.HomeScreen.name){
            NoteScreen(navController = navController, notesList = noteList)
        }
        composable(NotesScreen.CreateNotesScreen.name){
            CreateNoteScreen(
                navController = navController,
            )
        }
    }
}