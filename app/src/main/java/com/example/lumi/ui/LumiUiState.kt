package com.example.lumi.ui

import com.example.lumi.data.Task
import com.example.lumi.data.User

data class LumiUiState(
    val tasks: List<Task> = emptyList(),
    val user: User = User(name = "Guest")
)
