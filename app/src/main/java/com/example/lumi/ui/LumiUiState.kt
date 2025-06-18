package com.example.lumi.ui

import com.example.lumi.data.Task

data class LumiUiState(
    val tasks: List<Task> = emptyList()
)
