package com.example.quizapp

import com.example.quizapp.domain.model.Question
import org.junit.Test
import org.junit.Assert.*

class QuestionTest {
    @Test
    fun `question with valid data should be valid`() {
        val question = Question(
            id = 1,
            bankId = 1,
            questionText = "What is the capital of Norway?",
            options = listOf("Oslo", "Bergen", "Tromso"),
            correctAnswerIndex = 0
        )
        assertTrue(question.isValid())
    }

    @Test
    fun `question with empty text should be invalid`() {
        val question = Question(
            id = 1,
            bankId = 1,
            questionText = "",
            options = listOf("Oslo", "Bergen", "Tromso"),
            correctAnswerIndex = 0
        )
        assertFalse(question.isValid())
    }

    @Test
    fun `question with empty options should be invalid`() {
        val question = Question(
            id = 1,
            bankId = 1,
            questionText = "What is the capital of Norway?",
            options = emptyList(),
            correctAnswerIndex = 0
        )
        assertFalse(question.isValid())
    }

    @Test
    fun `question with more than 10 options should be invalid`() {
        val question = Question(
            id = 1,
            bankId = 1,
            questionText = "Test question",
            options = List(11) { "Option $it" },
            correctAnswerIndex = 0
        )
        assertFalse(question.isValid())
    }

    @Test
    fun `question with invalid correct answer index should be invalid`() {
        val question = Question(
            id = 1,
            bankId = 1,
            questionText = "Test question",
            options = listOf("Option 1", "Option 2"),
            correctAnswerIndex = 2
        )
        assertFalse(question.isValid())
    }

    @Test
    fun `question with blank options should be invalid`() {
        val question = Question(
            id = 1,
            bankId = 1,
            questionText = "Test question",
            options = listOf("Option 1", "", "Option 3"),
            correctAnswerIndex = 0
        )
        assertFalse(question.isValid())
    }
}