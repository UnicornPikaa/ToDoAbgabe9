package com.example.todoabgabe9.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream

/**
 * Eine Hilfsklasse zur Verwaltung der Erstellung, Verbindung und Aktualisierung der To-Do-Datenbank
 * Diese Klasse verwendet eine SQLite-Datenbank, die im Assets-Ordner der App gespeichert ist
 *
 * @param context Der Anwendungskontext, der verwendet wird, um die Datenbank und Assets zu finde.
 */

class DbHelper(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Wird aufgerufen, wenn die Datenbank zum ersten Mal erstellt wird.
     * Diese Methode lädt die Datenbank aus dem Assets-Ordner
     *
     * @param db Die Instanz der Datenbank
     */
    override fun onCreate(db: SQLiteDatabase?) {
        // Die Datenbank wird aus den Assets geladen.
    }

    /**
     * Wird aufgerufen, wenn die Datenbank aktualisiert werden muss.
     * Löscht die vorhandene Datenbank und lädt sie erneut aus dem Assets-Ordner
     *
     * @param db         Die Instanz der Datenbank
     * @param oldVersion Die alte Versionsnummer der Datenbank
     * @param newVersion Die neue Versionsnummer der Datenbank
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        context.deleteDatabase(DATABASE_NAME)
        copyDatabaseFromAssets()
    }

    /**
     * Öffnet die Datenbank im Nur-Lese-Modus. Falls die Datenbank nicht existiert, wird sie
     * aus dem Assets-Ordner kopiert, bevor sie geöffnet wird
     *
     * @return Eine lesbare Instanz der SQLite-Datenbank
     */
    override fun getReadableDatabase(): SQLiteDatabase {
        copyDatabaseFromAssets()
        return super.getReadableDatabase()
    }

    /**
     * Öffnet die Datenbank im Lese- und Schreibmodus. Falls die Datenbank nicht existiert, wird sie
     * aus dem Assets-Ordner kopiert, bevor sie geöffnet wird
     *
     * @return Eine beschreibbare Instanz der SQLite-Datenbank
     */
    override fun getWritableDatabase(): SQLiteDatabase {
        copyDatabaseFromAssets()
        return super.getWritableDatabase()
    }

    /**
     * Kopiert die Datenbankdatei aus dem Assets-Ordner in das Datenverzeichnis der App.
     * Diese Methode überprüft, ob die Datenbank bereits existiert, bevor sie kopiert wird
     */
    private fun copyDatabaseFromAssets() {
        val dbPath = context.getDatabasePath(DATABASE_NAME)
        if (!dbPath.exists()) {
            try {
                context.assets.open(DATABASE_NAME).use { inputStream ->
                    FileOutputStream(dbPath).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("DbHelper", "Error copying database", e)
            }
        }
    }

    companion object {
        const val DATABASE_NAME = "ToDo.db"
        const val DATABASE_VERSION = 1
    }
}