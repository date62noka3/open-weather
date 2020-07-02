package com.example.openweather

import com.example.openweather.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    companion object{
        const val BASE_URL = "http://weather.livedoor.com/"
    }

    @GET("forecast/webservice/json/v1")
    suspend fun getWeather(
        @Query("city")
        city:String
    ): Weather
}