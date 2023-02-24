package dev.danieltm.weatherapp

import android.net.http.HttpResponseCache.install
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface PostsService {
    suspend fun getPosts(lat: String, long: String): WeatherModelResponse.Welcome

    companion object{
        fun create() : PostsService{
            return WeatherController(
                client = HttpClient(Android){
                    install(ContentNegotiation){
                        json(Json{
                            prettyPrint = true
                            isLenient = true
                        })
                    }
                }
            )
        }
    }
}