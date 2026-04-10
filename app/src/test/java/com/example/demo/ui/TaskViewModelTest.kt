package com.example.demo.ui

import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import com.example.demo.MainDispatcherRule
import com.example.demo.data.local.TaskEntity
import com.example.demo.data.repository.TaskRepository
import com.example.demo.ui.main.TaskViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Rule
import org.junit.Test

class TaskViewModelTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Test
    fun viewModel_initialStates(){
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

    @Test
    fun loadTasks_success_updatesUi() = runTest {
        val repository = mockk<TaskRepository>()
        val fakeTasks = listOf(
            TaskEntity(id = 1, title = "Cocinar"),
            TaskEntity(id = 2, title = "Estudiar")
        )

        coEvery { repository.getTasks() } returns fakeTasks

        val viewModel = TaskViewModel(repository)

        viewModel.loadTasks()
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.getTasks() }

        assertEquals(2, viewModel.uiState.value.tasks.size)
        assertEquals("Cocinar", viewModel.uiState.value.tasks[0].title)
        assertEquals("Estudiar", viewModel.uiState.value.tasks[1].title)
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun loadTasks_error_setsErrorMessage() = runTest {
        val repository = mockk<TaskRepository>()

        coEvery { repository.getTasks() } throws RuntimeException("fallo")

        val viewModel = TaskViewModel(repository)

        viewModel.loadTasks()
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.getTasks() }

        assertTrue(viewModel.uiState.value.tasks.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Error al cargar tareas", viewModel.uiState.value.error)
    }


    @Test
    fun deleteTask_setValid() = runTest {
        val repository = mockk<TaskRepository>(relaxed = true)
        val task = TaskEntity(id = 1, title = "1 - Cocinar")

        coEvery { repository.deleteTask(task) } returns Unit
        coEvery { repository.getTasks() } returns emptyList()

        val viewModel = TaskViewModel(repository)

        viewModel.deleteTask(task)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.deleteTask(task) }
        coVerify(exactly = 1) { repository.getTasks() }

        assertEquals(0, viewModel.uiState.value.tasks.size)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun markTask_setDone() = runTest {
        val repository = mockk<TaskRepository>(relaxed = true)
        val originalTask = TaskEntity(id = 1, title = "1 - Cocinar", isDone = false)

        coEvery { repository.toggleTask(originalTask) } returns Unit
        coEvery { repository.getTasks() } returns listOf(
            TaskEntity(id = 1, title = "1 - Cocinar", isDone = true)
        )

        val viewModel = TaskViewModel(repository)
        viewModel.toggleTask(originalTask)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.toggleTask(originalTask) }
        coVerify(exactly = 1) { repository.getTasks() }

        assertEquals(1, viewModel.uiState.value.tasks.size)
        assertEquals(true, viewModel.uiState.value.tasks.first().isDone)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun markTask_setUndone() = runTest {
        val repository = mockk<TaskRepository>(relaxed = true)
        val doneTask = TaskEntity(id = 1, title = "1 - Cocinar", isDone = true)

        coEvery { repository.toggleTask(doneTask) } returns Unit
        coEvery { repository.getTasks() } returns listOf(
            TaskEntity(id = 1, title = "1 - Cocinar", isDone = false)
        )

        val viewModel = TaskViewModel(repository)
        viewModel.toggleTask(doneTask)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.toggleTask(doneTask) }
        coVerify(exactly = 1) { repository.getTasks() }

        assertEquals(1, viewModel.uiState.value.tasks.size)
        assertEquals(false, viewModel.uiState.value.tasks.first().isDone)
        assertNull(viewModel.uiState.value.error)
    }}