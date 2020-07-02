package com.example.openweather.model

data class Weather(
    val pinpointLocations : List<PinpointLocations>,
    val link : String,
    val forecasts : List<Forecasts>,
    val location : Location,
    val publicTime : String,
    val copyright : Copyright,
    val title : String,
    val description : Description
){
    data class PinpointLocations(
        val link : String,
        val name : String
    )

    data class Forecasts(
        val dateLabel : String,
        val telop : String,
        val date : String,
        val temperature : Temperature,
        val image : Image
    ){
        data class Temperature (
            val min : Any?, //nullが返ってくるものは型不明のnull許容型を用いる
            val max : Max? //Maxも返ってこない時ある。
        ){
            data class Max (
                val celsius : Int,
                val fahrenheit : Double
            )
        }

        data class Image (
            val width : Int,
            val link : String = "", //必須
            val url : String,
            val title : String,
            val height : Int
        )
    }

    data class Location (
        val city : String,
        val area : String,
        val prefecture : String
    )

    data class Copyright (
        val provider : List<Provider>,
        val link : String,
        val title : String,
        val image : Image
    ){
        data class Provider (
            val link : String,
            val name : String
        )

        data class Image (
            val width : Int,
            val link : String = "",
            val url : String,
            val title : String,
            val height : Int
        )
    }

    data class Description (
        val text : String,
        val publicTime : String
    )
}