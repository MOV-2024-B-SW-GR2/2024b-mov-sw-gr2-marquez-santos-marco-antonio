package com.example.appmanejobanco_cuenta.basedatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.appmanejobanco_cuenta.Banco
import com.example.appmanejobanco_cuenta.Cuenta

class SqliteHelperBanco(
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLcrearBanco =
            """
                CREATE TABLE BANCO(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                numero_accionistas INTEGER,
                saldo_total INTEGER,
                esta_operando INTEGER                
                );        
            """.trimIndent()
        val scriptSQLcrearCuenta =
            """
                CREATE TABLE CUENTA(
                numero_cuenta INTEGER,
                fondo REAL,
                habilitada INTEGER,
                fecha_apertura TEXT,
                propietario TEXT,
                id_banco INTEGER,
                PRIMARY KEY (numero_cuenta, id_banco),
                FOREIGN KEY (id_banco) REFERENCES BANCO(id) ON DELETE CASCADE
            );            
            """.trimIndent()
        db?.execSQL(scriptSQLcrearBanco)
        db?.execSQL(scriptSQLcrearCuenta)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearBanco(
        nombre: String,
        numero_accionistas: Int,
        saldo_total: Int,
        esta_operando: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("numero_accionistas", numero_accionistas)
        valoresGuardar.put("saldo_total", saldo_total)
        valoresGuardar.put("esta_operando", esta_operando)
        val resultadoGuardar = baseDatosEscritura
            .insert(
                "BANCO",
                null,
                valoresGuardar
            )
        baseDatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarBanco(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura
            .delete(
                "BANCO",
                "id=?",
                parametrosConsultaDelete
            )
        baseDatosEscritura.close()
        return if (resultadoEliminar.toInt() == -1) false else true
    }

    fun actualizarBanco(
        id: Int,
        nombre: String,
        numero_accionistas: Int,
        saldo_total: Int,
        esta_operando: Int
    ): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresActualizar = ContentValues()
        valoresActualizar.put("nombre", nombre)
        valoresActualizar.put("numero_accionistas", numero_accionistas)
        valoresActualizar.put("saldo_total", saldo_total)
        valoresActualizar.put("esta_operando", esta_operando)
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura
            .update(
                "BANCO",
                valoresActualizar,
                "id=?",
                parametrosConsultaActualizar
            )
        baseDatosEscritura.close()
        return if (resultadoActualizar.toInt() == -1) false else true
    }

    fun consultarBancoPorId(id: Int): Banco?{
        val baseDatosLectura = readableDatabase
        var scriptConsultaLectura = """
            SELECT * FROM BANCO WHERE ID = ?
        """.trimIndent()

        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura
            .rawQuery(
                scriptConsultaLectura,
                parametrosConsultaLectura
            )
        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        if (existeAlMenosUno) {
            val arregloRespuesta = arrayListOf<Banco>()
            do {
                val banco = Banco(
                    resultadoConsultaLectura.getString(1),
                    resultadoConsultaLectura.getInt(0),
                    resultadoConsultaLectura.getInt(2),
                    (resultadoConsultaLectura.getInt(3) / 100).toDouble(),
                    obtenerEstado(resultadoConsultaLectura.getInt(4))
                )
                arregloRespuesta.add(banco)
            } while (resultadoConsultaLectura.moveToNext())
            return arregloRespuesta[0]
        } else {
            return null
        }
    }

    private fun obtenerEstado(int: Int): Boolean {
        return int == 1
    }

    fun consultarBancos(): ArrayList<Banco>{
        val baseDatosLectura = readableDatabase
        var scriptConsultaLectura = """
            SELECT * FROM BANCO
        """.trimIndent()

        val resultadoConsultaLectura = baseDatosLectura
            .rawQuery(
                scriptConsultaLectura,
                arrayOf()
            )
        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        if (existeAlMenosUno) {
            val arregloRespuesta = arrayListOf<Banco>()
            do {
                val banco = Banco(
                    resultadoConsultaLectura.getString(1),
                    resultadoConsultaLectura.getInt(0),
                    resultadoConsultaLectura.getInt(2),
                    (resultadoConsultaLectura.getInt(3)).toDouble() / 100,
                    obtenerEstado(resultadoConsultaLectura.getInt(4))
                )
                arregloRespuesta.add(banco)
            } while (resultadoConsultaLectura.moveToNext())
            return arregloRespuesta
        } else {
            return arrayListOf<Banco>()
        }
    }

    fun crearCuenta(numeroCuenta: Int, fondo: Double, habilitada: Boolean, fechaApertura: String, propietario: String, idBanco: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues().apply {
            put("numero_cuenta", numeroCuenta)
            put("fondo", fondo)
            put("habilitada", if (habilitada) 1 else 0)
            put("fecha_apertura", fechaApertura)
            put("propietario", propietario)
            put("id_banco", idBanco)
        }
        val resultado = baseDatosEscritura.insert("CUENTA", null, valoresGuardar)
        baseDatosEscritura.close()
        return resultado != -1L
    }

    fun eliminarCuenta(numeroCuenta: Int, idBanco: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val resultado = baseDatosEscritura.delete(
            "CUENTA",
            "numero_cuenta=? AND id_banco=?",
            arrayOf(numeroCuenta.toString(), idBanco.toString())
        )
        baseDatosEscritura.close()
        return resultado > 0
    }

    fun actualizarCuenta(numeroCuenta: Int, fondo: Double, habilitada: Boolean, fechaApertura: String, propietario: String, idBanco: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresActualizar = ContentValues().apply {
            put("fondo", fondo)
            put("habilitada", if (habilitada) 1 else 0)
            put("fecha_apertura", fechaApertura)
            put("propietario", propietario)
            put("id_banco", idBanco)
        }
        val resultado = baseDatosEscritura.update(
            "CUENTA",
            valoresActualizar,
            "numero_cuenta=? AND id_banco=?",
            arrayOf(numeroCuenta.toString(), idBanco.toString()) // Aquí ambos parámetros
        )
        baseDatosEscritura.close()
        return resultado > 0
    }

    fun consultarCuentaPorNumero(numeroCuenta: Int): Cuenta? {
        val baseDatosLectura = readableDatabase
        val resultado = baseDatosLectura.rawQuery("SELECT * FROM CUENTA WHERE numero_cuenta = ?", arrayOf(numeroCuenta.toString()))
        return if (resultado.moveToFirst()) {
            Cuenta(
                resultado.getInt(0),
                resultado.getDouble(1),
                resultado.getInt(2) == 1,
                resultado.getString(3),
                resultado.getString(4)
            )
        } else null
    }

    fun consultarCuentasPorBanco(idBanco: Int): ArrayList<Cuenta> {
        val baseDatosLectura = readableDatabase
        val resultado = baseDatosLectura.rawQuery("SELECT * FROM CUENTA WHERE id_banco = ?", arrayOf(idBanco.toString()))
        val listaCuentas = ArrayList<Cuenta>()

        while (resultado.moveToNext()) {
            val cuenta = Cuenta(
                resultado.getInt(0),
                resultado.getDouble(1),
                resultado.getInt(2) == 1,
                resultado.getString(3),
                resultado.getString(4)
            )
            listaCuentas.add(cuenta)
        }
        return listaCuentas
    }
}