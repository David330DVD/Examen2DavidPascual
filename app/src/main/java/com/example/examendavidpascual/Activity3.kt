package com.example.examendavidpascual

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class Activity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)
        val DBHelper = DBHelper(this)
        val lista = findViewById<ListView>(R.id.lista)


        val listaTareas = DBHelper.getallTareas()
        val tareas = DBHelper.getallTareas()

        val nombres = ArrayList<String>()
        tareas.forEach {
            Log.d("DBHelper", it.nombre)
            nombres.add(it.nombre)
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            nombres
        )

        lista.adapter = adapter
        lista.setOnItemClickListener { parent, _, position, _ ->
            val nombre = parent.getItemAtPosition(position) as String
            val tarea = listaTareas.find { it.nombre == nombre }
            if (tarea != null) {
                val intent = Intent(this, Activity2::class.java)
                intent.putExtra("nombre", tarea.nombre)
                intent.putExtra("fecha", tarea.fecha)
                intent.putExtra("completa", tarea.completado)
                intent.putExtra("mod", true)
                startActivity(intent)
            }
        }
        val botonVolver = findViewById<Button>(R.id.butVolver)
        botonVolver.setOnClickListener {
            finish()
        }

    }
}