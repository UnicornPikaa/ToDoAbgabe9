package com.example.todoabgabe9.ui.theme.screen


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoabgabe9.database.ToDoController
import com.example.todoabgabe9.ui.theme.component.ToDoCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveToDosScreen(navController: NavController) {
    val context = LocalContext.current
    val toDoController = remember { ToDoController(context) }
    var todos by remember { mutableStateOf(toDoController.getAllToDos().filter { it.status == 0 }) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aktive ToDos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("dashboard") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_todo") // Navigiere zur AddToDoScreen
            }) {
                Icon(Icons.Default.Add, contentDescription = "Neues ToDo hinzufügen")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(
                todos.groupBy { it.priority }
                    .toSortedMap(reverseOrder())
                    .toList()
            ) { (priority, todosByPriority) ->
                Text(
                    text = when (priority) {
                        3 -> "Priorität: Sehr dringend - 3"
                        2 -> "Priorität: Wichtig aber nicht dringend - 2"
                        else -> "Priorität: Nicht so wichtig - 1"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
                todosByPriority.forEach { todo ->
                    ToDoCard(
                        todo = todo,
                        onUpdate = {
                            toDoController.updateToDoStatus(todo.id, 1) // Markiere als erledigt
                            todos = toDoController.getAllToDos().filter { it.status == 0 } // Aktualisiere die Liste
                        },
                        onDelete = {
                            toDoController.deleteToDo(todo.id) // Lösche das To-Do
                            todos = toDoController.getAllToDos().filter { it.status == 0 } // Aktualisiere die Liste
                        }
                    )
                }
            }
        }
    }
}
