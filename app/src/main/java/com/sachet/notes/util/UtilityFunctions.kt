package com.sachet.notes.util

import android.util.Log
import com.sachet.notes.data.Note
import com.sachet.notes.model.NotesOrder
import com.sachet.notes.model.OrderType

fun orderBy(noteList: List<Note>, notesOrder: NotesOrder): List<Note>{
    var modifiedNotes = noteList
//    Log.d("SORT", "orderBy: $noteList")
    when(notesOrder.orderType){
        is OrderType.Ascending -> {
            modifiedNotes = when(notesOrder){
                is NotesOrder.Title -> noteList.sortedBy { it.title.lowercase() }
                is NotesOrder.Date -> noteList.sortedBy { it.localDateTime }
                is NotesOrder.Color -> noteList.sortedBy { it.color }
            }
        }
        is OrderType.Descending -> {
            modifiedNotes = when(notesOrder){
                is NotesOrder.Title -> noteList.sortedByDescending { it.title.lowercase() }
                is NotesOrder.Date -> noteList.sortedByDescending { it.localDateTime }
                is NotesOrder.Color -> noteList.sortedByDescending { it.color }
            }
        }
    }
    Log.d("RETURNLIST", "orderBy: $modifiedNotes")
    return modifiedNotes
}