package com.example.quizapp.ui.screens.questionbank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class QuestionBankViewModel(
    private val repository: QuestionRepository,
    private val bankId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(QuestionBankState())
    val state: StateFlow<QuestionBankState> = _state

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        repository.getQuestionsForBank(bankId)
            .onEach { questions ->
                _state.value = _state.value.copy(
                    questions = questions,
                    isLoading = false,
                    error = null
                )
            }
            .catch { error ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
            .launchIn(viewModelScope)
    }

    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            try {
                repository.deleteQuestion(question)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete question: ${e.message}"
                )
            }
        }
    }

    class Factory(
        private val repository: QuestionRepository,
        private val bankId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuestionBankViewModel::class.java)) {
                return QuestionBankViewModel(repository, bankId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}