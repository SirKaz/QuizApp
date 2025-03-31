package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.QuestionBank
import kotlinx.coroutines.flow.Flow

interface QuestionBankRepository {
    fun getAllQuestionBanks(): Flow<List<QuestionBank>>

    suspend fun getQuestionBankById(id: Int): QuestionBank?

    suspend fun insertQuestionBank(questionBank: QuestionBank): Long

    suspend fun updateQuestionBank(questionBank: QuestionBank)

    suspend fun deleteQuestionBank(questionBank: QuestionBank)

    suspend fun deleteQuestionBankById(id: Int)
}