package com.example.quizapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.QuizAppTheme

@Composable
fun OptionInput(
    value: String,
    onValueChange: (String) -> Unit,
    isSelected: Boolean,
    onSelectedChange: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
    showRemoveButton: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelectedChange
        )

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            label = { Text("Option") },
            singleLine = true
        )

        if (showRemoveButton) {
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onRemoveClick) {
                Text("✕")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionInputPreview() {
    QuizAppTheme {
        OptionInput(
            value = "Sample option",
            onValueChange = {},
            isSelected = false,
            onSelectedChange = {},
            onRemoveClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OptionInputPreviewSelected() {
    QuizAppTheme {
        OptionInput(
            value = "Selected option",
            onValueChange = {},
            isSelected = true,
            onSelectedChange = {},
            onRemoveClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}