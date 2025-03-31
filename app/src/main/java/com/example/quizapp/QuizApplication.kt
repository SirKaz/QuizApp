package com.example.quizapp

import android.app.Application
import com.example.quizapp.di.AppContainer

class QuizApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}