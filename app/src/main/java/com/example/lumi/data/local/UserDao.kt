package com.example.lumi.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.lumi.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM user_profile WHERE id = 0")
    fun getUser(): Flow<User?>
}
