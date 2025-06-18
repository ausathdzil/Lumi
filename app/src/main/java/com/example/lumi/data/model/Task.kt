package com.example.lumi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StatusType {
    TODO, COMPLETED
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    val id: String,
    val title: String,
    val status: StatusType,
)
