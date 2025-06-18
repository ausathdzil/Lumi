package com.example.lumi.ui

import androidx.lifecycle.ViewModel
import com.example.lumi.data.StatusType
import com.example.lumi.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LumiViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LumiUiState())
    val uiState: StateFlow<LumiUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = LumiUiState(
            tasks = listOf(
                Task(1, "Create a project plan", StatusType.TODO),
                Task(2, "Meet with the design team", StatusType.TODO),
                Task(3, "Finalize the first draft", StatusType.COMPLETED)
            )
        )
    }

    fun addTask(title: String) {
        val newTask = Task(
            id = _uiState.value.tasks.size + 1,
            title = title,
            status = StatusType.TODO
        )
        _uiState.update { currentState ->
            currentState.copy(tasks = currentState.tasks + newTask)
        }
    }

    fun updateTask(taskId: Int, newStatus: StatusType) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.map { task ->
                if (task.id == taskId) {
                    task.copy(status = newStatus)
                } else {
                    task
                }
            }
            currentState.copy(tasks = updatedTasks)
        }
    }
}
