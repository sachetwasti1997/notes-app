package com.sachet.notes.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sachet.notes.viewModal.NotesViewModal
import com.sachet.notes.model.NotesOrder
import com.sachet.notes.model.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    notesOrder: NotesOrder = NotesOrder.Date(OrderType.Ascending),
    onOrderChange: (NotesOrder) -> Unit,
    viewModal: NotesViewModal = hiltViewModel()
){
    Log.d("ORDER", "OrderSection: ${viewModal.state.value.notesOrder}")
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