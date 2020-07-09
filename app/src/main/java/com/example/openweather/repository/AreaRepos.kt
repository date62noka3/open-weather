package com.example.openweather.repository

import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.nio.charset.Charset

class AreaRepos {
    companion object {
        private const val AREA_URL =
            "http://weather.livedoor.com/forecast/rss/primary_area.xml" //主要な観測地点マップ
        private var responseData: String? = null //getしてきたデータ

        private val prefRegex = Regex("<pref title=\".+\">[\\s\\S]*?</pref>") //都道府県を抽出する正規表現
        private val prefNameRegex = Regex("<pref title=\".+\"") //都道府県名を抽出する正規表現
        private val cityRegex = Regex("<city title=\".+\" id=\"[0-9]{6}\"") //市町村名とIDを抽出する正規表現
    }

    /*
    * 観測地点データを取得
    * */
    fun get() = AREA_URL.httpGet().response { _, response, result ->
        when (result) {
            is Result.Success -> {
                responseData = response.data.toString(Charset.defaultCharset())
                Log.d("GET", "success")
            }
            is Result.Failure -> {
                Log.d("GET", "failure")
            }
        }
    }

    /*
    * 都道府県名、市町村名とIDのセットを抽出
    *
    * @return 都道府県リスト Map<都道府県名, 市町村<市町村名, ID>>
    * */
    fun createArea(): Map<String, Map<String, String>>? {
        responseData ?: return null

        //都道府県ごとに抽出
        val prefectures: List<String> = prefRegex.findAll(responseData!!).map { it.value }.toList()

        val area = mutableMapOf<String, Map<String, String>>()
        for (x in prefectures) {
            val prefName: String = prefNameRegex.find(x)!!.value
                .replace("<pref title=\"", "")
                .replace("\"", "")

            //観測地点を抽出
            // cities : <観測地点名, 観測地点ID>
            val cities: Map<String, String> = cityRegex.findAll(x).map {
                val value = it.value.replace("<city title=\"", "")
                    .replace("\" id=\"", "")
                    .replace("\"", "")

                value.substring(0, value.length - 6) to
                        value.substring(value.length - 6, value.length)
            }.toMap()

            area[prefName] = cities
        }

        return area
    }
}