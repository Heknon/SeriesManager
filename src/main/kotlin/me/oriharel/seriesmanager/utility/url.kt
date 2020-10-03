package me.oriharel.seriesmanager.utility

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.Instant
import org.json.JSONObject
import java.net.URL


object Mapper {
    val mapper = ObjectMapper()

    init {
        val module = SimpleModule()
        module.addDeserializer(Instant::class.java, object : StdDeserializer<Instant>(Instant::class.java) {
            override fun deserialize(jp: JsonParser, ctx: DeserializationContext): Instant? {
                val node: JsonNode = jp.codec.readTree(jp)
                if (node.asText().isBlank()) return Instant.parse("1010-01-01T00:00:00Z")
                return Instant.parse("${node.asText()}T00:00:00Z")
            }
        })

        module.addSerializer(Instant::class.java, object : StdSerializer<Instant>(Instant::class.java) {
            override fun serialize(instant: Instant, j: JsonGenerator, sp: SerializerProvider?) {
                j.writeStartObject()
                j.writeStringField("iso", instant.toString())
                j.writeNumberField("epoch", instant.epochSeconds)
                j.writeEndObject()
            }

        })
        mapper.registerModule(module)
    }
}

inline fun <reified T> String.convertURLJsonResponse(): T {
    println(this)
    return Mapper.mapper.readValue(URL(this), T::class.java)
}

fun String.getJsonObject(): JSONObject {
    val text = URL(this.replace(" ", "%20")).readText()
    return JSONObject(text)
}