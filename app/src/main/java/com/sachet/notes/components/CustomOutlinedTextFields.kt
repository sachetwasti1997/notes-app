package com.sachet.notes.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomOutlinedTextFields(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    lines: Int = 1,
    trailingIconRequired: Boolean = false,
    icon : @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current
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
            maxLines = lines,
            visualTransformation = visualTransformation,
            trailingIcon = icon,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
        )
    }
}