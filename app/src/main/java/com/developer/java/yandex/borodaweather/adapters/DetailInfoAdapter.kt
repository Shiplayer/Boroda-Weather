package com.developer.java.yandex.borodaweather.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.developer.java.yandex.borodaweather.R
import com.developer.java.yandex.borodaweather.entity.DetailInfoAdapterData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.URL

/**
 * Created by Shiplayer on 07.12.18.
 */

class DetailInfoAdapter : RecyclerView.Adapter<DetailInfoAdapter.ViewHolder>() {
    var mData = listOf<DetailInfoAdapterData>()

    fun setData(data: List<DetailInfoAdapterData>){
        mData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_info_item, parent, false))
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.init(mData[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.detail_info_title)
        val temp = view.findViewById<TextView>(R.id.detail_info_temp)
        val icon = view.findViewById<SVGImageView>(R.id.detail_info_icon)
        val condition = view.findViewById<TextView>(R.id.detail_info_condition)
        private val url = view.resources.getString(R.string.url_icon)
        private val replaceName = view.resources.getString(R.string.yandex_url_icon_replace_name)

        fun init(detail: DetailInfoAdapterData){
            title.text = detail.title
            temp.text = detail.temp
            condition.text = detail.condition
            GlobalScope.launch(Dispatchers.Main) {
                val drawableWork = async(Dispatchers.IO) {
                    val iconUrl = url.replace(replaceName, detail.iconUrl.replace("\"", ""))
                    Log.i("TempAdapter", "url = $iconUrl")
                    SVG.getFromInputStream(URL(iconUrl).openStream())
                }
                icon.setSVG(drawableWork.await())
            }
        }
    }
}