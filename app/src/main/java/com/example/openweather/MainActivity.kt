package com.example.openweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.openweather.repository.WeatherRepos

class MainActivity : AppCompatActivity() {
    private val service = WeatherRepos().getRetrofit()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}