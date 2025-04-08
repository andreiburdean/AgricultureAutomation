package com.example.agricultureautomationapp.models

import com.google.gson.*
import java.lang.reflect.Type

class ProgramItemDeserializer : JsonDeserializer<ProgramItem> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ProgramItem {
        val jsonObject = json.asJsonObject

        val programId = if (jsonObject.has("programId") && !jsonObject.get("programId").isJsonNull)
            jsonObject.get("programId").asInt else null
        val programName = if (jsonObject.has("programName")) jsonObject.get("programName").asString else ""
        val status = if (jsonObject.has("status") && !jsonObject.get("status").isJsonNull)
            jsonObject.get("status").asInt else null
        val temperature = if (jsonObject.has("temperature") && !jsonObject.get("temperature").isJsonNull)
            jsonObject.get("temperature").asDouble else null
        val humidity = if (jsonObject.has("humidity") && !jsonObject.get("humidity").isJsonNull)
            jsonObject.get("humidity").asDouble else null
        val luminosity = if (jsonObject.has("luminosity") && !jsonObject.get("luminosity").isJsonNull)
            jsonObject.get("luminosity").asDouble else null

        // Extragem programTypeId din obiectul "programType"
        var programTypeId = 0
        if (jsonObject.has("programType") && jsonObject.get("programType").isJsonObject) {
            val programTypeObj = jsonObject.getAsJsonObject("programType")
            if (programTypeObj.has("programTypeId") && !programTypeObj.get("programTypeId").isJsonNull) {
                programTypeId = programTypeObj.get("programTypeId").asInt
            }
        }

        return ProgramItem(programId, programTypeId, programName, status, temperature, humidity, luminosity)
    }
}
