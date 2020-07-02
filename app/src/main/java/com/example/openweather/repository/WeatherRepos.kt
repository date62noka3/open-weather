package com.example.openweather.repository

import com.example.openweather.WeatherApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WeatherRepos {
    /*
    * Retrofitを取得する
    *
    * @return WeatherApi用Retrofitインスタンス
    * */
    fun getRetrofit(): WeatherApi {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit = Retrofit.Builder().baseUrl(WeatherApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(WeatherApi::class.java)
    }
}