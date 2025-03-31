package com.example.quizapp.ui.screens.addeditbank

data class AddEditBankState(
    val id: Int = 0,
    val name: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed class AddEditBankEvent {
    data class NameChanged(val name: String) : AddEditBankEvent()
    object SaveBank : AddEditBankEvent()
}