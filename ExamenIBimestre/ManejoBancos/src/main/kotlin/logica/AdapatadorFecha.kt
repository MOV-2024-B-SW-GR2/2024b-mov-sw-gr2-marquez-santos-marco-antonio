package org.example.logica
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AdapatadorFecha : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun serialize(src: LocalDate, tipoDeSrc: Type, contexto: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(formato))
    }

    override fun deserialize(json: JsonElement, tipoDeT: Type, contexto: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString, formato)
    }
}