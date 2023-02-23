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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
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

    private var longAndLats: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherAppTheme {

                // Gets the current long and latitude
                getLocation()

                //Get the current weather in Odense
                getWeather()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background

                ) {
                    ScaffoldTopBar(city = response.name.uppercase())
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
                            } \u2103", fontSize = 40.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Feels like ${
                                (response.main?.feelsLike?.minus(273.15)?.roundToInt()).toString()
                            } \u2103", fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        //Text(text = response.weather[0].description, fontSize = 20.sp)
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

                        Spacer(modifier = Modifier.height(4.dp))
                        //Text(text = "Latitude: ${longAndLats[0]}", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        //Text(text = "Longitude: ${longAndLats[1]}", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(onClick = {
                            getLocation()
                        }) {
                            Text(text = "Get Location")
                        }

                    }
                }
            }
        }
    }

    private fun getWeather(){
        runBlocking {
            launch { response = service.getPosts() }
        }
    }

    private fun getLocation(){
        runBlocking {
            launch { longAndLats = locationProvider.getLocation(this@MainActivity, context) }
        }
    }
}

@Composable
fun ScaffoldTopBar(city: String) {
    Scaffold(topBar = {CustomAppBar(city = city)}) {
        paddingValues -> Modifier.padding(paddingValues)
    }
}