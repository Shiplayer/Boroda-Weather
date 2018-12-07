package com.developer.java.yandex.borodaweather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.developer.java.yandex.borodaweather.adapters.DetailInfoAdapter
import com.developer.java.yandex.borodaweather.entity.DetailInfoAdapterData
import com.developer.java.yandex.borodaweather.entity.WeatherDay


private const val WEATHER_DAY_KEY = "WEATHER DAY"
private const val LIST_WEATHER_DAY_KEY = "LIST OF WEATHER DAY"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailWeather.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailWeather.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetailWeather : Fragment() {
    private var weatherDay: WeatherDay? = null
    private var listWeatherDay: List<WeatherDay>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if(it.containsKey(WEATHER_DAY_KEY)) {
                weatherDay = it.getParcelable(WEATHER_DAY_KEY)

            }else if(it.containsKey(LIST_WEATHER_DAY_KEY)){
                listWeatherDay = it.getParcelableArrayList(LIST_WEATHER_DAY_KEY)

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ditail_weather, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv_detail)
        rv.layoutManager = LinearLayoutManager(context)
        val adapter = DetailInfoAdapter()
        rv.adapter = adapter
        if(weatherDay != null)
            adapter.setData(DetailInfoAdapterData.convert(weatherDay!!))
        else if(listWeatherDay != null)
            adapter.setData(DetailInfoAdapterData.convert(listWeatherDay!!))
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(weather: WeatherDay) =
            DetailWeather().apply {
                arguments = Bundle().apply {
                    putParcelable(WEATHER_DAY_KEY, weather)
                }
            }

        @JvmStatic
        fun newInstance(weatherList: List<WeatherDay>) =
            DetailWeather().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(LIST_WEATHER_DAY_KEY, ArrayList(weatherList))
                }
            }
    }
}
