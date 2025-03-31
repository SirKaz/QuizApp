package com.example.quizapp.di

import android.content.Context
import com.example.quizapp.data.local.database.QuizDatabase
import com.example.quizapp.data.repository.QuestionBankRepositoryImpl
import com.example.quizapp.data.repository.QuestionRepositoryImpl
import com.example.quizapp.domain.repository.QuestionBankRepository
import com.example.quizapp.domain.repository.QuestionRepository

class AppContainer(private val applicationContext: Context) {

    // Database
    private val database: QuizDatabase by lazy {
        QuizDatabase.getDatabase(applicationContext)
    }

    // DAOs
    private val questionDao by lazy {
        database.questionDao()
    }

    private val questionBankDao by lazy {
        database.questionBankDao()
    }

    // Repositories
    val questionRepository: QuestionRepository by lazy {
        QuestionRepositoryImpl(questionDao)
    }

    val questionBankRepository: QuestionBankRepository by lazy {
        QuestionBankRepositoryImpl(questionBankDao)
    }
}