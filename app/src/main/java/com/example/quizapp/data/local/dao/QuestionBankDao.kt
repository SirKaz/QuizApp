package com.example.quizapp.data.local.dao

import androidx.room.*
import com.example.quizapp.data.local.entity.QuestionBankEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionBankDao {
    @Query("SELECT * FROM question_banks")
    fun getAllQuestionBanks(): Flow<List<QuestionBankEntity>>

    @Query("SELECT * FROM question_banks WHERE id = :id")
    suspend fun getQuestionBankById(id: Int): QuestionBankEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionBank(questionBank: QuestionBankEntity): Long

    @Update
    suspend fun updateQuestionBank(questionBank: QuestionBankEntity)

    @Delete
    suspend fun deleteQuestionBank(questionBank: QuestionBankEntity)

    @Query("DELETE FROM question_banks WHERE id = :id")
    suspend fun deleteQuestionBankById(id: Int)
}