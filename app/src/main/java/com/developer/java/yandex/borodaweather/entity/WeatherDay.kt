package com.developer.java.yandex.borodaweather.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Shiplayer on 06.12.18.
 */

data class WeatherDay(val date:String, val parts:List<WeatherPartInfo>) :Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(WeatherPartInfo)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeTypedList(parts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherDay> {
        override fun createFromParcel(parcel: Parcel): WeatherDay {
            return WeatherDay(parcel)
        }

        override fun newArray(size: Int): Array<WeatherDay?> {
            return arrayOfNulls(size)
        }
    }

}