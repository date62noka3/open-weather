package com.example.openweather.model

data class Prefecture(
    val name: String,
    val cities: List<City>
) {
    data class City(
        val name: String,
        val id: String
    )
}