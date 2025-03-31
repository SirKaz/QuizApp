package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    fun getQuestionsForBank(bankId: Int): Flow<List<Question>>

    fun getRandomQuestionsForBank(bankId: Int): Flow<List<Question>>

    suspend fun getQuestionById(id: Int): Question?

    suspend fun insertQuestion(question: Question)

    suspend fun updateQuestion(question: Question)

    suspend fun deleteQuestion(question: Question)

    suspend fun deleteQuestionById(id: Int)
}