package com.example.examendavidpascual

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.examendavidpascual.DBHelper
import com.example.examendavidpascual.R
import com.example.examendavidpascual.Tarea

class FragmentLista : Fragment() {

    private lateinit var comunicador: Comunicador
    override fun onAttach(context: Context) {
        super.onAttach(context)
        comunicador = context as Comunicador
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.layout_lista,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recibirDatos()

    }

     fun recibirDatos() {
        val DBHelper = DBHelper(requireContext())
        val tareas = DBHelper.getallTareas()
        Log.d("DBHelper2", tareas.get(0).nombre)

        val nombres = ArrayList<String>()
        tareas.forEach {
            Log.d("DBHelper", it.nombre)
            nombres.add(it.nombre)
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            nombres
        )

        val lista = view?.findViewById<android.widget.ListView>(R.id.lista)
        lista?.adapter = adapter

        lista?.setOnItemClickListener { parent, _, position, _ ->
            val nombre = parent.getItemAtPosition(position) as String
            val tarea = tareas.find { it.nombre == nombre }

            if (tarea != null) {
                comunicador.enviarDatos(tarea)
            }


        }
        lista?.setOnItemLongClickListener { parent, _, position, _ ->
            val nombre = parent.getItemAtPosition(position) as String
            val tarea = tareas.find { it.nombre == nombre }

            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.menu, null)
            builder.setView(dialogLayout)
            builder.setTitle("¿Qué desea hacer?")
            val botonCompletar = dialogLayout.findViewById<View>(R.id.btnCompletar)
            val botonBorrar = dialogLayout.findViewById<View>(R.id.btnEliminar)
            val botonEditar = dialogLayout.findViewById<View>(R.id.btnModificar)
            val botonEnviar = dialogLayout.findViewById<View>(R.id.btnEnviar)

            botonCompletar.setOnClickListener {
                if (tarea != null) {
                    DBHelper.completarTarea(Tarea(tarea.nombre, tarea.fecha, true))
                    Toast.makeText(requireContext(), "Tarea completada", Toast.LENGTH_SHORT).show()

                }

            }
            botonEditar.setOnClickListener {
                val intent = android.content.Intent(requireContext(), Activity2::class.java)
                intent.putExtra("nombre", tarea?.nombre)
                intent.putExtra("fecha", tarea?.fecha)
                intent.putExtra("mod", "true")
                startActivity(intent)
            }
            botonBorrar.setOnClickListener {
                if (tarea != null) {
                    DBHelper.borrarTarea(tarea)
                    Toast.makeText(requireContext(), "Tarea borrada", Toast.LENGTH_SHORT).show()

                }
            }
            botonEnviar.setOnClickListener {
                val intent = android.content.Intent(android.content.Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tarea")
                intent.putExtra(
                    android.content.Intent.EXTRA_TEXT,
                    tarea?.nombre + "\n" + tarea?.fecha
                )
                startActivity(intent)
            }

            builder.setNegativeButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()

            true
        }
    }

}