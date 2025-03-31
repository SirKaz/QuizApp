package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.QuestionDao
import com.example.quizapp.data.mapper.toDomain
import com.example.quizapp.data.mapper.toEntity
import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionRepositoryImpl(
    private val dao: QuestionDao
) : QuestionRepository {

    override fun getQuestionsForBank(bankId: Int): Flow<List<Question>> {
        return dao.getQuestionsForBank(bankId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getRandomQuestionsForBank(bankId: Int): Flow<List<Question>> {
        return dao.getRandomQuestionsForBank(bankId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getQuestionById(id: Int): Question? {
        return dao.getQuestionById(id)?.toDomain()
    }

    override suspend fun insertQuestion(question: Question) {
        dao.insertQuestion(question.toEntity())
    }

    override suspend fun updateQuestion(question: Question) {
        dao.updateQuestion(question.toEntity())
    }

    override suspend fun deleteQuestion(question: Question) {
        dao.deleteQuestion(question.toEntity())
    }

    override suspend fun deleteQuestionById(id: Int) {
        dao.deleteQuestionById(id)
    }
}