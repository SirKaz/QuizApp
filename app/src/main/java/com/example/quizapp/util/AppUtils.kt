package com.example.quizapp.util

import android.app.Activity
import com.example.quizapp.QuizApplication

fun Activity.getAppContainer() = (application as QuizApplication).appContainer