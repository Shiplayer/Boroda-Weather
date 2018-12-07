package com.developer.java.yandex.borodaweather.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Shiplayer on 06.12.18.
 */

data class WeatherPartInfo(val partName:String, val info:WeatherData) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(WeatherData::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(partName)
        parcel.writeParcelable(info, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherPartInfo> {
        override fun createFromParcel(parcel: Parcel): WeatherPartInfo {
            return WeatherPartInfo(parcel)
        }

        override fun newArray(size: Int): Array<WeatherPartInfo?> {
            return arrayOfNulls(size)
        }
    }
}