package com.example.lumi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class User(
    @PrimaryKey
    val id: Int = 0,
    val name: String
)
