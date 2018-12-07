package com.developer.java.yandex.borodaweather.retrofit

import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by Shiplayer on 05.12.18.
 */
interface YandexWeatherApi {

    companion object {
        val api by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.weather.yandex.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

            retrofit.create(YandexWeatherApi::class.java)
        }

        private val API_KEY = "94556265-1002-48c6-9f9e-1d72db08f96a"
    }

    @GET("v1/forecast")
    fun getWeather(
        @Query("lat") latitude:Float,
        @Query("lon") longitude:Float,
        @Query("lang") language: String = "en_US",
        @Query("limit") limit:Int = 7,
        @Query("hours") hours: Boolean = true,
        @Query("extra") extra:Boolean = false,
        @Header("X-Yandex-API-Key") apiKey:String = API_KEY
    ) : Deferred<Response<JsonElement>>
}