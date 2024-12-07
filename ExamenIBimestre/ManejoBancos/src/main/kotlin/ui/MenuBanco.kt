package org.example.ui

import org.example.logica.Banco
import org.example.logica.ManejadorArchivo

class MenuBanco {
    companion object{
        fun mostrarMenu(){
            val archivoBanco = ManejadorArchivo("src\\main\\kotlin\\datos\\bancos.txt")
            val bancos: ArrayList<Banco> = archivoBanco.cargarDatos()
            var ID_BANCOS: Int = (bancos.maxByOrNull { it.obtenerId() }?.obtenerId() ?: 0) + 1

            //Creamos el menú para el programa
            var opcion: Int

            do {
                val menuBanco = "\n--- Menú Banco ---" +
                        "\n1. Crear Banco"+
                        "\n2. Visualizar bancos"+
                        "\n3. Editar banco por ID"+
                        "\n4. Borrar banco por ID"+
                        "\n5. Realizar operaciones de cuenta bancaria"+
                        "\n6. Salir"+
                        "\nSeleccione una opción: "

                println(menuBanco)
                opcion = readLine()?.toIntOrNull() ?: 0

                when (opcion) {
                    1 -> {
                        println("====Crear Banco====")
                        try {
                            println("Ingrese el nombre del banco: ")
                            val nombreBanco: String = readLine().toString()
                            println("Ingrese la cantidad de accionistas del banco: ")
                            val cantidadAccionistas: Int = readLine()?.toIntOrNull() ?:0
                            println("Ingrese el saldo inicial del banco: ")
                            val saldoInicial: Double = readLine()?.toDoubleOrNull() ?:0.00

                            val banco: Banco = Banco (nombreBanco,ID_BANCOS, cantidadAccionistas, saldoInicial)

                            bancos.add(banco)
                            ID_BANCOS++

                            println("${nombreBanco} creado con éxito...")
                        } catch (e: Exception) {
                            println("Ha ocurrido un error: ${e.message}")
                        }
                    }
                    2 -> {
                        println("====Visualizar Bancos====")
                        bancos.forEach{
                            println("=========================\n${it}")
                        }
                    }
                    3 -> {
                        try {
                            println("====Editar Banco====")
                            println("Ingrese el identificador del banco a editar")
                            val idBanco:Int = readLine()?.toIntOrNull() ?:0
                            editarBanco(bancos,idBanco)
                        } catch (e: Exception) {
                            println("Ha ocurrido un error: ${e.message}")
                        }

                    }
                    4 -> {
                        try {
                            println("====Eliminar Banco====")
                            println("Ingrese el identificador del banco a eliminar")
                            val idBanco:Int = readLine()?.toIntOrNull() ?:0
                            eliminarBanco(bancos,idBanco)
                        } catch (e: Exception) {
                            println("Ha ocurrido un error: ${e.message}")
                        }
                    }
                    5 -> {
                        try {
                            println("====Trabajar con las cuentas de un Banco====")
                            println("Ingrese el identificador del banco a trabajar:")
                            val idBanco:Int = readLine()?.toIntOrNull() ?:0
                            val indiceBanco = existeBanco(bancos, idBanco)
                            MenuCuenta.mostrarMenu(bancos,indiceBanco)
                        } catch (e: Exception) {
                            println("Ha ocurrido un error: ${e.message}")
                        }
                    }
                    6 -> println("Saliendo del programa...")
                    else -> println("Opción inválida. Intente nuevamente.")
                }
            } while (opcion != 6)

            archivoBanco.guardarDatos(bancos)
            println("Programa finalizado.")
        }
        private fun editarBanco(bancos: ArrayList<Banco>, idBanco: Int) {
            val indiceBanco = existeBanco(bancos, idBanco)
            val bancoEncontrado = bancos[indiceBanco]

            var opcionEdicion:Int
            val menuEdicionBanco:String = "\n--- Menú Edicion ---" +
                    "\n1. Editar nombre del Banco"+
                    "\n2. Aumentar accionistas"+
                    "\n3. Disminuir accionistas"+
                    "\n4. Volver al menú principal"+
                    "\nSeleccione una opción: "
            do {
                println(menuEdicionBanco)
                opcionEdicion = readLine()?.toIntOrNull() ?: 0
                when (opcionEdicion) {
                    1 -> {
                        println("====Editar nombre del Banco====")
                        try {
                            println("Ingrese el nombre del banco: ")
                            val nuevoNombreBanco: String = readLine().toString()
                            bancoEncontrado.actualizarNombre(nuevoNombreBanco)
                            println("Nombre actualizado con éxito...")
                        } catch (e: Exception) {
                            println("Ha ocurrido un error: ${e.message}")
                        }
                    }
                    2 -> {
                        println("====Aumentar accionistas al Banco====")
                        try {
                            println("Ingrese la cantidad de accionistas a aumentar : ")
                            val cantidadAccionistas: Int = readLine()?.toIntOrNull() ?:0
                            bancoEncontrado.aumentarAccionista(cantidadAccionistas)
                            println("Cantidad de accionistas actualizado con éxito, se han agregado ${cantidadAccionistas}...")
                        } catch (e: Exception) {
                            println("Ha ocurrido un error: ${e.message}")
                        }
                    }
                    3 -> {
                        println("====Disminuir accionistas al Banco====")
                        try {
                            println("Ingrese la cantidad de accionistas a disminuir : ")
                            val cantidadAccionistas: Int = readLine()?.toIntOrNull() ?:0
                            bancoEncontrado.disminuirAccionista(cantidadAccionistas)
                            println("Cantidad de accionistas actualizado con éxito, se redujo en ${cantidadAccionistas}...")
                        } catch (e: Exception) {
                            println("Ha ocurrido un error: ${e.message}")
                        }
                    }
                    4 -> println("Volviendo al menú principal...")
                    else -> println("Dicha opción no existe")
                }
            }while (opcionEdicion!=4)

            bancos[indiceBanco] = bancoEncontrado
        }

        private fun eliminarBanco(bancos: ArrayList<Banco>, idBanco: Int) {
            val indiceBanco = existeBanco(bancos, idBanco)
            val eliminado: Banco = bancos.removeAt(indiceBanco)
            println("Se ha eliminado el siguiente banco con éxito: \n${eliminado}")
        }

        private fun existeBanco(bancos: ArrayList<Banco>, idBanco: Int): Int {
            val indiceBanco = bancos.indexOfFirst { it.obtenerId() == idBanco }
            if (indiceBanco == -1) throw Exception("No existe el banco")
            return indiceBanco
        }
    }
}