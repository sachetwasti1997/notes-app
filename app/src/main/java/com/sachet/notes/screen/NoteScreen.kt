package com.sachet.notes.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sachet.notes.data.Note
import com.sachet.notes.navigation.NotesScreen

@Composable
fun NoteScreen(
    navController: NavController,
    notesList: MutableList<Note>?,
    onDeleteNote: () -> Unit = {}
){
    Scaffold(
        topBar = {
            CreateMainTopBar(navController)
        }
    ) {
        CreateNoteList(notesListRefined = notesList, onDeleteNote)
    }
}

@Composable
fun CreateMainTopBar(navController: NavController){
    TopAppBar(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(CornerSize(10.dp))),
        backgroundColor = Color.Magenta
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Note",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task",
                    tint = Color.White,
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            navController.navigate(route = NotesScreen.CreateNotesScreen.name)
                        }
                )
            }
        }
    }
}

@Composable
fun CreateNoteList(notesListRefined: MutableList<Note>?, onDeleteNote: () -> Unit, viewModal: NotesViewModal = hiltViewModel()){
    val notesList = viewModal.data.value.data?.toMutableList()
    val isLoading = viewModal.data.value.loading
//    val newNote = viewModal.newNote.value
//    if (newNote){
//        viewModal.getAllNoteOfUser("user1")
//    }
    Log.d("CREATENOTEEE", "CreateNoteList: $notesList $isLoading")
    if (notesList?.isEmpty() == true /*&& isLoading == false*/){
        viewModal.getAllNoteOfUser("user1")
        Log.d("EMPTYNOTELIST", "CreateNoteList: $notesList $isLoading")
    }
    else if (notesList == null){
//        Log.d("NOTELIST", "CreateNoteList: ${}")
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                modifier = Modifier
                    .height(90.dp)
                    .width(400.dp),
                shape = RoundedCornerShape(CornerSize(12.dp)),
                elevation = 15.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        style = TextStyle(
                            color = Color.Magenta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        ),
                        text = "Click '+' to add a note"
                    )
                }
            }
        }
    }
    else{
        Column(modifier = Modifier.padding(12.dp)){
            LazyColumn{
                items(notesList!!.toList()/*.toMutableList()*/){
                    NoteRow(note = it, onDeleteNote = onDeleteNote)
                }
            }
        }
    }
}

@Composable
fun NoteRow(note: Note, onDeleteNote: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .defaultMinSize(minHeight = 100.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(CornerSize(10.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                .fillMaxWidth(fraction = 0.7f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = note.title.toString(), style = MaterialTheme.typography.subtitle2)
                Text(text = note.description.toString(), style = MaterialTheme.typography.subtitle1)
                note.localDateTime?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.3f)
                    .clickable { onDeleteNote() },
                shape = RoundedCornerShape(CornerSize(4.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Movie Image"
                )
            }
        }
    }
}


