package com.example.openweather

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.openweather.model.Weather
import com.example.openweather.repository.AreaRepos
import com.example.openweather.repository.WeatherRepos
import com.example.openweather.utils.UrlImageGetTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private object WeatherContainerViews {
    val dateLabel = 0
    val weatherImage = 1
    val telopText = 2
    val maxTempText = 3
    val minTempText = 4
}

class MainActivity : AppCompatActivity() {
    private val weatherService = WeatherRepos().getRetrofit()
    private val areaService = AreaRepos()

    private var area: Map<String, Map<String, String>>? = null //Map<都道府県名, 市町村<市町村名, ID>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var pref = "" //市町村を素早く取得するために都道府県を保持する

        prefSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val spinnerParent = parent as Spinner
                val prefName = spinnerParent.selectedItem as String

                //市町村プルダウンを更新
                val cities = area!![prefName]
                val cityAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    cities!!.keys.toList()
                )
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                citySpinner.adapter = cityAdapter

                pref = prefName
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val spinnerParent = parent as Spinner
                val cityName = spinnerParent.selectedItem as String

                val cityId = area!![pref]!![cityName]!!

                CoroutineScope(Dispatchers.Main).launch {
                    //天気情報を取得
                    val weather: Weather = weatherService.getWeather(cityId)

                    //日にちごとの天気情報を表示
                    weather.forecasts.forEachIndexed { i, forecast ->
                        val weatherLinear = weatherTable.children.elementAt(i) as LinearLayout

                        //天気アイコン
                        val urlImageGetTask = UrlImageGetTask(
                            weatherLinear.children.elementAt(WeatherContainerViews.weatherImage) as ImageView
                        )
                        urlImageGetTask.execute(forecast.image.url)

                        //天気
                        (weatherLinear.children.elementAt(WeatherContainerViews.telopText) as TextView)
                            .text = forecast.telop

                        //最高気温
                        val max = forecast.temperature.max
                        (weatherLinear.children.elementAt(WeatherContainerViews.maxTempText) as TextView)
                            .text = if (max != null) {
                            "${max.celsius}℃"
                        } else {
                            "-"
                        }

                        //最低気温
                        val min = forecast.temperature.min
                        (weatherLinear.children.elementAt(WeatherContainerViews.minTempText) as TextView)
                            .text = if (min != null) {
                            "${min.celsius}℃"
                        } else {
                            "-"
                        }
                    }

                    //詳細説明を表示
                    descriptionText.text = weather.description.text
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        getArea()
    }

    /*
    * エリアを取得し、プルダウンに値を設定
    * */
    private fun getArea() = CoroutineScope(Dispatchers.Main).launch {
        areaService.get().join()
        area = areaService.createArea()

        //都道府県プルダウンを更新
        if (area != null) {
            val prefAdapter = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_spinner_item,
                area!!.keys.toList()
            )
            prefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prefSpinner.adapter = prefAdapter
        }
    }
}