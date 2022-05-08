package com.sachet.notes.db

import com.sachet.notes.data.Security
import kotlinx.coroutines.flow.Flow

class UserCredRepository(
    private val dao: UserCredDao
) {

    suspend fun getCred(): Security? {
        return dao.getCred()
    }

    suspend fun saveToken(authToken: String){
        dao.insetToken(Security(id=0, authToken))
    }

    suspend fun deleteToken(security: Security){
        dao.deleteToken(security)
    }

}