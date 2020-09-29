package me.oriharel.seriemanager.utility

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.JSONPObject
import com.fasterxml.jackson.module.kotlin.readValue
import org.json.JSONObject
import java.net.URL


val mapper = ObjectMapper()
fun String.convertURLJsonResponse(): JSONObject {
    val text = URL(this).readText()
    return JSONObject(text)
}