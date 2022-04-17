package com.sachet.notes.navigation

import java.lang.IllegalArgumentException

enum class NotesScreen {

    HomeScreen,
    CreateNotesScreen;

    companion object{
        fun fromRoute(route: String?)
        = when(route?.substringBefore("/")){
            HomeScreen.name -> HomeScreen
            CreateNotesScreen.name -> CreateNotesScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route does not exist.")
        }
    }

}