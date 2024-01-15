package com.example.examendavidpascual

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.widget.Toolbar

private lateinit var sharedPref: SharedPreferences

class MainActivity : AppCompatActivity(), Comunicador {

    companion object {
        const val PREFS_NAME = "Prefs_File"
        const val FUENTE = "fuente"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        val lista = FragmentLista()

        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setCustomView(R.layout.custom_toolbar_layout)

            supportActionBar?.customView
        }


        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val dbHelper = DBHelper(this)
        val botonAñadir = findViewById<ImageView>(R.id.btnAñadir)
        val botonCompletas = findViewById<Button>(R.id.btnTareascompletas)


        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLista, FragmentLista())
            .commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentDetalles, FragmentDetalles())
            .commit()

        botonAñadir.setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)

        }
        botonCompletas.setOnClickListener {
            val intent = Intent(this, Activity3::class.java)
            startActivity(intent)
        }


    }

    // Puedes manejar el menú de opciones aquí, si es necesario
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.fuente -> {
                //Cambiar fuente
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.fuente, null)
                builder.setView(dialogLayout)
                builder.setTitle("¿Qué fuente desea?")
                val radioGroup = dialogLayout.findViewById<RadioGroup>(R.id.fuentes)

                if (sharedPref.getString(FUENTE, "fuente1") == "fuente1") {
                    radioGroup.check(R.id.fuente1)
                } else {
                    radioGroup.check(R.id.fuente2)
                }

                radioGroup.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.fuente1 -> {
                            val editor = sharedPref.edit()
                            editor.putString(FUENTE, "fuente1")
                            editor.apply()
                        }

                        R.id.fuente2 -> {
                            val editor = sharedPref.edit()
                            editor.putString(FUENTE, "fuente2")
                            editor.apply()
                        }


                    }
                }
                builder.setPositiveButton("Aceptar") { dialog, which ->
                    dialog.dismiss()
                }
                builder.show()
                true
            }

            else -> super.onOptionsItemSelected(item)

        }

    }

    override fun enviarDatos(tarea: Tarea) {
        val detalles =
            supportFragmentManager.findFragmentById(R.id.fragmentDetalles) as FragmentDetalles
        detalles.mostrarDetalles(tarea)
    }
}