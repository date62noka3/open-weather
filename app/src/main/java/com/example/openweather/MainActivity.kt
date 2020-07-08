package com.example.openweather

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.openweather.model.Prefecture
import com.example.openweather.repository.AreaRepos
import com.example.openweather.repository.WeatherRepos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val weatherService = WeatherRepos().getRetrofit()
    private val areaService = AreaRepos()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Default).launch {
            areaService.get().join()
            val area: List<Prefecture>? = areaService.createArea()
            if (area != null) {
                for (x in area) {
                    Log.d("HOLE", "都道府県名 : ${x.name}, 観測地点数 : ${x.cities.size}")
                }
            }
        }
    }
}