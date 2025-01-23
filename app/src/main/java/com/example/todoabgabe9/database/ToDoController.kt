package com.example.todoabgabe9.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.todoabgabe9.model.ToDo

/**
 * Controller Klasse zur Verwaltung von To-Do-Elementen in der Datenbank
 *
 * @param context Der Anwendungskontext, der für den Zugriff auf die Datenbank erforderlich ist
 */
class ToDoController(context: Context) {
    private val dbHelper = DbHelper(context)

    /**
     * Fügt ein neues To-Do-Element in die Datenbank ein.
     *
     * @param todo das To-Do Objekt, das eingefügt werden soll
     * @return "true", wenn das Einfügen erfolgreich war, andernfalls "false"
     */
    fun insertToDo(todo: ToDo): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("priority", todo.priority)
                put("deadline", todo.deadline)
                put("description", todo.description)
                put("status", todo.status)
            }
            val result = db.insert("ToDo", null, values)
            result != -1L
        } catch (e: Exception) {
            Log.e("ToDoController", "Insert failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Löscht ein To-Do-Element aus der Datenbank
     *
     * @param todoId Die ID des To-Do-Elements, das gelöscht werden soll
     * @return "true", wenn das Löschen erfolgreich war, andernfalls "false"
     */
    fun deleteToDo(todoId: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val result = db.delete("ToDo", "id = ?", arrayOf(todoId.toString()))
            result > 0
        } catch (e: Exception) {
            Log.e("ToDoController", "Delete failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Aktualisiert den Status eines To-Do-Elements
     *
     * @param todoId Die ID des To-Do-Elements, dessen Status aktualisiert werden soll
     * @param newStatus Der neue Statuswert (z. B. 0 = offen, 1 = erledigt)
     * @return "true", wenn die Statusaktualisierung erfolgreich war, andernfalls "false"
     */

    fun updateToDoStatus(todoId: Int, newStatus: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("status", newStatus)
            }
            val result = db.update("ToDo", values, "id = ?", arrayOf(todoId.toString()))
            result > 0
        } catch (e: Exception) {
            Log.e("ToDoController", "Status Update failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Ruft alle To-Do-Elemente aus der Datenbank ab
     *
     * @return Eine Liste von To-Do-Objekten
     */
    fun getAllToDos(): List<ToDo> {
        val db = dbHelper.readableDatabase
        val todos = mutableListOf<ToDo>()
        val cursor = db.rawQuery("SELECT * FROM ToDo", null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val todo = ToDo(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority")),
                        deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    )
                    todos.add(todo)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("ToDoController", "Fetching todos failed", e)
        } finally {
            cursor.close()
            db.close()
        }
        return todos
    }
}