package com.example.lumi.ui

import com.example.lumi.data.model.Task
import com.example.lumi.data.model.User

data class LumiUiState(
    val tasks: List<Task> = emptyList(),
    val user: User = User(name = "Guest")
)
