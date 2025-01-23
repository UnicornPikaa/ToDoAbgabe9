package com.example.todoabgabe9

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoabgabe9.database.ToDoController
import com.example.todoabgabe9.ui.theme.screen.ActiveToDosScreen
import com.example.todoabgabe9.ui.theme.screen.AddToDoScreen
import com.example.todoabgabe9.ui.theme.screen.CompletedToDosScreen
import com.example.todoabgabe9.ui.theme.screen.Dashboard

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "dashboard") {
                composable("dashboard") {
                    Dashboard(navController)
                }
                composable("active_todos") {
                    ActiveToDosScreen(navController)
                }
                composable("completed_todos") {
                    CompletedToDosScreen(navController)
                }
                composable("add_todo") {
                    AddToDoScreen(navController) { newToDo ->
                        val controller = ToDoController(this@MainActivity)
                        val success = controller.insertToDo(newToDo)
                        if (!success) {
                            Toast.makeText(
                                this@MainActivity,
                                "Fehler beim Speichern des To-Dos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
