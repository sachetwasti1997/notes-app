package com.sachet.notes.data

import com.sachet.notes.ui.theme.*

data class Note constructor(
    val noteId: String?= null,
    val title: String ,
    val description: String ,
    val userId: String = "user1",
    val localDateTime: String ?= null,
    val color: Int
){
    companion object{
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}
