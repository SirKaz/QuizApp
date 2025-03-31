package com.example.quizapp.ui.screens.banklist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.domain.model.QuestionBank
import com.example.quizapp.ui.components.QuestionBankCard
import com.example.quizapp.ui.theme.QuizAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionBankListScreen(
    viewModel: QuestionBankListViewModel,
    onNavigateToAddBank: () -> Unit,
    onNavigateToEditBank: (Int) -> Unit,
    onNavigateToQuestions: (Int) -> Unit,
    onNavigateToQuiz: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    QuestionBankListContent(
        state = state,
        onAddClick = onNavigateToAddBank,
        onEditClick = onNavigateToEditBank,
        onDeleteClick = viewModel::deleteQuestionBank,
        onViewQuestionsClick = onNavigateToQuestions,
        onStartQuizClick = onNavigateToQuiz
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestionBankListContent(
    state: QuestionBankListState,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (QuestionBank) -> Unit,
    onViewQuestionsClick: (Int) -> Unit,
    onStartQuizClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Question Banks") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.questionBanks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No question banks yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Click + to add a question bank",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.questionBanks) { bank ->
                        QuestionBankCard(
                            questionBank = bank,
                            onEditClick = { onEditClick(bank.id) },
                            onDeleteClick = { onDeleteClick(bank) },
                            onViewQuestionsClick = { onViewQuestionsClick(bank.id) },
                            onStartQuizClick = { onStartQuizClick(bank.id) }
                        )
                    }
                }
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionBankListPreviewEmpty() {
    QuizAppTheme {
        QuestionBankListContent(
            state = QuestionBankListState(),
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onViewQuestionsClick = {},
            onStartQuizClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionBankListPreviewWithBanks() {
    QuizAppTheme {
        QuestionBankListContent(
            state = QuestionBankListState(
                questionBanks = listOf(
                    QuestionBank(1, "General Knowledge"),
                    QuestionBank(2, "Science"),
                    QuestionBank(3, "History")
                )
            ),
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onViewQuestionsClick = {},
            onStartQuizClick = {}
        )
    }
}