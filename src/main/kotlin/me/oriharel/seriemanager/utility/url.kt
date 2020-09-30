package me.oriharel.seriemanager.utility

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
            override fun deserialize(jp: JsonParser, ctx: DeserializationContext): Instant {
                val node: JsonNode = jp.codec.readTree(jp)
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
    return Mapper.mapper.readValue(URL(this), T::class.java)
}

fun String.getJsonObject(): JSONObject {
    val text = URL(this).readText()
    return JSONObject(text)
}