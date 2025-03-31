package com.example.quizapp

import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.repository.QuestionRepository
import com.example.quizapp.ui.screens.quiz.QuizEvent
import com.example.quizapp.ui.screens.quiz.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {
    private lateinit var viewModel: QuizViewModel
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
    fun `initial state should be empty`() = runTest {
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(emptyList()))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(emptyList<Question>(), viewModel.state.value.questions)
        assertFalse(viewModel.state.value.isFinished)
        assertEquals(0, viewModel.state.value.score)
        assertNull(viewModel.state.value.selectedAnswerIndex)
    }

    @Test
    fun `loading questions should update state`() = runTest {
        val testQuestions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0),
            Question(2, 1, "Q2", listOf("X", "Y"), 1)
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(testQuestions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(testQuestions, viewModel.state.value.questions)
        assertEquals(0, viewModel.state.value.currentQuestionIndex)
        assertFalse(viewModel.state.value.isFinished)
    }

    @Test
    fun `selecting correct answer should increase score`() = runTest {
        val testQuestions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0)
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(testQuestions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(QuizEvent.AnswerSelected(0)) // Select correct answer
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onEvent(QuizEvent.NextQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.score)
        assertTrue(viewModel.state.value.isFinished)
    }

    @Test
    fun `selecting wrong answer should not increase score`() = runTest {
        val testQuestions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0)
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(testQuestions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(QuizEvent.AnswerSelected(1)) // Select wrong answer
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onEvent(QuizEvent.NextQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.state.value.score)
        assertTrue(viewModel.state.value.isFinished)
    }

    @Test
    fun `restarting quiz should reset state`() = runTest {
        val testQuestions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0)
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(testQuestions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Complete the quiz first
        viewModel.onEvent(QuizEvent.AnswerSelected(0))
        viewModel.onEvent(QuizEvent.NextQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        // Now restart
        viewModel.onEvent(QuizEvent.RestartQuiz)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.state.value.score)
        assertFalse(viewModel.state.value.isFinished)
        assertNull(viewModel.state.value.selectedAnswerIndex)
    }

    @Test
    fun `skipping question should move to next question`() = runTest {
        val questions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0),
            Question(2, 1, "Q2", listOf("X", "Y"), 1)
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(questions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.state.value.currentQuestionIndex)
        viewModel.onEvent(QuizEvent.SkipQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.state.value.currentQuestionIndex)
        assertNull(viewModel.state.value.selectedAnswerIndex)
    }

    @Test
    fun `skipping last question should finish quiz`() = runTest {
        val questions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0)
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(questions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(QuizEvent.SkipQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.isFinished)
        assertEquals(0, viewModel.state.value.score)
    }

    @Test
    fun `skipping after answer selection should not count points`() = runTest {
        val questions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0),
            Question(2, 1, "Q2", listOf("X", "Y"), 1)
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(questions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Select correct answer but then skip
        viewModel.onEvent(QuizEvent.AnswerSelected(0))
        viewModel.onEvent(QuizEvent.SkipQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.state.value.score)
        assertEquals(1, viewModel.state.value.currentQuestionIndex)
    }

    @Test
    fun `skipping all questions should result in zero score`() = runTest {
        val questions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0),
            Question(2, 1, "Q2", listOf("X", "Y"), 1),
            Question(3, 1, "Q3", listOf("P", "Q"), 0)
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(questions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Skip all questions
        repeat(questions.size) {
            viewModel.onEvent(QuizEvent.SkipQuestion)
            testDispatcher.scheduler.advanceUntilIdle()
        }

        assertTrue(viewModel.state.value.isFinished)
        assertEquals(0, viewModel.state.value.score)
    }

    @Test
    fun `mix of skipping and answering should calculate score correctly`() = runTest {
        val questions = listOf(
            Question(1, 1, "Q1", listOf("A", "B"), 0), // Will skip
            Question(2, 1, "Q2", listOf("X", "Y"), 1), // Will answer correctly
            Question(3, 1, "Q3", listOf("P", "Q"), 0)  // Will answer incorrectly
        )
        whenever(repository.getRandomQuestionsForBank(1))
            .thenReturn(flowOf(questions))

        viewModel = QuizViewModel(repository, 1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Skip first question
        viewModel.onEvent(QuizEvent.SkipQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        // Answer second question correctly
        viewModel.onEvent(QuizEvent.AnswerSelected(1))
        viewModel.onEvent(QuizEvent.NextQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        // Answer third question incorrectly
        viewModel.onEvent(QuizEvent.AnswerSelected(1))
        viewModel.onEvent(QuizEvent.NextQuestion)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.isFinished)
        assertEquals(1, viewModel.state.value.score) // Only one correct answer
    }
}