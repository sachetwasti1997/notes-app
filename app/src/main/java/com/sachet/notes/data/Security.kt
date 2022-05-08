package com.sachet.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "security")
data class Security(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "token_id")
    val id: Int,
    @ColumnInfo(name = "token")
    var authToken: String
)
