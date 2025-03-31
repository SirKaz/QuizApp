package com.example.quizapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.domain.model.QuestionBank
import com.example.quizapp.ui.theme.QuizAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon

@Composable
fun QuestionBankCard(
    questionBank: QuestionBank,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onViewQuestionsClick: () -> Unit,
    onStartQuizClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = questionBank.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewQuestionsClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Questions")
                }
                Button(
                    onClick = onStartQuizClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start Quiz")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionBankCardPreview() {
    QuizAppTheme {
        QuestionBankCard(
            questionBank = QuestionBank(1, "Norse Mythology"),
            onEditClick = {},
            onDeleteClick = {},
            onViewQuestionsClick = {},
            onStartQuizClick = {}
        )
    }
}