package com.example.examendavidpascual

class Tarea {

    var nombre :String = ""
    var fecha :String = ""
    var completado: Boolean = false

    constructor(nombre: String, fecha: String, completado: Boolean) {
        this.nombre = nombre
        this.fecha = fecha
        this.completado = completado
    }
    override fun toString(): String {
        return "$nombre"
    }


}
