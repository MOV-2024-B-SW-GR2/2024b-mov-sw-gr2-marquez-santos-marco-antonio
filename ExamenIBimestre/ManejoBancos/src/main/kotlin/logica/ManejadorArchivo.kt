package org.example.logica

import com.google.gson.GsonBuilder
import java.time.LocalDate
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ManejadorArchivo(private val rutaArchivo: String) {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, AdapatadorFecha()) // Registrar el adaptador
        .create()

    // Guardar datos en el archivo
    fun guardarDatos(bancos: ArrayList<Banco>) {
        val archivoTemporal = File("$rutaArchivo.tmp")
        try {
            FileWriter(archivoTemporal).use { writer ->
                val json = gson.toJson(bancos)
                writer.write(json)
            }
            val archivoPrincipal = File(rutaArchivo)
            if (archivoPrincipal.exists()) archivoPrincipal.delete() // Eliminar el archivo original
            archivoTemporal.renameTo(archivoPrincipal) // Reemplazar el archivo
        } catch (e: IOException) {
            println("Error al guardar los datos: ${e.message}")
        }
    }

    // Cargar datos desde el archivo
    fun cargarDatos(): ArrayList<Banco> {
        val archivoPrincipal = File(rutaArchivo)
        return if (archivoPrincipal.exists()) {
            try {
                val json = archivoPrincipal.readText()
                val tipoLista = object : TypeToken<ArrayList<Banco>>() {}.type
                gson.fromJson(json, tipoLista)
            } catch (e: Exception) {
                println("Error al cargar los datos: ${e.message}")
                arrayListOf()
            }
        } else {
            arrayListOf() // Devuelve una lista vacía si no existe el archivo
        }
    }
}