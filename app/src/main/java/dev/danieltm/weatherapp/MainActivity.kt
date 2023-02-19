package dev.danieltm.weatherapp

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import dev.danieltm.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val service = PostsService.create()
    private var response = WeatherModelResponse.Welcome()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //Get the current weather in Odense
            getWeather()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Cyan

                ) {
                    ScaffoldTopBar(city = response.name.uppercase())
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .offset(0.dp, 200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(text = response.name.uppercase(), fontSize = 40.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "${(response.main?.temp?.minus(273.15)?.roundToInt()).toString()} \u2103", fontSize = 40.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "${(response.wind?.speed).toString()} m/s", fontSize = 30.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = response.weather[0].description, fontSize = 30.sp)
                            }
                    }
            }
        }
    fun getWeather(){
        runBlocking {
            launch { response = service.getPosts() }
        }
    }
}

@Composable
fun ScaffoldTopBar(city: String) {
    Scaffold(topBar = {CustomAppBar(city = city)}) {
        paddingValues -> Modifier.padding(paddingValues)
    }
}