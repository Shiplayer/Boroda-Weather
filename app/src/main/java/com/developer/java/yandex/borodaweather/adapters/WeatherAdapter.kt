package com.developer.java.yandex.borodaweather.adapters

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.developer.java.yandex.borodaweather.R
import com.developer.java.yandex.borodaweather.WeatherActivity
import com.developer.java.yandex.borodaweather.WeatherModel
import com.developer.java.yandex.borodaweather.entity.Weather
import com.developer.java.yandex.borodaweather.interactor.StartActivityListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Shiplayer on 05.12.18.
 */

class WeatherAdapter(private val model: WeatherModel) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    private var mData = mutableListOf<Weather>()
    private lateinit var mStartActivityListener: StartActivityListener
    private var offsetDate = 0

    fun setData(list: List<Weather>){
        mData = list.toMutableList()
        notifyDataSetChanged()
    }

    fun setOffsetDate(offset : Int){
        offsetDate = offset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(mData[position], mStartActivityListener, model, offsetDate)
    }

    fun registerStartActivityListener(listener: StartActivityListener) {
        mStartActivityListener = listener
    }

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val cityName = view.findViewById<TextView>(R.id.tv_name_city)
        val rvTemp = view.findViewById<RecyclerView>(R.id.rv_temp)
        val cityImage = view.findViewById<ImageView>(R.id.city_image)
        val progressBar = view.findViewById<ProgressBar>(R.id.pb_load_image)

        fun init(weather: Weather, listener: StartActivityListener, model: WeatherModel, offset: Int){
            rvTemp.layoutManager = LinearLayoutManager(itemView.context, LinearLayout.HORIZONTAL, false)
            rvTemp.adapter = TempAdapter(weather.days[offset].parts)
            cityName.text = weather.city.city

            GlobalScope.launch(Dispatchers.Main){
                showLoading()
                cityImage.setImageDrawable(model.loadImage(weather.city.urlImage, weather.city.city))
                hiddenLoading()

            }
            view.setOnClickListener{
                val intent = Intent()
                intent.putExtra("WEATHER", weather)
                intent.putExtra("IMAGE_NAME", weather.city.urlImage)
                listener.startActivityFromAdapter(intent, WeatherActivity::class.java)
            }
        }

        fun showLoading(){
            progressBar.visibility = View.VISIBLE
            cityImage.visibility = View.GONE
        }

        fun hiddenLoading(){
            progressBar.visibility = View.GONE
            cityImage.visibility = View.VISIBLE
        }
    }
}