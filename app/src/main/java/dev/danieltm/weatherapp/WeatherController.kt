package dev.danieltm.weatherapp

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

class WeatherController(
    // Dependency injection
    private val client: HttpClient
) : PostsService{
    override suspend fun getPosts(): WeatherModelResponse.Welcome {
        return try{
           var temp = client.get { url(HttpRoutes.GET_ODENSE) }
            var tempbody = temp.body<WeatherModelResponse.Welcome>()
            tempbody

        } catch (e: RedirectResponseException){
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            WeatherModelResponse.Welcome()
        }catch (e: ClientRequestException){
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            WeatherModelResponse.Welcome()
        }catch (e: ServerResponseException){
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            WeatherModelResponse.Welcome()
        }catch (e: Exception){
            // 3xx - responses
            println("Error: ${e.message}")
            WeatherModelResponse.Welcome()
        }
    }
}