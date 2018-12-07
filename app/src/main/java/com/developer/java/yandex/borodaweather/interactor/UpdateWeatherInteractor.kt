package com.developer.java.yandex.borodaweather.interactor

import com.developer.java.yandex.borodaweather.entity.Weather

/**
 * Created by Shiplayer on 05.12.18.
 */

interface UpdateWeatherInteractor{
    fun onSuccess(weathers: List<Weather>)

    fun onError(error: Throwable)
}