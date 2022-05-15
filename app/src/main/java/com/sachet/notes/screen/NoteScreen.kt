package com.sachet.notes.screen

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sachet.notes.components.NoteItem
import com.sachet.notes.components.OrderSection
import com.sachet.notes.navigation.NotesScreen
import com.sachet.notes.util.NotesEvent
import com.sachet.notes.viewModal.NotesViewModal
import kotlinx.coroutines.flow.collectLatest


@Composable
fun NoteScreen(
    navController: NavController,
    noteId: String?,
    token: String,
    viewModal: NotesViewModal = hiltViewModel()
) {

    val state = viewModal.state.value
    val scaffoldState = rememberScaffoldState()
    val logOut = viewModal.logOut.value

//    if (state.ex != null){
    LaunchedEffect(true) {
        viewModal.eventFlow.collectLatest { event ->
            when (event) {
                is NotesEvent.FailureEvent -> {
                    println("FAILUREEVENT")
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                    )
                }
                is NotesEvent.TokenExpiredFailure -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Session has expired please login again to continue",
                    )
                    viewModal.logOut()
                    navController.popBackStack()
                    navController.navigate(NotesScreen.LoginScreen.name)
                }
            }
        }
    }
//    }

//    if (viewModal.state.value.hasJwtExpired) {
//        LaunchedEffect(Unit) {
//            scaffoldState.snackbarHostState.showSnackbar(
//                message = "Token Expired, please log in again"
//            )
//            viewModal.logOut()
//            navController.popBackStack()
//            navController.navigate(NotesScreen.LoginScreen.name)
//        }
//    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        NotesScreen.CreateNotesScreen.name
                                + "?noteId=&noteColor=-1"
                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        scaffoldState = scaffoldState
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Notes",
                    style = MaterialTheme.typography.h4
                )
                Row {
                    IconButton(
                        onClick = {
                            viewModal.toggleOrderSection()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Sort")
                    }
                    IconButton(onClick = {
                        viewModal.logOut()
                        navController.popBackStack()
                        navController.navigate(NotesScreen.LoginScreen.name)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Account Actions"
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                OrderSection(
                    onOrderChange = {
                        Log.d("CHANGENOTEORDER", "NoteScreen: $it")
                        viewModal.toggleNotesOrder(NotesEvent.Order(it))
                    },
                    notesOrder = state.notesOrder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            if (state.isSearchStarted) {
                CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                            .clickable {
                                navController.navigate(
                                    route = NotesScreen.CreateNotesScreen.name
                                            + "?noteId=${note.noteId}&noteColor=${note.color}"
                                )
                            },
                        onDeleteClick = {
                            viewModal.deleteNote(credential = "Bearer $token", note)
                        }
                    )
                }
            }
        }
    }

}




