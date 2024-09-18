package com.example.fridaycookie.network

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

import com.google.gson.*
import java.lang.reflect.Type

class ScoreDeserializer : JsonDeserializer<CookieScore> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CookieScore {
        val jsonObject = json.asJsonObject
        val location = jsonObject.get("Location").asString
        val scoreElement = jsonObject.get("Score")

        // Handle both string and numeric scores
        val score = if (scoreElement.isJsonPrimitive && scoreElement.asJsonPrimitive.isNumber) {
            scoreElement.asDouble
        } else {
            scoreElement.asString.toDoubleOrNull() ?: 0.0 // Fallback to 0.0 if conversion fails
        }
        return CookieScore(location = location, score = score)
    }
}

class FridayCookieDeserializer : JsonDeserializer<FridayCookie> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): FridayCookie {
        val jsonObject = json.asJsonObject
        return FridayCookie(
            cookie = jsonObject.get("Kaka").asString,
            cookieScores = listOf(CookieScore("ignore", 0.0)),//context.deserialize(jsonObject.get("Scores"), CookieScore::class.java),
            url = jsonObject.get("URL").asString,
            week = jsonObject.get("Week").asInt,
            year = jsonObject.get("Year").asInt
        )
    }
}


@Serializable
data class FridayCookie (
    val cookie: String,
    val cookieScores: List<CookieScore>,
    val url: String,
    val week: Int,
    val year: Int
)

@Serializable
data class CookieScore (
    val location: String,
    val score: Double,
)

data class CookieOfTheWeek (
    val name: String,
    val url: String
)

data class CookieWeek (
    val weekNumber: Int,
    val cookieOfTheWeek: CookieOfTheWeek
)

data class CookieYear (
    val yearNumber: Int,
    val cookies: List<CookieWeek>
)