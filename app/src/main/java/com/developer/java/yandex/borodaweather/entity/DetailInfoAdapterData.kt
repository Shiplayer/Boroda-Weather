package com.developer.java.yandex.borodaweather.entity

/**
 * Created by Shiplayer on 07.12.18.
 */

class DetailInfoAdapterData(val title: String, val temp: String, val iconUrl: String, val condition: String) {

    companion object {
        fun convert(weatherDay: WeatherDay) : List<DetailInfoAdapterData>{
            val list = mutableListOf<DetailInfoAdapterData>()
            weatherDay.parts.forEach {
                list.add(DetailInfoAdapterData(it.partName, it.info.temp.toString(), it.info.icon, it.info.condition))
            }
            return list
        }

        fun convert(listWeatherDay: List<WeatherDay>) : List<DetailInfoAdapterData>{
            val list = mutableListOf<DetailInfoAdapterData>()
            listWeatherDay.forEach {
                list.add(DetailInfoAdapterData(it.date,
                    (it.parts.map { it.info.temp }.sum() / it.parts.size).toString(),
                    it.parts[2].info.icon,
                    it.parts[2].info.condition
                ))
            }
            return list
        }
    }
}