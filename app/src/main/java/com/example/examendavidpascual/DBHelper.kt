package com.example.examendavidpascual

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DBExam"
        private const val DATABASE_VERSION = 1

        // Nombre de la tabla donde se almacenarán los discos.
        private const val TABLE_TAREAS = "tareas"

        // Nombres de las columnas de la tabla.
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_FECHA = "fecha"
        private const val KEY_COMPLETADO = "completado"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_TAREAS = ("CREATE TABLE " + TABLE_TAREAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOMBRE + " TEXT,"
                + KEY_FECHA + " TEXT,"
                + KEY_COMPLETADO + " TEXT" + ")")
        db?.execSQL(CREATE_TABLE_TAREAS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE TAREAS")
        onCreate(db)
    }

    fun addTarea(nombre:String,fecha:String,completado:String): Long {
        try {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(KEY_NOMBRE, nombre)
            values.put(KEY_FECHA, fecha)
            values.put(KEY_COMPLETADO, completado)
            Log.d("DBHelper", values.toString())
            val success = db.insert(TABLE_TAREAS, null, values)
            Log.d("DBHelper", "Tarea añadida")
            db.close()
            return success
        } catch (e: Exception) {
            Log.d("DBHelper", e.toString())
            return -1
        }
    }

    fun getallTareas(): ArrayList<Tarea> {
        val tareas = ArrayList<Tarea>()
        val selectQuery = "SELECT  * FROM $TABLE_TAREAS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var nombre: String
        var fecha: String
        var completado: Boolean

        if (cursor.moveToFirst()) {
            do {

                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
                val fechaIndex = cursor.getColumnIndex(KEY_FECHA)
                val completadoIndex = cursor.getColumnIndex(KEY_COMPLETADO)
                if (idIndex!=-1){
                    nombre = cursor.getString(nombreIndex)
                    fecha = cursor.getString(fechaIndex)
                    Log.d("DBHelper1", nombre)
                    completado = cursor.getString(completadoIndex).toBoolean()
                    val tarea = Tarea(nombre, fecha, completado)
                    tareas.add(tarea)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tareas
    }

    fun completarTarea(tarea: Tarea) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NOMBRE, tarea.nombre)
        values.put(KEY_FECHA, tarea.fecha)
        values.put(KEY_COMPLETADO, true)
        db.update(TABLE_TAREAS, values, "$KEY_NOMBRE=?", arrayOf(tarea.nombre))
        db.close()

    }

    fun borrarTarea(tarea: Tarea) {
        val db = this.writableDatabase
        db.delete(TABLE_TAREAS, "$KEY_NOMBRE=?", arrayOf(tarea.nombre))
        db.close()

    }


}