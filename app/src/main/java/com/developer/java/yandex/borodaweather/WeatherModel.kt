package com.developer.java.yandex.borodaweather

import android.arch.lifecycle.ViewModel
import android.graphics.drawable.Drawable
import com.developer.java.yandex.borodaweather.entity.*
import com.developer.java.yandex.borodaweather.interactor.UpdateWeatherInteractor
import com.developer.java.yandex.borodaweather.retrofit.YandexGeocodeApi
import com.developer.java.yandex.borodaweather.retrofit.YandexWeatherApi
import com.developer.java.yandex.borodaweather.utilities.ImageUtilities
import com.google.gson.JsonElement
import kotlinx.coroutines.*

/**
 * Created by Shiplayer on 05.12.18.
 */

class WeatherModel : ViewModel(){

    private var weather: List<Weather>? = null
    private lateinit var mWeatherInteractor: UpdateWeatherInteractor
    private var drawableCache = mutableMapOf<String, Drawable>()
    private var cities = listOf<String>()
    private val russiaCities = listOf(
        "г. Санкт-Петербург",
        "г. Москва",
        "г. Новосибирск",
        "г. Казань",
        "г. Екатеринбург",
        "г. Тюмень",
        "г. Уфа",
        "г. Сочи",
        "г. Омск",
        "г. Пермь"
    )
    private val otherCities = listOf(
        "г. Хельсинки",
        "г. Прага",
        "г. Варшава",
        "г. Лиссабон",
        "г. Мадрид",
        "г. Мюнхен",
        "г. Париж",
        "г. Прага",
        "г. Рига",
        "г. Рим")
    private var citiesUrl = listOf<String>()
    private val russiaCitiesUrl = listOf(
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/saint-petersburg.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/moscow.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Novosibrsk.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/kazan.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/ekaterinburg.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/tyumen.jpeg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/ufa.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/sochi.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/omsk.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Perm.jpeg"
        )
    private val otherCitiesUrl = listOf(
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Helsinki.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Praga.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Warsaw.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Lisbon.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/madrid.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Munich.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Paris.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Praga.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Riga.jpg",
        "https://github.com/Shiplayer/Boroda-Weather/raw/master/img/Rome.jpg"
    )

    public suspend fun loadImage(url:String, cityName:String): Drawable{
        return if(drawableCache.containsKey(cityName))
            drawableCache[cityName]!!
        else {
            drawableCache[cityName] = ImageUtilities.loadDrawable(url, cityName).await()
            drawableCache[cityName]!!
        }
    }

    public fun setWeatherInteractor(interactor: UpdateWeatherInteractor){
        mWeatherInteractor = interactor
    }

    public fun getWeather(){
        if(weather == null)
            loadWeather()
        else
            mWeatherInteractor.onSuccess(weather!!)

    }

    public fun filter(type: Int){
        when(type){
            0 -> {
                cities = russiaCities
                citiesUrl = russiaCitiesUrl
            }
            1 -> {
                cities = otherCities
                citiesUrl = otherCitiesUrl
            }
        }
        update()
    }

    public fun update(){
        loadWeather()
    }

    private fun loadWeather(){
        val errorHandler = CoroutineExceptionHandler{_, exception ->
            GlobalScope.launch(Dispatchers.Main) {
                mWeatherInteractor.onError(exception)
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            val list = mutableListOf<Weather>()
            val jobs = mutableListOf<Job>().apply {
                cities.forEachIndexed { index, city ->
                    this.add(GlobalScope.launch(errorHandler) {
                        val cityResponse = YandexGeocodeApi.api.getPositionOfCity(city).await()
                        val pair = getPointFromJson(cityResponse.body()!!)
                        if (pair != null) {
                            val weather = YandexWeatherApi.api.getWeather(pair.first, pair.second).await()
                            list.add(getWeatherFromJson(City(city, citiesUrl[index]), weather.body()!!)!!)
                        }
                    })
                }
            }
            jobs.forEach { it.join() }

            mWeatherInteractor.onSuccess(list)

        }
    }

    private fun getPointFromJson(element : JsonElement) : Pair<Float, Float>?{
        val featureMember = element.asJsonObject["response"].asJsonObject["GeoObjectCollection"].asJsonObject["featureMember"].asJsonArray

        val geoObject = featureMember.lastOrNull { if(it.isJsonObject) it.asJsonObject.has("GeoObject") else false}?.asJsonObject
        return if(geoObject != null) {
            val point = geoObject["GeoObject"].asJsonObject["Point"].asJsonObject["pos"].asString.split(" ")
            Pair(point[1].toFloat(), point[0].toFloat())
        } else
            null
    }

    private fun getWeatherFromJson(city:City, element: JsonElement) : Weather? {
        val fact = parseWeatherInfo(element.asJsonObject["fact"])
        //val parts = element.asJsonObject["forecasts"].asJsonArray[0].asJsonObject["parts"].asJsonObject
        val days = mutableListOf<WeatherDay>()
        for(day in element.asJsonObject["forecasts"].asJsonArray){
            val parts = day.asJsonObject["parts"].asJsonObject
            val list = mutableListOf<WeatherPartInfo>()
            parts.keySet().filter {!it.contains("_")}.forEach {
                val dayInfo = parseWeatherInfo(parts.get(it))
                list.add(WeatherPartInfo(it, dayInfo))
            }
            days.add(WeatherDay(day.asJsonObject["date"].asString, list))
        }

        return Weather(city, fact, days)
    }

    private fun parseWeatherInfo(element: JsonElement) : WeatherData {
        val day = element.asJsonObject
        val temp = if(day.has("temp_avg"))
            day["temp_avg"].asInt
        else
            day["temp"].asInt
        val feelsLike = if(day.has("feelsLike")){
            day["feelsLike"].asInt
        } else
            null
        val icon = day["icon"].asString
        val condition = day["condition"].asString
        val pressure = day["pressure_mm"].asInt
        return WeatherData(temp, feelsLike, icon, condition, pressure)
    }
}