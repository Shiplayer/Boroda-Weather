package com.developer.java.yandex.borodaweather.interactor

import android.content.Intent

/**
 * Created by Shiplayer on 05.12.18.
 */

interface StartActivityListener{

    fun <T> startActivityFromAdapter(intent: Intent, clazz: Class<T>)
}