package com.example.todoabgabe9.ui.theme.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoabgabe9.model.ToDo
/**
 * Eine Composable-Funktion zur Darstellung einer To-Do-Karte.
 * Die Karte zeigt grundlegende Informationen über ein To-Do an und ermöglicht es,
 * das To-Do zu aktualisieren (als erledigt zu markieren) oder zu löschen.
 *
 * @param todo Das To-Do Objekt, das angezeigt wird.
 * @param onUpdate Eine Lambda-Funktion, die aufgerufen wird, um das To-Do als erledigt zu markieren.
 * @param onDelete Eine Lambda-Funktion, die aufgerufen wird, um das To-Do zu löschen.
 */
@Composable
fun ToDoCard(
    todo: ToDo,
    onUpdate: () -> Unit,
    onDelete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { showDialog = true }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = todo.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "Priorität: ${todo.priority}")
            Text(text = "Endzeitpunkt: ${todo.deadline}")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
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
                    onUpdate() // Markiere das To-Do als erledigt
                    showDialog = false
                }) {
                    Text("Als erledigt markieren")
                }
            },
            dismissButton = {
                Row {
                    Button(onClick = {
                        onDelete()
                        showDialog = false
                    }) {
                        Text("Löschen")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { showDialog = false }) {
                        Text("Schließen")
                    }
                }
            }
        )
    }
}
