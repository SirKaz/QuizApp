package com.example.quizapp.ui.screens.quiz

import com.example.quizapp.domain.model.Question

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val score: Int = 0,
    val isFinished: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val progress: Float
        get() = if (questions.isEmpty()) 0f else (currentQuestionIndex + 1).toFloat() / questions.size

    val totalQuestions: Int
        get() = questions.size
}

sealed class QuizEvent {
    data class AnswerSelected(val index: Int) : QuizEvent()
    object NextQuestion : QuizEvent()
    object StartQuiz : QuizEvent()
    object RestartQuiz : QuizEvent()
    object SkipQuestion : QuizEvent()
}