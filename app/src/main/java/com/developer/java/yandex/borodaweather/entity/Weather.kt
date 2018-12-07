package com.developer.java.yandex.borodaweather.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Shiplayer on 05.12.18.
 */

data class Weather(val city:City, val now:WeatherData, val days: List<WeatherDay>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(City::class.java.classLoader),
        parcel.readParcelable(WeatherData::class.java.classLoader),
        parcel.createTypedArrayList(WeatherDay)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(city, flags)
        parcel.writeParcelable(now, flags)
        parcel.writeTypedList(days)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(parcel)
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }

}