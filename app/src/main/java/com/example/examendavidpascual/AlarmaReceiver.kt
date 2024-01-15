package com.example.examendavidpascual

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

/**
 * Se hereda de BroadcastReceiver
 */
class AlarmaReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        crearNotificacion(context)
    }

    // Función para crear una notificación
    private fun crearNotificacion(context: Context?) {
        Log.d("AlarmaFragment", "Alarma creada")
        val ID_CANAL = "mi_canal_id"
        val idNotificacion = 101
        crearCanal(context)
        // Intent para reiniciar la aplicación cuando se haga clic en la notificación
        val intentReinicio = Intent(context, MainActivity::class.java)
        intentReinicio.action = "FROM_NOTIFICATION_ACTION"
        intentReinicio.putExtra("fromNotification", true)
        val pendingIntentReinicio = PendingIntent.getActivity(context, 0, intentReinicio,
            PendingIntent.FLAG_IMMUTABLE)

        // Crea el canal de notificación
        val administradorNotificaciones =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificacion = NotificationCompat.Builder(context, ID_CANAL)
            .setContentTitle("Alarma App")
            .setContentText("Alarma Creada")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntentReinicio)
            .setAutoCancel(true)  // La notificación se cierra automáticamente al hacer clic en ella
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        //Muestra la notificación
        administradorNotificaciones.notify(idNotificacion, notificacion)
    }
    // Función para crear un canal de notificación
    private fun crearCanal(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Nombre del Canal"
            val channelDescription = "Descripción del Canal"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = "mi_canal_id"

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}