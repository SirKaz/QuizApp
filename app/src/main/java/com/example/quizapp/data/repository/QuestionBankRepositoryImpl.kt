package com.example.quizapp.data.repository

import com.example.quizapp.data.local.dao.QuestionBankDao
import com.example.quizapp.data.mapper.toDomain
import com.example.quizapp.data.mapper.toEntity
import com.example.quizapp.domain.model.QuestionBank
import com.example.quizapp.domain.repository.QuestionBankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionBankRepositoryImpl(
    private val dao: QuestionBankDao
) : QuestionBankRepository {

    override fun getAllQuestionBanks(): Flow<List<QuestionBank>> {
        return dao.getAllQuestionBanks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getQuestionBankById(id: Int): QuestionBank? {
        return dao.getQuestionBankById(id)?.toDomain()
    }

    override suspend fun insertQuestionBank(questionBank: QuestionBank): Long {
        return dao.insertQuestionBank(questionBank.toEntity())
    }

    override suspend fun updateQuestionBank(questionBank: QuestionBank) {
        dao.updateQuestionBank(questionBank.toEntity())
    }

    override suspend fun deleteQuestionBank(questionBank: QuestionBank) {
        dao.deleteQuestionBank(questionBank.toEntity())
    }

    override suspend fun deleteQuestionBankById(id: Int) {
        dao.deleteQuestionBankById(id)
    }
}