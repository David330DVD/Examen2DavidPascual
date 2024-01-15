package com.example.examendavidpascual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.examendavidpascual.R

class FragmentDetalles : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.layout_detalles,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    fun mostrarDetalles(tarea: Tarea) {

        val nombre = view?.findViewById<android.widget.TextView>(R.id.nombre)
        val fecha = view?.findViewById<android.widget.TextView>(R.id.fecha)
        val completado = view?.findViewById<android.widget.TextView>(R.id.checkCompletada)
        nombre?.text = tarea.nombre
        fecha?.text = tarea.fecha
        if (tarea.completado) {

            completado?.isEnabled  = true
        }


    }
}