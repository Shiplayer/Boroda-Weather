package com.developer.java.yandex.borodaweather.retrofit

import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Shiplayer on 05.12.18.
 */

interface YandexGeocodeApi {

    companion object {
        val api by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://geocode-maps.yandex.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

            retrofit.create(YandexGeocodeApi::class.java)
        }

        private val API_KEY = "3d55e093-3c1a-4e3f-9fbd-c11fd64cb534"
    }

    @GET("1.x")
    fun getPositionOfCity(
        @Query("geocode") geocode:String,
        @Query("format") format:String = "json",
        @Query("lang") language: String = "en_US",
        @Query("apikey") apiKey:String = API_KEY
    ) : Deferred<Response<JsonElement>>
}