package com.example.examendavidpascual

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import java.time.Month
import java.time.MonthDay
import java.time.Year
import java.util.Calendar

class Activity2 : AppCompatActivity() {
    private var fecha: String = ""
    private var hora: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2)

        val dbHelper = DBHelper(this)
        val botonGuardar = findViewById<Button>(R.id.botonGuardar)
        val botonLimpiar = findViewById<Button>(R.id.botonlimpiar)
        val btnFecha = findViewById<Button>(R.id.btnFecha)
        val nombre = findViewById<EditText>(R.id.nombreTxt)
        if (intent.hasExtra("nombre")) {
            nombre.setText(intent.getStringExtra("nombre"))
        }
        if (intent.hasExtra("mod")){
            val check = findViewById<Button>(R.id.completaCheck)
            check.isEnabled = true
            check.isClickable = true
        }



        botonGuardar.setOnClickListener {
            val fechaCompleta = fecha +"_"+ hora
            val check = findViewById<Button>(R.id.completaCheck)
    Log.d("fecha", fechaCompleta)
            dbHelper.addTarea(nombre.text.toString(), fechaCompleta , check.text.toString())
            finish()
        }
        botonLimpiar.setOnClickListener {
            nombre.setText("")
            fecha = ""
            hora = ""
        }

        btnFecha.setOnClickListener {

            abrirTimePickerDialog()

        }

    }

    private fun abrirTimePickerDialog() {
        // Obtengo una instancia del calendario y fijo la hora y minutos actuales
        val calendario = Calendar.getInstance()

        val year = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minutos = calendario.get(Calendar.MINUTE)

        // Creo un timePickerDialog y le paso el contexto, la hora seleccionada y el minuto
        // y establezco una alarma
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hora, minutos ->
                // Establezco la hora y minutos seleccionados en el textView
                GuardarHora(hora.toString(), minutos.toString())
            },
            hora,
            minutos,
            true
        )
        timePickerDialog.show()
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, mes, dia ->
                // formatear el mes para que tenga 2 dígitos
                val mesFormateado = if (mes + 1 < 10) "0${mes + 1}" else "${mes + 1}"
                GuardarFecha(year,mesFormateado , dia)
            },
            year,
            mes,
            dia
        )
        datePickerDialog.show()
        establecerAlarma(year, mes, dia, hora, minutos)

    }

    fun GuardarFecha(year: Int, mes: String, dia: Int) {
        Log.d("fecha", "$dia/$mes/$year")
        fecha = "$dia/$mes/$year"
    }

    fun GuardarHora(horaS: String, minutos: String) {
        hora = "$horaS:$minutos"
    }
    private fun establecerAlarma(year: Int ,month: Int,day: Int ,hora: Int, minutos: Int) {
        // Obtengo una instancia del calendario y le fijo la hora y minutos obtenidos por parámetro
        val calendarioAlarma = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hora)
            set(Calendar.MINUTE, minutos)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmaReceiver::class.java)

        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Configuramos la alarma en el tiempo especificado y ejecutará el pendintIntent
        // que en este caso crea la notificación
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarioAlarma.timeInMillis, pendingIntent)
    }
}