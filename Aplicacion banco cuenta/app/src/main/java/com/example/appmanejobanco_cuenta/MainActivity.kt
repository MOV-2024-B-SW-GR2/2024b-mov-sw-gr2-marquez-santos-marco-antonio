package com.example.appmanejobanco_cuenta

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmanejobanco_cuenta.basedatos.BaseDeDatos
import com.example.appmanejobanco_cuenta.basedatos.SqliteHelperBanco
import com.example.appmanejobanco_cuenta.operacionesbanco.BancoCRUD

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.principal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicializar BDD
        BaseDeDatos.tablaBanco = SqliteHelperBanco(this)

        val botonIniciarApp =findViewById<Button>(R.id.btn_iniciar_aplicacion)
        botonIniciarApp.setOnClickListener {
            irActividad(BancoCRUD::class.java)
        }
    }

    fun irActividad(clase:Class<*>){
        startActivity(Intent(this,clase))
    }
}