package com.developer.java.yandex.borodaweather

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.developer.java.yandex.borodaweather.adapters.WeatherAdapter
import com.developer.java.yandex.borodaweather.entity.Weather
import com.developer.java.yandex.borodaweather.interactor.StartActivityListener
import com.developer.java.yandex.borodaweather.interactor.UpdateWeatherInteractor
import com.developer.java.yandex.borodaweather.utilities.ImageUtilities
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UpdateWeatherInteractor, StartActivityListener{
    override fun <T> startActivityFromAdapter(intent: Intent, clazz: Class<T>) {
        intent.setClass(baseContext, clazz)
        this.startActivity(intent)
    }

    companion object {
        val citiesRussia = listOf("Москва", "Санкт-Петербург", "Волгоград", "Пермь",
            "Уфа", "Тюмень", "Орск", "Казань", "Воронеж", "Новгород")
    }


    override fun onSuccess(weathers: List<Weather>) {
        hiddenLoading()
        mWeatherAdapter.setData(weathers)

    }

    override fun onError(error: Throwable) {
        hiddenLoading()
        Snackbar.make(rv_weather, error.message!!, Snackbar.LENGTH_LONG)
    }

    private lateinit var mWeatherAdapter: WeatherAdapter
    private lateinit var mWeatherModel : WeatherModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImageUtilities.setCacheDir(cacheDir)

        initLayout()
        initHandlers()

    }

    private fun initHandlers() {
        filter_country.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("MainActivity", "Item selected is ${filter_country.getItemAtPosition(position)}")
                mWeatherModel.filter(position)
            }

        }
        filter_day.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("MainActivity", "Item selected is ${filter_country.getItemAtPosition(position)}")
                mWeatherAdapter.setOffsetDate(position)
            }

        }
    }

    private fun initLayout(){
        showLoading()
        mWeatherModel = ViewModelProviders.of(this).get(WeatherModel::class.java)
        mWeatherModel.setWeatherInteractor(this)
        mWeatherModel.getWeather()
        rv_weather.layoutManager = LinearLayoutManager(baseContext)
        mWeatherAdapter = WeatherAdapter(mWeatherModel)
        mWeatherAdapter.registerStartActivityListener(this)
        rv_weather.adapter = mWeatherAdapter
        rv_weather.isNestedScrollingEnabled = false
    }

    private fun showLoading(){
        pb_loading.visibility = View.VISIBLE
        rv_weather.visibility = View.GONE
    }

    private fun hiddenLoading(){
        pb_loading.visibility = View.GONE
        rv_weather.visibility = View.VISIBLE
    }
}
