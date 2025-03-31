package com.example.quizapp

import com.example.quizapp.domain.model.QuestionBank
import org.junit.Test
import org.junit.Assert.*

class QuestionBankTest {
    @Test
    fun `question bank with valid name should be valid`() {
        val bank = QuestionBank(
            id = 1,
            name = "Norse Mythology"
        )
        assertTrue(bank.isValid())
    }

    @Test
    fun `question bank with empty name should be invalid`() {
        val bank = QuestionBank(
            id = 1,
            name = ""
        )
        assertFalse(bank.isValid())
    }

    @Test
    fun `question bank with blank name should be invalid`() {
        val bank = QuestionBank(
            id = 1,
            name = "   "
        )
        assertFalse(bank.isValid())
    }

    @Test
    fun `question bank with name exceeding length limit should be invalid`() {
        val bank = QuestionBank(
            id = 1,
            name = "A".repeat(51) // 51 characters, exceeding 50 char limit
        )
        assertFalse(bank.isValid())
    }
}