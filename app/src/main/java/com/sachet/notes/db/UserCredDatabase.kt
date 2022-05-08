package com.sachet.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sachet.notes.data.Security

@Database(
    entities = [Security::class],
    version = 1
)
abstract class UserCredDatabase: RoomDatabase() {

    abstract val userCredDao: UserCredDao

    companion object{
        const val DATABASE_NAME = "user_cred_db"
    }

}