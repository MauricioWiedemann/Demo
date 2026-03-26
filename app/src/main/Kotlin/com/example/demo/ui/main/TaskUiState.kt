package com.example.demo.ui.main

import com.example.demo.data.local.TaskEntity

data class TaskUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)