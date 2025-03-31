package com.example.quizapp.domain.model

data class Question(
    val id: Int = 0,
    val bankId: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
) {
    fun isValid(): Boolean {
        return questionText.isNotBlank() &&
                options.isNotEmpty() &&
                options.size <= 10 && // Maximum 10 options as per requirements
                correctAnswerIndex in options.indices && // Ensure correct answer index is valid
                options.all { it.isNotBlank() } // Ensure no blank options
    }
}