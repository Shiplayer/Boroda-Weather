package com.developer.java.yandex.borodaweather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.developer.java.yandex.borodaweather.entity.Weather
import com.developer.java.yandex.borodaweather.utilities.ImageUtilities
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.content_weather.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherActivity : AppCompatActivity() {
    private lateinit var weather: Weather
    private val listOfDay = listOf("Сегодня", "Завтра", "На 3 дня")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent != null && intent.hasExtra("WEATHER")){
            weather = intent.getParcelableExtra<Weather>("WEATHER")
            title = weather.city.city

            GlobalScope.launch {
                iv_city_toolbar.setImageDrawable(ImageUtilities.loadDrawable(weather.city.urlImage, weather.city.city).await())
            }
            pager.adapter = ScreenSlidePagerAdapter(supportFragmentManager)

        }
    }

    private inner class ScreenSlidePagerAdapter(fm : FragmentManager) : FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment = if(position != 2) DetailWeather.newInstance(weather.days[position])
        else DetailWeather.newInstance(weather.days.subList(0, position + 1))

        override fun getCount(): Int = listOfDay.size

        override fun getPageTitle(position: Int): CharSequence? {
            return listOfDay[position]
        }
    }
}
