package dev.danieltm.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.danieltm.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    private val service = PostsService.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val posts = produceState<WeatherModelResponse.Welcome>(
                initialValue = WeatherModelResponse.Welcome(),
                producer = {
                    value = service.getPosts()
                }
            )

            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(text = posts.value.name, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = posts.value.main?.temp.toString(), fontSize = 20.sp)
                            }
                    }
                }
            }
        }
    }

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}