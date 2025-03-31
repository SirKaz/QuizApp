package com.example.quizapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizapp.data.local.dao.QuestionBankDao
import com.example.quizapp.data.local.dao.QuestionDao
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.local.entity.QuestionBankEntity
import com.example.quizapp.data.local.entity.QuestionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class QuizDatabaseTest {
    private lateinit var questionDao: QuestionDao
    private lateinit var questionBankDao: QuestionBankDao
    private lateinit var db: QuizDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, QuizDatabase::class.java
        ).allowMainThreadQueries() // For testing only
            .build()
        questionDao = db.questionDao()
        questionBankDao = db.questionBankDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // Question Bank Tests
    @Test
    fun insertAndReadQuestionBank() = runBlocking {
        val questionBank = QuestionBankEntity(name = "Test Bank")
        val id = questionBankDao.insertQuestionBank(questionBank)
        val loaded = questionBankDao.getQuestionBankById(id.toInt())
        assertNotNull(loaded)
        assertEquals(questionBank.name, loaded?.name)
    }

    @Test
    fun updateQuestionBank() = runBlocking {
        // Insert initial bank
        val questionBank = QuestionBankEntity(name = "Original Name")
        val id = questionBankDao.insertQuestionBank(questionBank)

        // Update the bank
        val updatedBank = QuestionBankEntity(id = id.toInt(), name = "Updated Name")
        questionBankDao.updateQuestionBank(updatedBank)

        // Verify update
        val loaded = questionBankDao.getQuestionBankById(id.toInt())
        assertEquals("Updated Name", loaded?.name)
    }

    @Test
    fun deleteQuestionBank() = runBlocking {
        // Insert and then delete
        val questionBank = QuestionBankEntity(name = "To Delete")
        val id = questionBankDao.insertQuestionBank(questionBank)
        val bankWithId = questionBank.copy(id = id.toInt())
        questionBankDao.deleteQuestionBank(bankWithId)

        // Verify deletion
        val loaded = questionBankDao.getQuestionBankById(id.toInt())
        assertNull(loaded)
    }

    @Test
    fun getAllQuestionBanks() = runBlocking {
        // Insert multiple banks
        val banks = listOf(
            QuestionBankEntity(name = "Bank 1"),
            QuestionBankEntity(name = "Bank 2"),
            QuestionBankEntity(name = "Bank 3")
        )
        banks.forEach { questionBankDao.insertQuestionBank(it) }

        // Get all banks and verify
        val loadedBanks = questionBankDao.getAllQuestionBanks().first()
        assertEquals(3, loadedBanks.size)
    }

    // Question Tests
    @Test
    fun insertAndReadQuestion() = runBlocking {
        // First create a bank
        val bankId = questionBankDao.insertQuestionBank(
            QuestionBankEntity(name = "Test Bank")
        ).toInt()

        // Create and insert a question
        val question = QuestionEntity(
            bankId = bankId,
            questionText = "Test Question",
            options = listOf("A", "B", "C"),
            correctAnswerIndex = 0
        )
        questionDao.insertQuestion(question)

        // Verify question was saved
        val questions = questionDao.getQuestionsForBank(bankId).first()
        assertEquals(1, questions.size)
        assertEquals(question.questionText, questions[0].questionText)
        assertEquals(question.options, questions[0].options)
        assertEquals(question.correctAnswerIndex, questions[0].correctAnswerIndex)
    }

    @Test
    fun getRandomQuestionsForBank() = runBlocking {
        // Create bank
        val bankId = questionBankDao.insertQuestionBank(
            QuestionBankEntity(name = "Test Bank")
        ).toInt()

        // Insert multiple questions
        val questions = List(5) { index ->
            QuestionEntity(
                bankId = bankId,
                questionText = "Question $index",
                options = listOf("A", "B", "C"),
                correctAnswerIndex = 0
            )
        }
        questions.forEach { questionDao.insertQuestion(it) }

        // Get random questions multiple times
        val firstSet = questionDao.getRandomQuestionsForBank(bankId).first()
        val secondSet = questionDao.getRandomQuestionsForBank(bankId).first()

        // Verify we get all questions in potentially different orders
        assertEquals(5, firstSet.size)
        assertEquals(5, secondSet.size)
    }

    @Test
    fun cascadingDelete() = runBlocking {
        // Create bank and questions
        val bankId = questionBankDao.insertQuestionBank(
            QuestionBankEntity(name = "Test Bank")
        ).toInt()

        repeat(3) { index ->
            questionDao.insertQuestion(
                QuestionEntity(
                    bankId = bankId,
                    questionText = "Question $index",
                    options = listOf("A", "B", "C"),
                    correctAnswerIndex = 0
                )
            )
        }

        // Delete bank
        questionBankDao.deleteQuestionBankById(bankId)

        // Verify all questions are deleted
        val remainingQuestions = questionDao.getQuestionsForBank(bankId).first()
        assertTrue(remainingQuestions.isEmpty())

        // Verify bank is deleted
        val deletedBank = questionBankDao.getQuestionBankById(bankId)
        assertNull(deletedBank)
    }

    @Test
    fun updateQuestion() = runBlocking {
        val bankId = questionBankDao.insertQuestionBank(
            QuestionBankEntity(name = "Test Bank")
        ).toInt()

        // Create and insert initial question
        val initialQuestion = QuestionEntity(
            bankId = bankId,
            questionText = "Original Question",
            options = listOf("A", "B", "C"),
            correctAnswerIndex = 0
        )
        questionDao.insertQuestion(initialQuestion)

        // Get the inserted question to get its ID
        val insertedQuestion = questionDao.getQuestionsForBank(bankId).first().first()

        // Create updated version with the same ID
        val updatedQuestion = QuestionEntity(
            id = insertedQuestion.id,  // Use the same ID
            bankId = bankId,
            questionText = "Updated Question",
            options = listOf("X", "Y", "Z"),
            correctAnswerIndex = 1
        )

        // Perform update
        questionDao.updateQuestion(updatedQuestion)

        // Verify update
        val questions = questionDao.getQuestionsForBank(bankId).first()
        assertEquals(1, questions.size)

        val loadedQuestion = questions.first()
        assertEquals(updatedQuestion.id, loadedQuestion.id)
        assertEquals(updatedQuestion.questionText, loadedQuestion.questionText)
        assertEquals(updatedQuestion.options, loadedQuestion.options)
        assertEquals(updatedQuestion.correctAnswerIndex, loadedQuestion.correctAnswerIndex)
        assertEquals(updatedQuestion.bankId, loadedQuestion.bankId)
    }
}