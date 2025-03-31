package com.example.quizapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizapp.data.local.converter.Converters
import com.example.quizapp.data.local.dao.QuestionBankDao
import com.example.quizapp.data.local.dao.QuestionDao
import com.example.quizapp.data.local.entity.QuestionBankEntity
import com.example.quizapp.data.local.entity.QuestionEntity

@Database(
    entities = [QuestionEntity::class, QuestionBankEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun questionBankDao(): QuestionBankDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getDatabase(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                    "quiz_database"
                )
                    .fallbackToDestructiveMigration() // This will recreate tables if schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}