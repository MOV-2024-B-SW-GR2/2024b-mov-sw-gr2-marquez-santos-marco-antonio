package org.example.ui

import org.example.logica.Banco
import org.example.logica.Cuenta

class MenuCuenta {
    companion object {
        fun mostrarMenu(bancos: ArrayList<Banco>, idBanco: Int) {
            val bancoEncontrado = bancos[idBanco]
            var opcionMenuCuenta: Int
            val menuGestionCuenta:String = "\n--- Menú gestión cuentas ---" +
                    "\n1. Crear cuenta"+
                    "\n2. Visualizar cuentas"+
                    "\n3. Operaciones en cuenta"+
                    "\n4. Borrar cuenta"+
                    "\n5. Volver al menú de bancos"+
                    "\nSeleccione una opción: "

            do{
                println(menuGestionCuenta)
                opcionMenuCuenta = readLine()?.toIntOrNull() ?: 0
                when (opcionMenuCuenta) {
                    1 -> crearCuenta(bancoEncontrado)
                    2 -> visualizarCuentas(bancoEncontrado)
                    3 -> actualizarCuenta(bancoEncontrado)
                    4 -> borrarCuenta(bancoEncontrado)
                    5 -> println("Volviendo al menú principal...")
                    else -> println("La opcion no existe")
                }
            }while (opcionMenuCuenta!=5)

            bancos[idBanco] = bancoEncontrado
        }

        private fun actualizarCuenta(banco: Banco) {
            try {
                println("====Operaciones en Cuenta====")
                println("Ingrese el identificador de la cuenta a modificar: ")
                val numeroCuenta:Int = readLine()?.toIntOrNull() ?:0
                val cuentas = banco.obtenerCuentas()
                val indiceCuenta = existeCuenta(cuentas,numeroCuenta)

                val cuentaEncontrada = cuentas[indiceCuenta]
                var opcionEdicion:Int
                val menuEdicionBanco:String = "\n--- Menú Edicion ---" +
                        "\n1. Editar nombre del propietario"+
                        "\n2. Depositar"+
                        "\n3. Retirar"+
                        "\n4. Transferir"+
                        "\n5. Volver al menú de cuenta"+
                        "\nSeleccione una opción: "
                do {
                    println(menuEdicionBanco)
                    opcionEdicion = readLine()?.toIntOrNull() ?: 0
                    when (opcionEdicion) {
                        1 -> {
                            println("====Editar nombre del propietario====")
                            try {
                                println("Ingrese el nombre del propietario: ")
                                val nuevoNombrePropietario: String = readLine().toString()
                                cuentaEncontrada.actualizarNombrePropietario(nuevoNombrePropietario)
                                println("Nombre actualizado con éxito...")
                            } catch (e: Exception) {
                                println("Ha ocurrido un error: ${e.message}")
                            }
                        }
                        2 -> {
                            println("====Realizar deposito====")
                            try {
                                println("Ingrese la cantidad a depositar: ")
                                val cantidadADepositar: Double = readLine()?.toDoubleOrNull() ?:0.00
                                banco.depositarEnCuenta(cuentaEncontrada.obtenerNumeroCuenta(),cantidadADepositar)
                                println("Deposito realizado con éxito, se han agregado $${cantidadADepositar}...")
                            } catch (e: Exception) {
                                println("Ha ocurrido un error: ${e.message}")
                            }
                        }
                        3 -> {
                            println("====Realizar retiro====")
                            try {
                                println("Ingrese la cantidad a retirar: ")
                                val cantidadARetirar: Double = readLine()?.toDoubleOrNull() ?:0.00
                                banco.retirarDeCuenta(cuentaEncontrada.obtenerNumeroCuenta(),cantidadARetirar)
                                println("Retiro realizado con éxito, se han descontado $${cantidadARetirar}...")
                            } catch (e: Exception) {
                                println("Ha ocurrido un error: ${e.message}")
                            }
                        }
                        4 -> {
                            println("====Realizar transferencia====")
                            try {
                                println("Ingrese la cantidad a transferir: ")
                                val cantidadATransferir: Double = readLine()?.toDoubleOrNull() ?:0.00
                                println("Ingrese el número de la cuenta receptora de la transferencia: ")
                                val numeroCuentaTransferir = readLine()?.toIntOrNull() ?:0

                                val indiceCuentaTransferido = existeCuenta(cuentas,numeroCuentaTransferir)
                                val cuentaEncontradaTransferido = cuentas[indiceCuentaTransferido]

                                banco.transferir(cuentaEncontrada.obtenerNumeroCuenta(),cuentaEncontradaTransferido.obtenerNumeroCuenta(),cantidadATransferir)
                                cuentas[indiceCuentaTransferido] = cuentaEncontradaTransferido
                                println("Transferencia realizado con éxito, se han descontado $${cantidadATransferir}...")
                            } catch (e: Exception) {
                                println("Ha ocurrido un error: ${e.message}")
                            }
                        }
                        5 -> println("Volviendo al menú principal...")
                        else -> println("Dicha opción no existe")
                    }
                }while (opcionEdicion!=5)

                cuentas[indiceCuenta] = cuentaEncontrada
                println("Cuenta actualizada con éxito...")
            } catch (e: Exception) {
                println("Ha ocurrido un error: ${e.message}")
            }
        }

        private fun borrarCuenta(banco: Banco) {
            try {
                println("====Eliminar Cuenta====")
                println("Ingrese el identificador de la cuenta a eliminar")
                val numeroCuenta:Int = readLine()?.toIntOrNull() ?:0
                val indiceCuentaArreglo = existeCuenta(banco.obtenerCuentas(),numeroCuenta)
                val eliminado: Cuenta = banco.obtenerCuentas().removeAt(indiceCuentaArreglo)
                println("Se ha eliminado la siguiente cuenta con éxito: \n${eliminado}")
            } catch (e: Exception) {
                println("Ha ocurrido un error: ${e.message}")
            }
        }

        private fun visualizarCuentas(banco: Banco) {
            println("====Visualizar cuentas====")
            banco.obtenerCuentas().forEach{
                println("=========================\n${it}")
            }
        }

        private fun crearCuenta(banco: Banco) {
            println("==== Crear nueva cuenta ====")
            try {
                print("Ingrese el nombre del propietario: ")
                val nombrePropietario = readLine().orEmpty()
                banco.crearCuenta(nombrePropietario)
                println("Cuenta creada con éxito...")
            } catch (e: Exception) {
                println("Ha ocurrido un error: ${e.message}")
            }
        }
        private fun existeCuenta(cuentas: ArrayList<Cuenta>, numeroCuenta: Int): Int {
            val indiceCuentaArreglo = cuentas.indexOfFirst { it.obtenerNumeroCuenta() == numeroCuenta }
            if (indiceCuentaArreglo == -1) throw Exception("No existe la cuenta...")
            return indiceCuentaArreglo
        }
    }

}
