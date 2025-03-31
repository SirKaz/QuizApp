package com.example.quizapp.domain.model

data class QuestionBank(
    val id: Int = 0,
    val name: String
) {
    fun isValid(): Boolean {
        return name.isNotBlank() && name.length <= 50 // Add reasonable length limit
    }

    companion object {
        fun createEmpty(): QuestionBank {
            return QuestionBank(
                id = 0,
                name = ""
            )
        }
    }
}