package com.sachet.notes.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sachet.notes.components.ButtonComponent
import com.sachet.notes.components.InputTextComponent
import com.sachet.notes.data.Note


@Composable
fun CreateNoteScreen(
    navController: NavController,
    onAddNote: (Note) -> Unit
){

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            CreateTopBar(navController = navController)
        }

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(300.dp),
                elevation = 15.dp
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    InputTextComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        text = title,
                        label = "Title",
                        onTextChange = {
                            title = it
                        }
                    )

                    InputTextComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        text = description,
                        label = "Add a Note",
                        onTextChange = {
                            description = it
                        },
                        maxLine = 5
                    )

                    ButtonComponent(
                        modifier = Modifier
                            .padding(10.dp),
                        text = "Save",
                        onClick = {
                            onAddNote(Note(title = title, description = description))
                            navController.popBackStack()
                        },
                        enabled = (title.isNotEmpty() && description.isNotEmpty())
                    )

                }
            }
        }
        }
}

@Composable
fun CreateTopBar(navController: NavController){
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
            Surface(modifier = Modifier.size(30.dp), color = Color.Magenta) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = Color.White,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Create Note",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}