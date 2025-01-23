package com.example.todoabgabe9.model

data class ToDo(
    val id: Int = 0,
    val name: String,
    val priority: Int,
    val deadline: String,
    val description: String,
    val status: Int = 0 // 0: offen, 1: erledigt
)
