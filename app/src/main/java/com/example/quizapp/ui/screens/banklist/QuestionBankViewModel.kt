package com.example.quizapp.ui.screens.banklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.QuestionBank
import com.example.quizapp.domain.repository.QuestionBankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class QuestionBankListViewModel(
    private val repository: QuestionBankRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuestionBankListState())
    val state: StateFlow<QuestionBankListState> = _state

    init {
        loadQuestionBanks()
    }

    private fun loadQuestionBanks() {
        repository.getAllQuestionBanks()
            .onEach { banks ->
                _state.value = _state.value.copy(
                    questionBanks = banks,
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

    fun deleteQuestionBank(bank: QuestionBank) {
        viewModelScope.launch {
            try {
                repository.deleteQuestionBank(bank)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete question bank: ${e.message}"
                )
            }
        }
    }

    class Factory(
        private val repository: QuestionBankRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuestionBankListViewModel::class.java)) {
                return QuestionBankListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}