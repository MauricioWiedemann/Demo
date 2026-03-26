package com.example.demo.ui

import com.example.demo.data.repository.TaskRepository
import com.example.demo.ui.main.TaskViewModel
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class TaskViewModelTest {

    @Test
    fun addTask_setError(){
        val repository = mockk<TaskRepository>()
        val viewModel = TaskViewModel(repository)

        viewModel.addTask(" ")
        assertEquals("La tarea no puede estar vacía", viewModel.uiState.value.error)
    }

    @Test
    fun addTask_setCorrect(){
        val repository = mockk<TaskRepository>()
        val viewModel = TaskViewModel(repository)

        viewModel.addTask("1 - Cocinar")
        assertEquals("La tarea no puede estar vacía", viewModel.uiState.value.error)
    }

}