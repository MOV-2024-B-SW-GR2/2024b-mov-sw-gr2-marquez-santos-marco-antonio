package com.example.gr2sw2024b_mams

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelperEntrenador(
    contexto: Context?
):SQLiteOpenHelper (
    contexto,
    "moviles",
    null,
    1
){
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLrearTablaEntrenador =
            """
                CREATE TABLE ENTRENADOR(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                descripcion VARCHAR(50)
                )
            """.trimIndent()
        db?.execSQL(scriptSQLrearTablaEntrenador)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearEntrenador(nombre:String,descripcion:String):Boolean{
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre",nombre)
        valoresGuardar.put("descripcion",descripcion)
        val resultadoGuardar = baseDatosEscritura
            .insert(
                "ENTRENADOR",
                null,
                valoresGuardar
            )
        baseDatosEscritura.close()
        return  if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarEntrenador(id:Int):Boolean{
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura
            .delete(
                "ENTRENADOR",
                "id=?",
                parametrosConsultaDelete
            )
        baseDatosEscritura.close()
        return if (resultadoEliminar.toInt() == -1) false else true
    }

    fun actualizarEntrenador(id:Int, nombre:String,
                             descripcion:String):Boolean{
        val baseDatosEscritura = writableDatabase
        val valoresActualizar = ContentValues()
        valoresActualizar.put("nombre",nombre)
        valoresActualizar.put("descripcion",descripcion)
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura
            .update(
                "ENTRENADOR",
                valoresActualizar,
                "id=?",
                parametrosConsultaActualizar
            )
        baseDatosEscritura.close()
        return if (resultadoActualizar.toInt() == -1) false else true
    }

    fun consultarEntrenadorPorId(id:Int):BEntrenador?{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM ENTRENADOR WHERE ID = ?
        """.trimIndent()
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura
            .rawQuery(
                scriptConsultaLectura,
                parametrosConsultaLectura
            )
        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()
        if (existeAlMenosUno){
            val arregloRespuesta = arrayListOf<BEntrenador>()
            do{
                val entrenador = BEntrenador(
                    resultadoConsultaLectura.getInt(0),
                    resultadoConsultaLectura.getString(1),
                    resultadoConsultaLectura.getString(2)
                )
                arregloRespuesta.add(entrenador)
            }while (resultadoConsultaLectura.moveToNext())
            return arregloRespuesta[0]
        } else {
            return null
        }
    }



}