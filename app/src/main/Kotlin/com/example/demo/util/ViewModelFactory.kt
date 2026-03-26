package com.example.demo.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo.data.repository.TaskRepository
import com.example.demo.ui.main.TaskViewModel

class ViewModelFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}