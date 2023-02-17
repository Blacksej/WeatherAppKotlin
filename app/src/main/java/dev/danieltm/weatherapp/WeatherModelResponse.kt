package dev.danieltm.weatherapp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class WeatherModelResponse {

    @Serializable
    data class Welcome (
        val coord: Coord? = null,
        val weather: List<Weather> = emptyList(),
        val base: String = "",
        val main: Main? = null,
        val visibility: Long = 2,
        val wind: Wind? = null,
        val clouds: Clouds? = null,
        val dt: Long = 2,
        val sys: Sys? = null,
        val timezone: Long = 2,
        val id: Long = 2,
        val name: String = "",
        val cod: Long = 2
    )

    @Serializable
    data class Clouds (
        val all: Long
    )

    @Serializable
    data class Coord (
        val lon: Double,
        val lat: Double
    )

    @Serializable
    data class Main (
        val temp: Double,

        @SerialName("feels_like")
        val feelsLike: Double,

        @SerialName("temp_min")
        val tempMin: Double,

        @SerialName("temp_max")
        val tempMax: Double,

        val pressure: Long,
        val humidity: Long,

        @SerialName("sea_level")
        val seaLevel: Long,

        @SerialName("grnd_level")
        val grndLevel: Long
    )

    @Serializable
    data class Sys (
        val country: String,
        val sunrise: Long,
        val sunset: Long
    )

    @Serializable
    data class Weather (
        val id: Long,
        val main: String,
        val description: String,
        val icon: String
    )

    @Serializable
    data class Wind (
        val speed: Double,
        val deg: Long,
        val gust: Double
    )
}