package com.example.quizapp.ui.screens.addquestion

data class AddEditQuestionState(
    val id: Int = 0,
    val questionText: String = "",
    val options: List<String> = listOf(""),  // Start with one empty option
    val correctAnswerIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed class AddEditQuestionEvent {
    data class QuestionTextChanged(val text: String) : AddEditQuestionEvent()
    data class OptionChanged(val index: Int, val text: String) : AddEditQuestionEvent()
    data class CorrectAnswerChanged(val index: Int) : AddEditQuestionEvent()
    object AddOption : AddEditQuestionEvent()
    data class RemoveOption(val index: Int) : AddEditQuestionEvent()
    object SaveQuestion : AddEditQuestionEvent()
}