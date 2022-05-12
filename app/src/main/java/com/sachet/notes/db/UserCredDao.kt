package com.sachet.notes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.sachet.notes.data.Security
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE

@Dao
interface UserCredDao {

    @Query("SELECT * FROM security")
    suspend fun getCred(): Security?

    @Insert(onConflict = REPLACE)
    suspend fun insetToken(security: Security)

    @Query("DELETE FROM security")
    suspend fun deleteToken()

}