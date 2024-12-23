package com.andreasmlbngaol.codegenerator.views.code

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.andreasmlbngaol.codegenerator.model.db.CodeEvent
import com.andreasmlbngaol.codegenerator.model.db.CodeState

@Composable
fun AddCodeDialog(
    state: CodeState,
    onEvent: (CodeEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(CodeEvent.HideDialog)
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(CodeEvent.SaveCode)
                }
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onEvent(CodeEvent.HideDialog)
                }
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Add Code")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = {
                        onEvent(CodeEvent.SetTitle(it))
                    },
                    label = {
                        Text(text = "Title")
                    }
                )
                OutlinedTextField(
                    value = state.content,
                    onValueChange = {
                        onEvent(CodeEvent.SetContent(it))
                    },
                    label = {
                        Text(text = "Text to generate")
                    }
                )
            }
        }
    )
}