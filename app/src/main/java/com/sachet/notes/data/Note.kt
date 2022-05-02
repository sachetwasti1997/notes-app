package com.sachet.notes.data

import com.sachet.notes.ui.theme.*

data class Note constructor(
    var noteId: String?= null,
    var title: String,
    var description: String,
    var userId: String = "user1",
    var localDateTime: String ?= null,
    var color: Int
){
    companion object{
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}
