package com.example.quizapp

import com.example.quizapp.domain.model.QuestionBank
import com.example.quizapp.domain.repository.QuestionBankRepository
import com.example.quizapp.ui.screens.banklist.QuestionBankListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionBankListViewModelTest {
    private lateinit var viewModel: QuestionBankListViewModel
    private lateinit var repository: QuestionBankRepository
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
    fun `initial state should be empty list with loading`() = runTest {
        whenever(repository.getAllQuestionBanks())
            .thenReturn(flowOf(emptyList()))

        viewModel = QuestionBankListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.questionBanks.isEmpty())
        assertFalse(viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `loading question banks should update state`() = runTest {
        val banks = listOf(
            QuestionBank(1, "Bank 1"),
            QuestionBank(2, "Bank 2")
        )
        whenever(repository.getAllQuestionBanks())
            .thenReturn(flowOf(banks))

        viewModel = QuestionBankListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, viewModel.state.value.questionBanks.size)
        assertEquals("Bank 1", viewModel.state.value.questionBanks[0].name)
        assertEquals("Bank 2", viewModel.state.value.questionBanks[1].name)
    }

    @Test
    fun `deleting question bank should call repository`() = runTest {
        whenever(repository.getAllQuestionBanks())
            .thenReturn(flowOf(emptyList()))

        viewModel = QuestionBankListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        val bank = QuestionBank(1, "Test Bank")
        viewModel.deleteQuestionBank(bank)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).deleteQuestionBank(bank)
    }

    @Test
    fun `repository error should update error state`() = runTest {
        // Create a flow that emits an error
        val errorFlow: Flow<List<QuestionBank>> = flow {
            throw RuntimeException("Test error")
        }

        whenever(repository.getAllQuestionBanks())
            .thenReturn(errorFlow)

        viewModel = QuestionBankListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.state.value.error)
        assertTrue(viewModel.state.value.error?.contains("Test error") == true)
    }

    @Test
    fun `empty bank list should show empty state`() = runTest {
        whenever(repository.getAllQuestionBanks())
            .thenReturn(flowOf(emptyList()))

        viewModel = QuestionBankListViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.questionBanks.isEmpty())
        assertFalse(viewModel.state.value.isLoading)
    }
}