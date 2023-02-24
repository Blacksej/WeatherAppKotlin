package dev.danieltm.weatherapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import dev.danieltm.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.jar.Manifest
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val context: Context = this
    private val service = PostsService.create()
    private var response = WeatherModelResponse.Welcome()
    private var locationProvider = LocationProvider()

    private var longitude = mutableStateOf("0")
    private var latitude = mutableStateOf("0")
    private var cityName = mutableStateOf("0")
    private var weatherDescription = mutableStateOf("0")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherAppTheme {

                // Gets the current long and latitude
                getLocation()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background

                ) {
                    ScaffoldTopBar(city = cityName.value.uppercase())
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .offset(0.dp, 60.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val hourOfDay = remember {
                            LocalTime.now()
                                .hour + 1
                        }
                        val dayOfWeek = remember {
                            LocalDate.now()
                                .dayOfWeek
                        }

                        Text(text = "$dayOfWeek $hourOfDay:00", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${
                                (response.main?.temp?.minus(273.15)?.roundToInt()).toString()
                            }\u00B0", fontSize = 40.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Feels like ${
                                (response.main?.feelsLike?.minus(273.15)?.roundToInt()).toString()
                            }\u00B0", fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = weatherDescription.value, fontSize = 20.sp)
                    }
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .offset(0.dp, 70.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "${(response.wind?.speed).toString()} m/s", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${(response.wind?.deg).toString()} deg", fontSize = 20.sp)
                    }
                }
            }
        }
    }

    private fun getWeather(lat: String, long: String){
        runBlocking {
            launch {
                response = service.getPosts(lat, long)
                weatherDescription.value = response.weather[0].description
            }
        }
    }

    private fun getLocation(){
        locationProvider.getLocation(this@MainActivity, context)
        // Callback function here
        {list: ArrayList<String> ->
            latitude.value = list[0]
            longitude.value = list[1]
            getCity(list[0], list[1])
            getWeather(list[0], list[1])}
    }

    // Method for setting the cityname variable
    private fun getCity(lat: String, long: String){
        cityName.value = locationProvider.getCityName(lat.toDouble(), long.toDouble(), context)
    }
}

@Composable
fun ScaffoldTopBar(city: String) {
    Scaffold(topBar = {CustomAppBar(city = city)}) {
        paddingValues -> Modifier.padding(paddingValues)
    }
}