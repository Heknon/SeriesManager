package me.oriharel.seriemanager.utility

import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import java.net.URL


val mapper = ObjectMapper()
fun String.convertURLJsonResponse(): JSONObject {
    val text = URL(this).readText()
    return JSONObject(text)
}