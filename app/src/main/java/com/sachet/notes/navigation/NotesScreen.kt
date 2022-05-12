package com.sachet.notes.navigation

import java.lang.IllegalArgumentException

enum class NotesScreen {

    LoginScreen,
    HomeScreen,
    CreateNotesScreen;

    companion object{
        fun fromRoute(route: String?)
        = when(route?.substringBefore("/")){
            LoginScreen.name -> LoginScreen
            HomeScreen.name -> HomeScreen
            CreateNotesScreen.name -> CreateNotesScreen
            null -> LoginScreen
            else -> throw IllegalArgumentException("Route $route does not exist.")
        }
    }

}