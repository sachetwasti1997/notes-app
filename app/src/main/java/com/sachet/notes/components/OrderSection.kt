package com.sachet.notes.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import com.sachet.notes.util.NotesOrder
import com.sachet.notes.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    notesOrder: NotesOrder = NotesOrder.Date(OrderType.Ascending),
    onOrderChange: (NotesOrder) -> Unit
){
    Log.d("ORDER", "OrderSection: $notesOrder")
    Column(
        modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = notesOrder is NotesOrder.Title,
                onCheck = { onOrderChange(NotesOrder.Title(notesOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = notesOrder is NotesOrder.Date,
                onCheck = { onOrderChange(NotesOrder.Date(notesOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Color",
                selected = notesOrder is NotesOrder.Color,
                onCheck = { onOrderChange(NotesOrder.Color(notesOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = notesOrder.orderType is OrderType.Ascending,
                onCheck = {
                    onOrderChange(notesOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = notesOrder.orderType is OrderType.Descending,
                onCheck = {
                    onOrderChange(notesOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}