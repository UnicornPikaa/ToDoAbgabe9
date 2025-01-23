package com.example.todoabgabe9.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoabgabe9.model.ToDo

@Composable
fun ToDoDetailsDialog(
    todo: ToDo,
    onMarkAsCompleted: () -> Unit,
    onReopen: () -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onClose() },
        title = { Text("ToDo Details") },
        text = {
            Column {
                Text("Name: ${todo.name}")
                Text("Priorität: ${todo.priority}")
                Text("Deadline: ${todo.deadline}")
                Text("Beschreibung: ${todo.description}")
            }
        },
        confirmButton = {
            Button(onClick = {
                if (todo.status == 0) { // Status offen
                    onMarkAsCompleted()
                } else { // Status erledigt
                    onReopen()
                }
                onClose()
            }) {
                Text(if (todo.status == 0) "Als erledigt markieren" else "Als offen markieren")
            }
        },
        dismissButton = {
            Row {
                Button(onClick = {
                    onDelete()
                    onClose()
                }) {
                    Text("Löschen")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onClose) {
                    Text("Schließen")
                }
            }
        }
    )
}
