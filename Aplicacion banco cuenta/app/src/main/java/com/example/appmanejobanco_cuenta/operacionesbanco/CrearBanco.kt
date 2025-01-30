package com.example.appmanejobanco_cuenta.operacionesbanco

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmanejobanco_cuenta.Banco
import com.example.appmanejobanco_cuenta.R
import com.example.appmanejobanco_cuenta.basedatos.BaseDeDatos
import com.google.android.material.snackbar.Snackbar

class CrearBanco : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_banco)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_crear_banco)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonCrearBanco = findViewById<Button>(R.id.btn_editar_banco_opcion)
        botonCrearBanco.setOnClickListener {
            try {
                val nombre = findViewById<EditText>(R.id.input_nuevo_nombre_banco)
                val numeroAccionistas = findViewById<EditText>(R.id.input_cambiar_numero_accionistas_banco)
                val saldoInicial = findViewById<EditText>(R.id.input_saldo_inicial_banco)

                val banco: Banco = Banco (
                    nombre.text.toString(),
                    1,
                    numeroAccionistas.text.toString().toInt(),
                    saldoInicial.text.toString().toDouble())

                BaseDeDatos.tablaBanco!!.crearBanco(banco.nombre,banco.numeroAccionistas,(banco.saldoTotal*100).toInt(),recuperarNumero(banco.enOperacion))
                setResult(Activity.RESULT_OK)
                finish()
            } catch (e:Exception){
                mostrarSnackbar("Ha ocurrido un error: ${e.message}")
            }
        }
    }

    private fun recuperarNumero(enOperacion: Boolean): Int {
        return if (enOperacion) 1 else 0
    }

    fun mostrarSnackbar (texto:String){
        var snack = Snackbar.make(
            findViewById(R.id.view_crear_banco),
            texto,
            Snackbar.LENGTH_SHORT
        )
        snack.show()
    }
}