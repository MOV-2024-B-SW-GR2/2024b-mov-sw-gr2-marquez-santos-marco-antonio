package org.example.logica

import java.time.LocalDate

data class Cuenta(
    val numeroCuenta: Int,
    var fondo: Double = 0.00,
    var habilitada: Boolean = true,
    val fechaApertura: LocalDate = LocalDate.now(),
    var propietario:String
){
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