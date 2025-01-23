package com.example.gr2sw2024b_mams

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_ciclo_vida)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val botonCicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        botonCicloVida.setOnClickListener {
            irActividad(ACicloVida::class.java)
        }

        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonListView.setOnClickListener {
            irActividad(BListView::class.java)
        }
        val botonSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonSqlite.setOnClickListener {
            irActividad(ECrudEntrenador::class.java)
        }

        val botonImplicito = findViewById<Button>(R.id.btn_ir_intent_implicito)
        botonImplicito.setOnClickListener {
            val intentConRespuesta = Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            )
            callbackContenidoIntentImplicito.launch(intentConRespuesta)
        }

        val botonExplicito = findViewById<Button>(R.id.btn_ir_intent_implicito)
        botonExplicito.setOnClickListener {
            val intentExplicito = Intent(
                this, clienteExplicitoParametros::class.java
            )
            intentExplicito.putExtra("Nombre","Marco")
            intentExplicito.putExtra("Apellido","Marquez")
            intentExplicito.putExtra("edad",22)
            intentExplicito.putExtra("entrenador", BEntrenador(1,"Marco","ejemplo"))
            callbackContenidoIntentExplicito.launch(intentExplicito)
        }
    }

    fun irActividad(clase:Class<*>){
        startActivity(Intent(this,clase))
    }

    fun mostrarSnackbar (texto:String){
        var snack = Snackbar.make(
            findViewById(R.id.cl_ciclo_vida),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    val callbackContenidoIntentExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == Activity.RESULT_OK){
            if (result.data != null){
                val data = result.data
                mostrarSnackbar("${data?.getStringExtra("nombreModificado")}")
            }
        }
    }
    val callbackContenidoIntentImplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == Activity.RESULT_OK){
            if (result.data != null){
                if (result.data!!.data != null){
                    var uri: Uri = result.data!!.data!!
                    val cursor = contentResolver.query(
                        uri, null, null, null, null
                    )
                    cursor?.moveToFirst()
                    val indiceTelefono = cursor?.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                    val telefono = cursor?.getString(indiceTelefono!!)
                    cursor?.close()
                    mostrarSnackbar("telefono ${telefono}")
                }
            }
        }
    }
}