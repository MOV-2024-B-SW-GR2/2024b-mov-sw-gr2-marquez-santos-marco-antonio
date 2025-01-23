package com.example.appmanejobanco_cuenta.operacionesbanco

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmanejobanco_cuenta.BaseDatosMemoria
import com.example.appmanejobanco_cuenta.R
import com.google.android.material.snackbar.Snackbar

class EditarBanco : AppCompatActivity() {
    val bancos = BaseDatosMemoria.arregloBancos
    var multiplicador = 1
    var banco = bancos[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_banco)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.view_editar_banco)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idBanco = intent.getIntExtra("idBanco",0)
        banco = bancos[idBanco]

        val nombreBanco = findViewById<EditText>(R.id.input_nuevo_nombre_banco)
        nombreBanco.setText(banco.nombre)

        val numeroAccionistas = findViewById<EditText>(R.id.input_cambiar_numero_accionistas_banco)
        numeroAccionistas.visibility = View.INVISIBLE

        val botonEditarBanco = findViewById<Button>(R.id.btn_editar_banco_opcion)
        botonEditarBanco.setOnClickListener { editarBanco() }

        val aumentarAccionistas = findViewById<CheckBox>(R.id.cb_aumentar_accionistas)
        val disminuirAccionistas = findViewById<CheckBox>(R.id.cb_reducir_accionistas)


        // Listener para el checkbox de aumentar accionistas
        aumentarAccionistas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                multiplicador = 1
                disminuirAccionistas.isChecked = false  // Desmarcar el otro checkbox
                numeroAccionistas.visibility = View.VISIBLE
            } else {numeroAccionistas.visibility = View.INVISIBLE}
        }

        // Listener para el checkbox de disminuir accionistas
        disminuirAccionistas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                multiplicador = -1
                aumentarAccionistas.isChecked = false  // Desmarcar el otro checkbox
                numeroAccionistas.visibility = View.VISIBLE
            } else {numeroAccionistas.visibility = View.INVISIBLE}
        }
    }

    private fun editarBanco() {
        try {
            val nuevoNombreBanco = findViewById<EditText>(R.id.input_nuevo_nombre_banco)
            val accionistas = findViewById<EditText>(R.id.input_cambiar_numero_accionistas_banco)
            if (accionistas.visibility === View.INVISIBLE) {
                accionistas.setText("0")
                banco.aumentarAccionista(accionistas.text.toString().toInt())
            }
            if (multiplicador > 0){
                val aumentarAccionistas = accionistas.text.toString().toInt()
                banco.aumentarAccionista(aumentarAccionistas)
            }
            if (multiplicador < 0){
                val disminuirAccionistas = accionistas.text.toString().toInt()
                banco.disminuirAccionista(disminuirAccionistas)
            }

            banco.actualizarNombre(nuevoNombreBanco.text.toString())

            setResult(Activity.RESULT_OK)
            finish()
        } catch (e: Exception) {
            mostrarSnackbar("Ha ocurrido un error: ${e.message}")
        }
    }

    fun mostrarSnackbar (texto:String){
        var snack = Snackbar.make(
            findViewById(R.id.view_editar_banco),
            texto,
            Snackbar.LENGTH_SHORT
        )
        snack.show()
    }
}