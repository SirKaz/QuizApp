package com.example.quizapp.ui.screens.questionbank

import com.example.quizapp.domain.model.Question

data class QuestionBankState(
    val bankId: Int = 0,
    val questions: List<Question> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)