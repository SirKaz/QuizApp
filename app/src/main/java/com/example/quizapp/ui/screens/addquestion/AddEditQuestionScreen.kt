package com.example.quizapp.ui.screens.addquestion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.components.OptionInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditQuestionScreen(
    viewModel: AddEditQuestionViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if (state.id == 0) "Add Question" else "Edit Question")
                },
                navigationIcon = {
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.questionText,
                onValueChange = { viewModel.onEvent(AddEditQuestionEvent.QuestionTextChanged(it)) },
                label = { Text("Question") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Options:")

            Spacer(modifier = Modifier.height(8.dp))

            state.options.forEachIndexed { index, option ->
                OptionInput(
                    value = option,
                    onValueChange = { viewModel.onEvent(AddEditQuestionEvent.OptionChanged(index, it)) },
                    isSelected = index == state.correctAnswerIndex,
                    onSelectedChange = { viewModel.onEvent(AddEditQuestionEvent.CorrectAnswerChanged(index)) },
                    onRemoveClick = { viewModel.onEvent(AddEditQuestionEvent.RemoveOption(index)) },
                    showRemoveButton = state.options.size > 1,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if (state.options.size < 10) {
                Button(
                    onClick = { viewModel.onEvent(AddEditQuestionEvent.AddOption) },
                    modifier = Modifier.padding(vertical = 8.dp),
                    enabled = !state.isLoading
                ) {
                    Text("Add Option")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { viewModel.onEvent(AddEditQuestionEvent.SaveQuestion) },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading
                ) {
                    Text("Save Question")
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}