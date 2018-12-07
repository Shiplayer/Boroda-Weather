package com.developer.java.yandex.borodaweather.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.developer.java.yandex.borodaweather.R
import com.developer.java.yandex.borodaweather.entity.WeatherPartInfo
import kotlinx.coroutines.*
import java.net.URL

/**
 * Created by Shiplayer on 05.12.18.
 */

class TempAdapter(val data : List<WeatherPartInfo>) : RecyclerView.Adapter<TempAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.temp_item, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(data[position])
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val day = view.findViewById<TextView>(R.id.temp_day)
        val temp = view.findViewById<TextView>(R.id.tv_count_temp)
        val image = view.findViewById<SVGImageView>(R.id.iv_temp)
        val weatherProgressBar = view.findViewById<ProgressBar>(R.id.pb_weather_icon)
        private val url = view.resources.getString(R.string.url_icon)
        private val replaceName = view.resources.getString(R.string.yandex_url_icon_replace_name)

        fun init(weather : WeatherPartInfo){
            val errorHandler = CoroutineExceptionHandler{_, error ->
                Log.i("TempAdapter", error.message)
                Toast.makeText(null, error.message, Toast.LENGTH_LONG).show()
            }
            day.text = weather.partName
            temp.text = weather.info.temp.toString()
            GlobalScope.launch(Dispatchers.Main + errorHandler){
                showLoading()
                val drawableWork = async(Dispatchers.IO) {
                    val iconUrl = url.replace(replaceName, weather.info.icon.replace("\"", ""))
                    Log.i("TempAdapter", "url = $iconUrl")
                    SVG.getFromInputStream(URL(iconUrl).openStream())
                }
                image.setSVG(drawableWork.await())
                hiddenLoading()
            }
        }
        fun showLoading(){
            weatherProgressBar.visibility = View.VISIBLE
            image.visibility = View.GONE
        }

        fun hiddenLoading(){
            weatherProgressBar.visibility = View.GONE
            image.visibility = View.VISIBLE
        }
    }
}