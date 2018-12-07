package com.developer.java.yandex.borodaweather.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Shiplayer on 06.12.18.
 */

data class WeatherData(
    val temp:Int,
    val feelsLike:Int?,
    val icon: String,
    val condition: String,
    val pressure: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(temp)
        parcel.writeValue(feelsLike)
        parcel.writeString(icon)
        parcel.writeString(condition)
        parcel.writeInt(pressure)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherData> {
        override fun createFromParcel(parcel: Parcel): WeatherData {
            return WeatherData(parcel)
        }

        override fun newArray(size: Int): Array<WeatherData?> {
            return arrayOfNulls(size)
        }
    }
}