package com.example.demo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.data.local.TaskEntity
import com.example.demo.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val tasks = repository.getTasks()
                _uiState.value = TaskUiState(
                    tasks = tasks,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = TaskUiState(
                    tasks = emptyList(),
                    isLoading = false,
                    error = "Error al cargar tareas"
                )
            }
        }
    }

    fun addTask(title: String) {
        if (title.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "La tarea no puede estar vacía")
            return
        }

        viewModelScope.launch {
            repository.addTask(title.trim())
            loadTasks()
        }
    }

    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTask(task)
            loadTasks()
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
            loadTasks()
        }
    }
}