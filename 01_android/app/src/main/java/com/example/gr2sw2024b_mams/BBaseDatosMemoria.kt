package com.example.gr2sw2024b_mams

class BBaseDatosMemoria {
    companion object{
        var arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Marco","a@a.com"))
            arregloBEntrenador.add(BEntrenador(2,"Antonio","b@a.com"))
            arregloBEntrenador.add(BEntrenador(3,"Jose","c@a.com"))
        }
    }
}