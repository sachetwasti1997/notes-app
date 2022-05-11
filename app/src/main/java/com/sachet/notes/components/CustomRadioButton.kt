package com.sachet.notes.components

import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable

@Composable
fun CustomRadioButton(
    selected: Boolean = false,
    onClick: () -> Unit
){
    RadioButton(selected = selected, onClick = onClick)
}