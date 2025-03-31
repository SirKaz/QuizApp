package com.example.quizapp.data.mapper

import com.example.quizapp.data.local.entity.QuestionBankEntity
import com.example.quizapp.data.local.entity.QuestionEntity
import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.model.QuestionBank

// Question Mappers
fun QuestionEntity.toDomain(): Question {
    return Question(
        id = id,
        bankId = bankId,
        questionText = questionText,
        options = options,
        correctAnswerIndex = correctAnswerIndex
    )
}

fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = id,
        bankId = bankId,
        questionText = questionText,
        options = options,
        correctAnswerIndex = correctAnswerIndex
    )
}

// QuestionBank Mappers
fun QuestionBankEntity.toDomain(): QuestionBank {
    return QuestionBank(
        id = id,
        name = name
    )
}

fun QuestionBank.toEntity(): QuestionBankEntity {
    return QuestionBankEntity(
        id = id,
        name = name
    )
}