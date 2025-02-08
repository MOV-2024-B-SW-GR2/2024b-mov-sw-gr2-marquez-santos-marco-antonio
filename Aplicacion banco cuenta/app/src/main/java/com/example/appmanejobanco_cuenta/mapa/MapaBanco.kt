package com.example.appmanejobanco_cuenta.mapa

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmanejobanco_cuenta.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar

class MapaBanco : AppCompatActivity() {
    private lateinit var mapa: GoogleMap
    var permisos = false
    var nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    var nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mapa_banco)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        solicitarPermisos()
        inicializarLogicaMapa()
    }

    fun solicitarPermisos() {
        if (!tengoPermisos()) ActivityCompat.requestPermissions(
            this, arrayOf(nombrePermisoFine, nombrePermisoCoarse),
            1
        )
    }

    fun tengoPermisos(): Boolean {
        val contexto = applicationContext
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        permisos = tienePermisos
        return permisos
    }

    fun inicializarLogicaMapa() {
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.mapa_banco) as SupportMapFragment

        fragmentoMapa?.getMapAsync { googleMap ->
            mapa = googleMap
            establecerConfiguraciones()

            val latitud = intent.getDoubleExtra("latitud",0.00)
            val longitud = intent.getDoubleExtra("longitud",0.00)
            val banco = LatLng(latitud, longitud)

            val titulo = "Banco"
            val marcadorBanco = anadirMarcador(banco, titulo)
            marcadorBanco.tag = titulo
            moverCamaraConZoom(banco)
            anadirPolilinea(latitud,longitud,0.002)
            anadirPoligono(latitud,longitud,0.002)
            mostrarSnackbar("Te encuentras en latitud:${latitud},longitud:${longitud}")
            // Escuchar eventos solo después de inicializar el mapa
            escucharListeners()
        } ?: mostrarSnackbar("Error: No se pudo cargar el mapa")
    }

    @SuppressLint("MissingPermission")
    fun establecerConfiguraciones() {
        with(mapa) {
            if (tengoPermisos()) {
                mapa.isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }

    fun moverCamaraConZoom (latLang: LatLng, zoom:Float=17f){
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang,zoom))
    }

    fun anadirMarcador(latLang: LatLng, title:String): Marker {
        return mapa.addMarker(MarkerOptions().position(latLang).title(title))!!
    }

    fun mostrarSnackbar (texto:String){
        var snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_SHORT
        )
        snack.show()
    }


    fun anadirPolilinea(latitud:Double,longitud:Double,desplazamiento:Double) {
        with(mapa) {
            val polilinea = addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .add(
                        LatLng(latitud + desplazamiento, longitud - desplazamiento),
                        LatLng(latitud + desplazamiento, longitud + desplazamiento),
                        LatLng(latitud - desplazamiento, longitud + desplazamiento),
                        LatLng(latitud - desplazamiento, longitud - desplazamiento),
                        LatLng(latitud + desplazamiento, longitud - desplazamiento)
                    )
            )
            polilinea.tag = "polilinea-uno"
        }
    }
    fun anadirPoligono(latitud:Double,longitud:Double,desplazamiento:Double) {
        with(mapa) {
            val poligono = mapa.addPolygon(
                PolygonOptions()
                    .clickable(true)
                    .add(
                        LatLng(latitud + desplazamiento, longitud - desplazamiento),
                        LatLng(latitud + desplazamiento, longitud + desplazamiento),
                        LatLng(latitud - desplazamiento, longitud + desplazamiento),
                        LatLng(latitud - desplazamiento, longitud - desplazamiento),
                        LatLng(latitud + desplazamiento, longitud - desplazamiento)
                    )
            )
            poligono.tag = "poligono-uno"
        }
    }
    fun escucharListeners() {
        mapa.setOnPolylineClickListener {
            mostrarSnackbar("setOnPolylineClickListener ${it.tag}")
        }
        mapa.setOnPolygonClickListener {
            mostrarSnackbar("setOnPolygonClickListener ${it.tag}")
        }
        mapa.setOnMarkerClickListener {
            mostrarSnackbar("setOnMarkerClickListener ${it.tag}")
            return@setOnMarkerClickListener true
        }
        mapa.setOnCameraIdleListener {
            mostrarSnackbar("setOnCameraIdleListener")
        }
        mapa.setOnCameraMoveStartedListener {
            mostrarSnackbar("setOnCameraMoveStartedListener")
        }
        mapa.setOnCameraMoveListener {
            mostrarSnackbar("setOnCameraMoveListener")
        }
    }
}