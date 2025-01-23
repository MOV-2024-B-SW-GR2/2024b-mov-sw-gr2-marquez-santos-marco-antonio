package com.example.appmanejobanco_cuenta

class BaseDatosMemoria {

    companion object{
        var arregloBancos = arrayListOf<Banco>()
        var ID_BANCOS: Int = (arregloBancos.maxByOrNull { it.obtenerId() }?.obtenerId() ?: 0) + 1
        init {
            arregloBancos.add(Banco("Banco Austro",1, 5, 2500.00))
            ID_BANCOS ++
        }
    }
}
