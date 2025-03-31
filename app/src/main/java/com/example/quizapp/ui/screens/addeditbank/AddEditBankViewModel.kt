package com.example.quizapp.ui.screens.addeditbank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.QuestionBank
import com.example.quizapp.domain.repository.QuestionBankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddEditBankViewModel(
    private val repository: QuestionBankRepository,
    private val bankId: Int? = null
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditBankState())
    val state: StateFlow<AddEditBankState> = _state

    init {
        bankId?.let { loadQuestionBank(it) }
    }

    private fun loadQuestionBank(id: Int) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val bank = repository.getQuestionBankById(id)
                bank?.let {
                    _state.value = _state.value.copy(
                        id = it.id,
                        name = it.name,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load question bank: ${e.message}"
                )
            }
        }
    }

    fun onEvent(event: AddEditBankEvent) {
        when (event) {
            is AddEditBankEvent.NameChanged -> {
                _state.value = _state.value.copy(name = event.name)
            }
            is AddEditBankEvent.SaveBank -> saveBank()
        }
    }

    private fun saveBank() {
        val currentState = _state.value

        val bank = QuestionBank(
            id = currentState.id,
            name = currentState.name
        )

        // Validate
        if (!bank.isValid()) {
            _state.value = currentState.copy(
                error = "Please enter a valid name"
            )
            return
        }

        viewModelScope.launch {
            try {
                _state.value = currentState.copy(isLoading = true)
                repository.insertQuestionBank(bank)
                _state.value = currentState.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    error = "Failed to save question bank: ${e.message}"
                )
            }
        }
    }

    class Factory(
        private val repository: QuestionBankRepository,
        private val bankId: Int? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddEditBankViewModel::class.java)) {
                return AddEditBankViewModel(repository, bankId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}