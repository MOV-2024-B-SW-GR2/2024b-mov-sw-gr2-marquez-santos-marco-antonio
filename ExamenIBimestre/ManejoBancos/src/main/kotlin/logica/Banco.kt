package org.example.logica

data class Banco(
    var nombre: String,
    val id: Int,
    var numeroAccionistas: Int,
    var saldoTotal: Double,
    var enOperacion: Boolean = true,
    var cuentas: ArrayList<Cuenta> = arrayListOf<Cuenta>(),
    val VALOR_MINIMO_PARA_OPERAR: Double = 1000.00,
    val VALOR_MINIMO_DE_ACCIONISTAS: Int = 2
) {
    init {
        evaluarSaldoTotal()
        tieneSuficientesAccionistasParaOperar()
    }

    fun obtenerCuenta(numCuenta: Int): Cuenta {
        if(this.cuentas.isEmpty()) throw Exception("Crear al menos una cuenta en el banco")
        val cuentaEncontrada: Cuenta? = existeCuenta(numCuenta)
        return cuentaEncontrada as Cuenta
    }

    private fun existeCuenta(numCuenta: Int): Cuenta? {
        var cuentaEncontrada: Cuenta? = null
        this.cuentas.forEach { cuenta: Cuenta ->
            if (cuenta.obtenerNumeroCuenta() == numCuenta) {
                cuentaEncontrada = cuenta
            }
        }
        if (cuentaEncontrada == null) throw Exception("No existe el número de cuenta")
        return cuentaEncontrada
    }

    fun crearCuenta(nombrePropietario: String) {
        val numeroCuenta: Int = (cuentas.maxByOrNull { it.obtenerNumeroCuenta() }?.obtenerNumeroCuenta() ?: 0) + 1
        val nuevaCuenta: Cuenta = Cuenta(numeroCuenta=numeroCuenta, propietario = nombrePropietario)
        this.cuentas.add(nuevaCuenta)
    }

    fun transferir(numeroCuentaTransferente: Int, numeroCuentaTransferido: Int, valorATransferir: Double) {
        val cuentaTransferente: Cuenta? = existeCuenta(numeroCuentaTransferente)
        val cuentaTransfido: Cuenta? = existeCuenta(numeroCuentaTransferido)

        cuentaTransferente?.retirar(valorATransferir)
        cuentaTransfido?.depositar(valorATransferir)
    }

    fun actualizarNombre(nuevoNombre: String) {
        this.nombre = nuevoNombre
    }

    fun aumentarAccionista(cantidadAccionistas: Int) {
        this.numeroAccionistas += cantidadAccionistas
        reactivarOperacion()
    }

    fun disminuirAccionista(cantidadAccionistas: Int) {
        if (cantidadAccionistas>this.numeroAccionistas) throw Exception("No hay tantos accionistas")
        this.numeroAccionistas -= cantidadAccionistas
        tieneSuficientesAccionistasParaOperar()
    }

    override fun toString(): String {
        return "Nombre: ${this.nombre}"+
                "\nId: ${this.id}"+
                "\nCantidad de accionistas: ${this.numeroAccionistas}"+
                "\nFondos del banco: $${String.format("%.2f", this.saldoTotal)}"+
                "\nEsta operando con normalidad: ${this.enOperacion}"+
                "\nCuentas:\n ${this.cuentas}"
    }

    fun depositarEnCuenta(numeroCuenta: Int, valorDeposito: Double) {
        val cuentaDeposito: Cuenta? = existeCuenta(numeroCuenta)
        cuentaDeposito?.depositar(valorDeposito)
        this.saldoTotal+=valorDeposito
        reactivarOperacion()
    }

    fun retirarDeCuenta(numeroCuenta: Int, valorRetiro: Double) {
        estaOperando()
        val cuentaRetiro: Cuenta? = existeCuenta(numeroCuenta)
        cuentaRetiro?.retirar(valorRetiro)
        this.saldoTotal-=valorRetiro
        evaluarSaldoTotal()
    }

    private fun estaOperando() {
        if (!this.enOperacion) throw Exception ("El banco no esta operando para operaciones de retiro, esperando reactivación")
    }

    private fun evaluarSaldoTotal() {
        if (this.saldoTotal<=this.VALOR_MINIMO_PARA_OPERAR) detenerOperacion()
    }

    private fun tieneSuficientesAccionistasParaOperar() {
        if (this.numeroAccionistas<=this.VALOR_MINIMO_DE_ACCIONISTAS) detenerOperacion()
    }

    private fun detenerOperacion() {
        this.enOperacion = false
    }

    fun reactivarOperacion() {
        if (this.numeroAccionistas > this.VALOR_MINIMO_DE_ACCIONISTAS &&
            this.saldoTotal > this.VALOR_MINIMO_PARA_OPERAR) this.enOperacion = true
    }

    fun obtenerId(): Int {
        return this.id
    }

    fun obtenerCuentas(): ArrayList<Cuenta> {
        return this.cuentas
    }
}
