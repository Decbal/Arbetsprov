package com.example.fridaycookie.network

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL_COOKIE = "https://api.fredagskakan.se"
private const val BASE_URL_FACT = "http://numbersapi.com"

private val retrofitCookie = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL_COOKIE)
    .build()

private val retrofitFact = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL_FACT)
    .build()

private val gson = GsonBuilder()
    .registerTypeAdapter(CookieScore::class.java, ScoreDeserializer())
    .registerTypeAdapter(FridayCookie::class.java, FridayCookieDeserializer())
    .create()

interface CookieAPIService {
    @GET("getkaka")
    suspend fun getCookies(): String
}

interface FactAPIService {
    @GET("{number}")
    suspend fun getNumberFact(@Path("number") num: Int): String
}


object CookieAPI{ // using singleton not recommended?
    private val retrofitService : CookieAPIService by lazy {
        retrofitCookie.create(CookieAPIService::class.java)
    }

    // The value of Score uses both String and Double so conversion is done manually
    suspend fun getCookies() : List<CookieYear> {
        val jsonString: String = retrofitService.getCookies()
        val listOfFridayCookies: List<FridayCookie> = gson.fromJson(jsonString, Array<FridayCookie>::class.java).toList()
        val cookieYears: MutableMap<Int, MutableList<CookieWeek>> = mutableMapOf()
        for((i, fridayCookie) in listOfFridayCookies.withIndex()) {
            //Log.d("HERE", "${fridayCookie.year}; ${fridayCookie.week}; ${fridayCookie.cookie}; ${fridayCookie.url}")
            val yearList = cookieYears.getOrPut(fridayCookie.year){mutableListOf()}
            val cookieWeek = CookieWeek(fridayCookie.week, CookieOfTheWeek(fridayCookie.cookie, fridayCookie.url))
            yearList.add(cookieWeek)
        }
        val cookieYearList = mutableListOf<CookieYear>()
        for((year, list) in cookieYears.entries)  {
            cookieYearList.add(CookieYear(year, list.toList()))
        }
        return cookieYearList
    }
}

object FactAPI{
    private val retrofitService : FactAPIService by lazy {
        retrofitFact.create(FactAPIService::class.java)
    }

    suspend fun getFact(num: Int) : String {
        val retString: String = retrofitService.getNumberFact(num)
        return retString
    }
}

