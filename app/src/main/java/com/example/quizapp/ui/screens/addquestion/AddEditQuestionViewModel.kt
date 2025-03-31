package com.example.quizapp.ui.screens.addquestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddEditQuestionViewModel(
    private val repository: QuestionRepository,
    private val bankId: Int,
    private val questionId: Int? = null
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditQuestionState())
    val state: StateFlow<AddEditQuestionState> = _state

    init {
        questionId?.let { loadQuestion(it) }
    }

    private fun loadQuestion(id: Int) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val question = repository.getQuestionById(id)
                question?.let {
                    _state.value = _state.value.copy(
                        id = it.id,
                        questionText = it.questionText,
                        options = it.options,
                        correctAnswerIndex = it.correctAnswerIndex,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load question: ${e.message}"
                )
            }
        }
    }

    fun onEvent(event: AddEditQuestionEvent) {
        try {
            when (event) {
                is AddEditQuestionEvent.QuestionTextChanged -> {
                    _state.value = _state.value.copy(questionText = event.text)
                }
                is AddEditQuestionEvent.OptionChanged -> {
                    val newOptions = _state.value.options.toMutableList()
                    if (event.index < newOptions.size) {
                        newOptions[event.index] = event.text
                        _state.value = _state.value.copy(options = newOptions)
                    }
                }
                is AddEditQuestionEvent.CorrectAnswerChanged -> {
                    if (event.index < _state.value.options.size) {
                        _state.value = _state.value.copy(correctAnswerIndex = event.index)
                    }
                }
                AddEditQuestionEvent.AddOption -> {
                    if (_state.value.options.size < 10) {
                        _state.value = _state.value.copy(
                            options = _state.value.options + ""
                        )
                    }
                }
                is AddEditQuestionEvent.RemoveOption -> {
                    if (_state.value.options.size > 1) {
                        val newOptions = _state.value.options.toMutableList()
                        newOptions.removeAt(event.index)
                        _state.value = _state.value.copy(
                            options = newOptions,
                            correctAnswerIndex = if (_state.value.correctAnswerIndex >= newOptions.size) {
                                0
                            } else {
                                _state.value.correctAnswerIndex
                            }
                        )
                    }
                }
                AddEditQuestionEvent.SaveQuestion -> saveQuestion()
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                error = "An error occurred: ${e.message}"
            )
        }
    }

    private fun saveQuestion() {
        viewModelScope.launch {
            try {
                val currentState = _state.value

                val question = Question(
                    id = currentState.id,
                    bankId = bankId,  // Use the bankId from constructor
                    questionText = currentState.questionText,
                    options = currentState.options,
                    correctAnswerIndex = currentState.correctAnswerIndex
                )

                if (!question.isValid()) {
                    _state.value = currentState.copy(
                        error = "Please ensure all fields are filled correctly"
                    )
                    return@launch
                }

                _state.value = currentState.copy(isLoading = true)
                repository.insertQuestion(question)
                _state.value = currentState.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to save question: ${e.message}"
                )
            }
        }
    }

    class Factory(
        private val repository: QuestionRepository,
        private val bankId: Int,
        private val questionId: Int? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddEditQuestionViewModel::class.java)) {
                return AddEditQuestionViewModel(repository, bankId, questionId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}