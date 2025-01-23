package com.example.todoabgabe9.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoabgabe9.database.ToDoController
import com.example.todoabgabe9.model.ToDo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedToDosScreen(navController: NavController) {
    val context = LocalContext.current
    val toDoController = remember { ToDoController(context) }
    var todos by remember { mutableStateOf(toDoController.getAllToDos().filter { it.status == 1 }) }
    var selectedToDo by remember { mutableStateOf<ToDo?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Erledigte ToDos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("dashboard") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(todos) { todo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { selectedToDo = todo }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(todo.name, style = MaterialTheme.typography.titleLarge)
                        Text("Priorität: ${todo.priority}")
                        Text("Endzeitpunkt: ${todo.deadline}")
                    }
                }
            }
        }
    }

    selectedToDo?.let { todo ->
        ToDoDetailsDialog(
            todo = todo,
            onMarkAsCompleted = { /* Sollte hier nicht relevant sein */ },
            onReopen = {
                toDoController.updateToDoStatus(todo.id, 0)
                todos = toDoController.getAllToDos().filter { it.status == 1 }
            },
            onDelete = {
                toDoController.deleteToDo(todo.id)
                todos = toDoController.getAllToDos().filter { it.status == 1 }
            },
            onClose = { selectedToDo = null }
        )
    }
}