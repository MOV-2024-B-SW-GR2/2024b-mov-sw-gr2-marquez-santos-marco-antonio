package com.example.appmanejobanco_cuenta.operacionescuenta

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmanejobanco_cuenta.BaseDatosMemoria
import com.example.appmanejobanco_cuenta.Cuenta
import com.example.appmanejobanco_cuenta.R
import com.google.android.material.snackbar.Snackbar

class CuentaCRUD : AppCompatActivity() {
    val bancos = BaseDatosMemoria.arregloBancos
    var banco = bancos[0]
    var posicionCuentaSeleccionado = -1
    var adaptador: ArrayAdapter<Cuenta>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cuenta_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_cuenta_crud)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val idBanco = intent.getIntExtra("idBanco", 0)
        banco = bancos[idBanco]

        val nombreBanco = findViewById<TextView>(R.id.tv_nombre_banco_seleccionado)
        nombreBanco.setText(banco.nombre)

        val listView = findViewById<ListView>(R.id.lv_cuentas)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            banco.cuentas
        )
        listView.adapter = adaptador
        adaptador?.notifyDataSetChanged()

        val botonCrearBanco = findViewById<Button>(R.id.btn_crear_cuenta)
        botonCrearBanco.setOnClickListener { crearCuenta() }
        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_cuenta, menu)
        //obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionCuentaSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editar_cuenta -> {
                editarCuenta()
                return true
            }

            R.id.depositar_cuenta -> {
                depositarEnLaCuenta()
                return true
            }

            R.id.retirar_cuenta -> {
                retirarDeLaCuenta()
                return true
            }

            R.id.transferir_cuenta -> {
                trasnferirAOtraCuenta()
                return true
            }

            R.id.borrar_cuenta -> {
                eliminarCuenta()
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun trasnferirAOtraCuenta() {
        val transferencia = EditText(this)
        val cuentaDestino = EditText(this)
        transferencia.hint = "Ingrese la cantidad a transferir (ej. 0.0)"
        cuentaDestino.hint = "Ingrese el número de la cuenta (ej. 12345)"
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)
        layout.addView(transferencia)
        layout.addView(cuentaDestino)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Realizar retiro")
        builder.setMessage("Ingrese la cantidad a transferir y el identificador de la cuenta de destino:")
        builder.setView(layout)

        builder.setPositiveButton(
            "Tranferir",
            DialogInterface.OnClickListener { dialog, which ->
                try {
                    val cantidadATransferir = transferencia.text.toString().toDouble()
                    val numeroCuentaTransferir = cuentaDestino.text.toString().toInt()

                    val cuentaDestinoTranferencia = banco.obtenerCuentaPorId(numeroCuentaTransferir)

                    banco.transferir(banco.obtenerCuentas()[posicionCuentaSeleccionado].numeroCuenta,cuentaDestinoTranferencia.obtenerNumeroCuenta(),cantidadATransferir)
                    adaptador?.notifyDataSetChanged()
                    mostrarSnackbar("Transferencia realizado con éxito, se han descontado $${cantidadATransferir}...")
                } catch (e: Exception) {
                    mostrarSnackbar("Ha ocurrido un error: ${e.message}")
                }
            }
        )

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }

    fun retirarDeLaCuenta() {
        val editText = EditText(this)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Realizar retiro")
        builder.setMessage("Ingrese la cantidad a retirar:")

        builder.setView(editText)

        builder.setPositiveButton(
            "Retirar",
            DialogInterface.OnClickListener { dialog, which ->
                try {
                    val cantidadARetirar = editText.text.toString().toDouble()
                    banco.retirarDeCuenta(banco.obtenerCuentas()[posicionCuentaSeleccionado].obtenerNumeroCuenta(),cantidadARetirar)
                    adaptador?.notifyDataSetChanged()
                    mostrarSnackbar("Retiro realizado con éxito, se han descontado $${cantidadARetirar}...")
                } catch (e: Exception) {
                    mostrarSnackbar("Ha ocurrido un error: ${e.message}")
                }
            }
        )

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }

    fun depositarEnLaCuenta() {
        val editText = EditText(this)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Realizar deposito")
        builder.setMessage("Ingrese la cantidad a depositar:")

        builder.setView(editText)

        builder.setPositiveButton(
            "Depositar",
            DialogInterface.OnClickListener { dialog, which ->
                try {
                    val cantidadADepositar = editText.text.toString().toDouble()
                    banco.depositarEnCuenta(banco.obtenerCuentas()[posicionCuentaSeleccionado].obtenerNumeroCuenta(),cantidadADepositar)
                    adaptador?.notifyDataSetChanged()
                    mostrarSnackbar("Deposito realizado con éxito, se han agregado $${cantidadADepositar}...")
                } catch (e: Exception) {
                    mostrarSnackbar("Ha ocurrido un error: ${e.message}")
                }
            }
        )

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }

    fun eliminarCuenta() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar cuenta")
        builder.setMessage("¿Desea eliminar la cuenta?")

        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                try {
                    val eliminado: Cuenta = banco.obtenerCuentas().removeAt(posicionCuentaSeleccionado)
                    adaptador?.notifyDataSetChanged()

                    mostrarSnackbar("Se ha eliminado la siguiente cuenta con éxito: \n${eliminado}")
                } catch (e: Exception) {
                    mostrarSnackbar("Ha ocurrido un error: ${e.message}")
                }
            }
        )

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }

    fun crearCuenta() {
        val editText = EditText(this)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Crear cuenta")
        builder.setMessage("Ingrese el nombre del propietario de la cuenta:")

        builder.setView(editText)

        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                val nombrePropietario = editText.text.toString()
                try {
                    banco.crearCuenta(nombrePropietario)
                    adaptador?.notifyDataSetChanged()
                    mostrarSnackbar("Cuenta creada con éxito a nombre de $nombrePropietario")
                } catch (e: Exception) {
                    mostrarSnackbar("Ha ocurrido un error: ${e.message}")
                }
            }
        )

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }

    fun editarCuenta() {
        val editText = EditText(this)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar cuenta")
        builder.setMessage("Ingrese el nuevo nombre del propietario:")

        builder.setView(editText)

        builder.setPositiveButton(
            "Actualizar nombre",
            DialogInterface.OnClickListener { dialog, which ->
                val nuevoNombrePropietario = editText.text.toString()
                try {
                    var cuenta = banco.cuentas[posicionCuentaSeleccionado]
                    cuenta.actualizarNombrePropietario(nuevoNombrePropietario)
                    adaptador?.notifyDataSetChanged()
                    mostrarSnackbar("Cuenta actualizada a nombre de $nuevoNombrePropietario")
                } catch (e: Exception) {
                    mostrarSnackbar("Ha ocurrido un error: ${e.message}")
                }
            }
        )

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }

    fun mostrarSnackbar(texto: String) {
        var snack = Snackbar.make(
            findViewById(R.id.view_cuenta_crud),
            texto,
            Snackbar.LENGTH_SHORT
        )
        snack.show()
    }
}