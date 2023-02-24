package dev.danieltm.weatherapp

object HttpRoutes {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather?"
    fun getWeatherFromLatAndLong(lat: String, long: String):String{
        val GET_ODENSE = "${BASE_URL}lat=$lat&lon=$long&appid=148932149d9e7c5c7e5e3a39504a6054"
        return GET_ODENSE
    }
}