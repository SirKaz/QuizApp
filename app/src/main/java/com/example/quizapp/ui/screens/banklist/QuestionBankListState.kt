package com.example.quizapp.ui.screens.banklist

import com.example.quizapp.domain.model.QuestionBank

data class QuestionBankListState(
    val questionBanks: List<QuestionBank> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)