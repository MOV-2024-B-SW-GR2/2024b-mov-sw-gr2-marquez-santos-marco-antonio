package com.example.appmanejobanco_cuenta

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class Cuenta(
    val numeroCuenta: Int,
    var fondo: Double = 0.00,
    var habilitada: Boolean = true,
    var fechaApertura: String = "",
    var propietario:String
){
    init {
        val cal: Calendar = Calendar.getInstance()
        val fechaActual: Date = cal.getTime()
        val formatoFecha: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        this.fechaApertura = formatoFecha.format(fechaActual)
    }
    fun obtenerNumeroCuenta(): Int {
        return this.numeroCuenta
    }

    fun depositar(valorADepositar: Double) {
        this.fondo+=valorADepositar
    }

    fun retirar(valorARetirar: Double) {
        if ((this.fondo-valorARetirar)<0) throw Exception ("No hay fondos suficientes")
        this.fondo-=valorARetirar
    }

    override fun toString(): String {
        return "Numero de cuenta: ${this.numeroCuenta}" +
                "\nPropietario: ${this.propietario}"+
                "\nSaldo disponible: $${String.format("%.2f", this.fondo)}"+
                "\nHabilitada: ${this.habilitada}"+
                "\nfecha de apertura: ${this.fechaApertura}"
    }

    fun actualizarNombrePropietario(nuevoNombrePropietario: String) {
        this.propietario = nuevoNombrePropietario
    }
}