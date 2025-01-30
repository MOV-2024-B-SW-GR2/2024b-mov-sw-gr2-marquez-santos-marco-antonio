package com.example.gr2sw2024b_mams

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.oAuthProvider

class HFirebaseUiAuth : AppCompatActivity() {

    private val respuestaLoginAuth = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){
        res: FirebaseAuthUIAuthenticationResult ->
        if (res.resultCode == RESULT_OK) {
            if (res.idpResponse != null) {
                seLogeo(res.idpResponse!!)
            }
        }
    }

    private fun seLogeo(response: IdpResponse) {
        val nombre = FirebaseAuth.getInstance().currentUser?.displayName
        cambiarInterfaz(View.INVISIBLE,View.VISIBLE,nombre!!)
        if (response.isNewUser == true){
            registrarUsuaioPorPrimeraVez(response)
        }
    }

    private fun registrarUsuaioPorPrimeraVez(usuario: IdpResponse) {
    }

    fun cambiarInterfaz(
        visibilidadLogin:Int= View.VISIBLE,
        visibilidadlogout:Int = View.INVISIBLE,
        textoTextView:String = ""){
        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
        val tvBienvenida = findViewById<TextView>(R.id.tv_bienvenido)
        btnLogin.visibility = visibilidadLogin
        btnLogout.visibility = visibilidadlogout
        tvBienvenida.text = textoTextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hfirebase_ui_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        btnLogin.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )
            val logearseIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build()
            respuestaLoginAuth.launch(logearseIntent)
        }

        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
        btnLogout.setOnClickListener {
            cambiarInterfaz(View.VISIBLE,View.INVISIBLE,"Bienvenido")
            FirebaseAuth.getInstance().signOut()
        }

        val usuario = FirebaseAuth.getInstance().currentUser
        if (usuario!=null){
            cambiarInterfaz(View.INVISIBLE,View.VISIBLE,usuario.displayName!!)
        }

    }
}