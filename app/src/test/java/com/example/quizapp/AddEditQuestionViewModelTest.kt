package com.example.quizapp

import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.repository.QuestionRepository
import com.example.quizapp.ui.screens.addquestion.AddEditQuestionEvent
import com.example.quizapp.ui.screens.addquestion.AddEditQuestionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditQuestionViewModelTest {
    private lateinit var viewModel: AddEditQuestionViewModel
    private lateinit var repository: QuestionRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty for new question`() = runTest {
        viewModel = AddEditQuestionViewModel(repository, bankId = 1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("", viewModel.state.value.questionText)
        assertEquals(listOf(""), viewModel.state.value.options)
        assertEquals(0, viewModel.state.value.correctAnswerIndex)
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `loading existing question should update state`() = runTest {
        val question = Question(
            id = 1,
            bankId = 1,
            questionText = "Test Question",
            options = listOf("A", "B", "C"),
            correctAnswerIndex = 1
        )
        whenever(repository.getQuestionById(1)).thenReturn(question)

        viewModel = AddEditQuestionViewModel(repository, bankId = 1, questionId = 1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Test Question", viewModel.state.value.questionText)
        assertEquals(listOf("A", "B", "C"), viewModel.state.value.options)
        assertEquals(1, viewModel.state.value.correctAnswerIndex)
    }

    @Test
    fun `adding option should update options list`() = runTest {
        viewModel = AddEditQuestionViewModel(repository, bankId = 1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AddEditQuestionEvent.AddOption)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.state.value.options.size)
    }

    @Test
    fun `removing option should update options list`() = runTest {
        viewModel = AddEditQuestionViewModel(repository, bankId = 1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AddEditQuestionEvent.AddOption)
        viewModel.onEvent(AddEditQuestionEvent.RemoveOption(0))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.options.size)
    }

    @Test
    fun `changing question text should update state`() = runTest {
        viewModel = AddEditQuestionViewModel(repository, bankId = 1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AddEditQuestionEvent.QuestionTextChanged("New Question"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("New Question", viewModel.state.value.questionText)
    }

    @Test
    fun `changing option text should update state`() = runTest {
        viewModel = AddEditQuestionViewModel(repository, bankId = 1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AddEditQuestionEvent.OptionChanged(0, "New Option"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("New Option", viewModel.state.value.options[0])
    }

    @Test
    fun `saving valid question should succeed`() = runTest {
        viewModel = AddEditQuestionViewModel(repository, bankId = 1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(AddEditQuestionEvent.QuestionTextChanged("Valid Question"))
        viewModel.onEvent(AddEditQuestionEvent.OptionChanged(0, "Option A"))
        viewModel.onEvent(AddEditQuestionEvent.SaveQuestion)
    }
}