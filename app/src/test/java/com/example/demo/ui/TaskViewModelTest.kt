package com.example.demo.ui

import com.example.demo.MainDispatcherRule
import com.example.demo.data.local.TaskEntity
import com.example.demo.data.repository.TaskRepository
import com.example.demo.ui.main.TaskViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class TaskViewModelTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Test
    fun viewModel_initialStates() = runTest(){
        val repository = mockk<TaskRepository>(relaxed = true)
        val viewModel = TaskViewModel(repository)

        assertTrue(viewModel.uiState.value.tasks.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun addTask_setError(){
        val repository = mockk<TaskRepository>(relaxed = true)
        val viewModel = TaskViewModel(repository)

        viewModel.addTask(" ")
        assertEquals("La tarea no puede estar vacía", viewModel.uiState.value.error)
    }

    @Test
    fun addTaskAndReload_setValid() = runTest {
        val repository = mockk<TaskRepository>(relaxed = true)

        coEvery { repository.addTask("1 - Cocinar") } returns Unit
        coEvery { repository.getTasks() } returns listOf(
            TaskEntity(id = 1, title = "1 - Cocinar")
        )

        val viewModel = TaskViewModel(repository)

        viewModel.addTask("1 - Cocinar")
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.addTask("1 - Cocinar") }
        coVerify(exactly = 1) { repository.getTasks() }

        assertEquals(1, viewModel.uiState.value.tasks.size)
        assertEquals("1 - Cocinar", viewModel.uiState.value.tasks.first().title)
        assertNull(viewModel.uiState.value.error)
    }

}