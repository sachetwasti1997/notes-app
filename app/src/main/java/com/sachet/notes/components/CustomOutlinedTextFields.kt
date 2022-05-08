package com.sachet.notes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun CustomOutlinedTextFields(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    lines: Int = 1
) {
    Box(
    modifier = modifier
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
//            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(text = hint, style = textStyle)
            },
            maxLines = lines
        )
    }
}