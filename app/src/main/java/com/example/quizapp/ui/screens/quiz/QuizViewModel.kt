package com.example.quizapp.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuestionRepository,
    private val bankId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                // Get random questions for specific bank
                val randomQuestions = repository.getRandomQuestionsForBank(bankId).first()

                if (randomQuestions.isEmpty()) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "No questions available in this bank. Please add some questions first."
                    )
                    return@launch
                }

                _state.value = _state.value.copy(
                    questions = randomQuestions,
                    currentQuestionIndex = 0,
                    selectedAnswerIndex = null,
                    score = 0,
                    isFinished = false,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load questions: ${e.message}"
                )
            }
        }
    }

    fun onEvent(event: QuizEvent) {
        when (event) {
            is QuizEvent.AnswerSelected -> {
                if (!_state.value.isFinished) {
                    _state.value = _state.value.copy(
                        selectedAnswerIndex = event.index
                    )
                }
            }

            QuizEvent.NextQuestion -> {
                val currentState = _state.value
                val currentQuestion = currentState.currentQuestion

                if (currentQuestion != null) {
                    val isCorrect = currentState.selectedAnswerIndex == currentQuestion.correctAnswerIndex
                    val newScore = if (isCorrect) currentState.score + 1 else currentState.score

                    if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                        // Move to next question
                        _state.value = currentState.copy(
                            currentQuestionIndex = currentState.currentQuestionIndex + 1,
                            selectedAnswerIndex = null,
                            score = newScore
                        )
                    } else {
                        // Quiz is finished
                        _state.value = currentState.copy(
                            isFinished = true,
                            score = newScore
                        )
                    }
                }
            }

            QuizEvent.SkipQuestion -> {
                val currentState = _state.value
                if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                    // Move to next question
                    _state.value = currentState.copy(
                        currentQuestionIndex = currentState.currentQuestionIndex + 1,
                        selectedAnswerIndex = null
                    )
                } else {
                    // Quiz is finished
                    _state.value = currentState.copy(
                        isFinished = true
                    )
                }
            }

            QuizEvent.StartQuiz -> {
                loadQuestions()
            }

            QuizEvent.RestartQuiz -> {
                _state.value = QuizState()
                loadQuestions()
            }
        }
    }

    class Factory(
        private val repository: QuestionRepository,
        private val bankId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
                return QuizViewModel(repository, bankId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}