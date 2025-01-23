package com.example.todoabgabe9.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoabgabe9.model.ToDo
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDoScreen(
    navController: NavController,
    onSave: (ToDo) -> Unit // Verwendet, um das neue To-Do zu speichern
) {
    var name by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("1") }
    var deadline by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neues ToDo hinzufügen") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("active_todos") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                TextField(
                    value = priority,
                    onValueChange = { priority = it },
                    label = { Text("Priorität (1-3)") }
                )
                TextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Deadline (DD.MM.YYYY)") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Beschreibung") }
                )
                Button(onClick = {
                    if (priority.toIntOrNull() !in 1..3) {
                        Toast.makeText(navController.context, "Priorität muss zwischen 1 und 3 liegen", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!deadline.matches(Regex("\\d{2}\\.\\d{2}\\.\\d{4}"))) {
                        Toast.makeText(navController.context, "Deadline muss im Format DD.MM.YYYY sein", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Überprüfe, ob das Datum in der Vergangenheit liegt (heute erlaubt)
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val deadlineDate = dateFormat.parse(deadline)
                    val currentDate = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.time

                    if (deadlineDate.before(currentDate)) {
                        Toast.makeText(navController.context, "Deadline darf nicht in der Vergangenheit liegen", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val newToDo = ToDo(
                        name = name,
                        priority = priority.toInt(),
                        deadline = deadline,
                        description = description,
                        status = 0 // Standardmäßig: Aktiv
                    )

                    // Verwende onSave, um das To-Do zu speichern
                    onSave(newToDo)

                    // Navigiere zurück zur Ansicht für aktive To-Dos
                    navController.navigate("active_todos")
                }) {
                    Text("Speichern")
                }
            }
        }
    )
}
