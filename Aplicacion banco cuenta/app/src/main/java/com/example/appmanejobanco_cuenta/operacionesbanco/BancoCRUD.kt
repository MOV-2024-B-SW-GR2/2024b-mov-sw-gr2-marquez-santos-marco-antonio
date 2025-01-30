package com.example.appmanejobanco_cuenta.operacionesbanco

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmanejobanco_cuenta.Banco
import com.example.appmanejobanco_cuenta.R
import com.example.appmanejobanco_cuenta.basedatos.BaseDeDatos
import com.example.appmanejobanco_cuenta.operacionescuenta.CuentaCRUD
import com.google.android.material.snackbar.Snackbar

class BancoCRUD : AppCompatActivity() {
    var bancos = arrayListOf<Banco>()
    var idSeleccionado = -1
    var adaptador: ArrayAdapter<Banco>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_banco_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_crud_banco)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.lv_bancos)
        adaptador = ArrayAdapter(
        this,
        android.R.layout.simple_list_item_1,
            bancos
        )
        listView.adapter = adaptador
        actualizarListaBancos()

        val botonCrearBanco = findViewById<Button>(R.id.btn_crear_banco)
        botonCrearBanco.setOnClickListener { aniadirBanco() }
        registerForContextMenu(listView)
    }

    private fun actualizarListaBancos() {
        bancos.clear()
        bancos.addAll(BaseDeDatos.tablaBanco!!.consultarBancos())
        adaptador?.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //llenamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_banco, menu)
        //obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        val bancoSeleccionado = adaptador!!.getItem(posicion)
        idSeleccionado = bancoSeleccionado!!.id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editar_banco -> {
                editarBanco()
                return true
            }

            R.id.borrar_banco -> {
                abrirDialogo()
                return true
            }

            R.id.utilizar_cuenta_bancaria -> {
                val intent = Intent(this, CuentaCRUD::class.java)
                intent.putExtra("idBanco",idSeleccionado)
                startActivity(intent)
                actualizarListaBancos()
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun editarBanco() {
        val intent = Intent(this, EditarBanco::class.java)
        intent.putExtra("idBanco",idSeleccionado)
        startActivityForResult(intent,2)
    }

    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea Eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                eliminarBanco()
            }
        )
        builder.setNegativeButton(
            "Cancelar",
            null
        )
        val dialogo = builder.create()
        dialogo.show()
    }

    private fun eliminarBanco() {
        BaseDeDatos.tablaBanco!!.eliminarBanco(idSeleccionado)
        actualizarListaBancos()
        mostrarSnackbar("Se ha eliminado el banco con éxito")
    }

    fun aniadirBanco() {
        val intent = Intent(this, CrearBanco::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Verificar que el código de solicitud y el resultado sean correctos
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            actualizarListaBancos()
            mostrarSnackbar("Se ha creado con éxito el banco")
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            actualizarListaBancos()
            mostrarSnackbar("Se ha editado con éxito el banco")
        }
    }

    fun mostrarSnackbar(texto: String) {
        var snack = Snackbar.make(
            findViewById(R.id.view_crud_banco),
            texto,
            Snackbar.LENGTH_SHORT
        )
        snack.show()
    }
}