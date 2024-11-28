package org.example

import java.util.*

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val inmutable: String="Marco";
    //inmutable = "Antonio"; //ERROR

    var mutable: String="Ricardo";
    mutable = "Gabriel"; //PERMITE RE ASIGNARLE UN VALOR

    //DUCK TYPING
    val ejemploVariable = "Marco Marquez";
    ejemploVariable.trim();
    //Variables primitivas
    val edadEjemplo: Int = 21;
    val nombreProfesor:String="Adrian Eguez";
    val sueldo:Double=1.2;
    val estadoCivil:Char='S';
    val mayorEdad:Boolean=true;
    //Clases
    val fechaNacimiento: Date =Date();

    //Estructura when
    val estadoCivilWhen='C';
    when(estadoCivilWhen){
        ('C')->{
            //println("Casado")
        }
        'S' -> {
            //println("Soltero")
        }
        else -> {
            println("No se")
        }
    }

    //IF ELSE EN UNA SOLA LINEA
    val esSoltero = (estadoCivilWhen=='S');
    val coqueteo = if(esSoltero) "Si" else "No"


    //imprimirNombre("Marco");

    //Llamado a la funcion con parametro requerido
    calcularSueldo(10.00);
    //Parametro requerido y sobreescribir los opcionales
    calcularSueldo(10.00,15.00,20.00);
    //Usando parametros nombrados
    calcularSueldo(bonoEspecial = 20.00, sueldo=10.00, tasa=15.00);

    //Vamos a llamar a la clase suma con sus diferentes constructores
    val sumaA = Suma(1,1)
    val sumaB = Suma(null,1)
    val sumaC = Suma(1,null)
    val sumaD = Suma(null,null)

    sumaA.sumar()
    sumaB.sumar()
    sumaC.sumar()
    sumaD.sumar()

    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)


    //Arreglos
    //Estaticos
    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3)
    println(arregloEstatico)
    //Dinamicas
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(1,2,3)
    println(arregloDinamico)

    //FOR EACH => Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach{valorActual:Int ->
            println("Valor actual: ${valorActual}")
        }

    // Valor actual con it para elemento iterado
    arregloDinamico.forEach{println("Valor actual: ${it}")}

    //MAP -> MUTA(Modifica cambia) el arreglo
    //1) Enviamos el nuevo valor de la iteracion
    //2) Nos devuelve un NUEVO ARREGLO con valores
    // de las iteraciones
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual:Int ->
            return@map valorActual.toDouble() + 100.00
        }
    println(respuestaMap)

    val respuestaMapDos = arregloDinamico.map { it + 15 }
    println(arregloDinamico)
    println(respuestaMapDos)

    //Filter = Filtrar el arreglo
    //1) Devolver una expresion True o False
    //2) Nuevo arreglo filtrado
    val respuestaFilter: List<Int> = arregloDinamico
        .filter { valorActual:Int ->
            //Expresion o condicion
            val mayoresACinco: Boolean = valorActual>5
            return@filter mayoresACinco
        }

    val respuestaFilterDos = arregloDinamico.filter { it <=5 }

    println(respuestaFilter)
    println(respuestaFilterDos)

    //OR / AND
    //OR -> ANY (ALGUNO CUMPLE?)
    //AND -> ALL (TODOS CUMPLEN)
    val respuestaAny: Boolean = arregloDinamico
        .any { valorActual:Int ->
            return@any (valorActual>2)
        }
    val respuestaAll: Boolean = arregloDinamico
        .all { valorActual:Int ->
            return@all (valorActual>2)
        }

    println(respuestaAny)
    println(respuestaAll)

    //Funcion reduce - > valor acumulado
    val respuestaReduce:Int = arregloDinamico
        .reduce { acumulado:Int, valorActual:Int ->
            return@reduce (acumulado+valorActual)
        }

    println(respuestaReduce)
}

fun imprimirNombre(nombre:String):Unit{
    fun otraFuncion(){
        println("Otra función adentro");
    }
    println("Nombre: ${nombre}"); //Template string
}

fun calcularSueldo(
    sueldo: Double, //Requerido
    tasa: Double = 12.00, //Opcional (por defecto)
    bonoEspecial: Double?=null //Opcional (nullable)
):Double{
    //Int -> Int?(nullable)
    if(bonoEspecial==null){
        return sueldo*(100/tasa);
    }else{
        return sueldo * (100/tasa) * bonoEspecial;
    }
}

abstract class NumerosJava {
    protected val numeroUno:Int
    private val numeroDos: Int

    constructor(
        uno:Int,
        dos:Int
    ) {
        this.numeroUno = uno;
        this.numeroDos = dos;
        println("Inicializando")
    }
}

abstract class Numeros(
    //Contructor primario
    protected val numeroUno:Int,//Parametro-propiedad de clase
    protected val numeroDos:Int,
    parametroNoAtributoDeClase:Int? = null //Parametro normal
){
    init {
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

class Suma (
    //Constructor primario
    parametroUno:Int, //Parametro normal
    parametroDos:Int //Parametro normal
):Numeros( //Clase padre
    parametroUno,
    parametroDos
){
    public val soyPublicoExplicito: String = "Publicas"
    val soyPublicoImplicito: String = "Publico Implicito"
    init {
        this.numeroUno
        this.numeroDos
        //numeroUno   //this es opcional
        //numeroDos
        this.soyPublicoImplicito
        soyPublicoExplicito
    }

    //Constructores secundarios
    constructor(
        uno: Int?,
        dos: Int
    ):this(
        if (uno==null)0 else uno,
        dos
    ){}
    constructor(
        uno: Int,
        dos: Int?
    ):this(
        uno,
        if(dos==null) 0 else dos
    ){}
    constructor(
        uno: Int?,
        dos: Int?
    ):this(
        if (uno==null)0 else uno,
        if(dos==null) 0 else dos
    ){}

    fun sumar (): Int {
        val total = numeroUno + numeroDos
        agregarHistorial(total)
        return total
    }

    companion object { //Comparte para todas las instancias de la clase
        //funciones, variables
        val pi = 3.14
        fun elevarAlCuadrado(num:Int):Int{return num*num}

        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial (valorTotalSuma:Int){
            historialSumas.add(valorTotalSuma)
        }
    }

}

