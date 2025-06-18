package com.example.lumi.data

enum class StatusType {
    TODO, COMPLETED
}

data class Task(
    val id: Int,
    val title: String,
    val status: StatusType,
)
