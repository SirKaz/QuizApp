package com.example.quizapp.data.local.dao

import androidx.room.*
import com.example.quizapp.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE bankId = :bankId")
    fun getQuestionsForBank(bankId: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE bankId = :bankId ORDER BY RANDOM()")
    fun getRandomQuestionsForBank(bankId: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getQuestionById(id: Int): QuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestionById(id: Int)
}