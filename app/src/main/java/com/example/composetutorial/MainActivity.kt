package com.example.composetutorial

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetutorial.viewmodel.WeatherViewModel
import com.example.composetutorial.data.model.WeatherResponse
import com.example.composetutorial.data.repository.NetworkResponse
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private val weatherViewModel: WeatherViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTutorialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                MakeFetchWeatherData()

            }
        }

    }

    @Composable
    private fun MakeFetchWeatherData() {
        var showLoader by remember { mutableStateOf(true) }
        weatherViewModel.getLatestWeather("London")
        val weatherResult = weatherViewModel.weatherLiveData.observeAsState()

        when (weatherResult.value) {

            is NetworkResponse.Error -> {
                showLoader = false
                Toast.makeText(
                    this,
                    (weatherResult.value as NetworkResponse.Error).error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            is NetworkResponse.Success -> {
                showLoader = false
                val res =
                    (weatherResult.value as NetworkResponse.Success<Any>).data as WeatherResponse
                Log.d(TAG, "makeFetchWeatherData: $res")
            }

            NetworkResponse.Loading -> {
                CircularLoader(showLoader)
            }

            null -> TODO()
        }


    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(Color.Gray) // Optional: Add a background color
    ) {
        Text(
            text = "Hello $name!", modifier = modifier

        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTutorialTheme {
        Greeting("Android")
    }
}

@Composable
fun CircularLoader(showLoader: Boolean) {
    if (showLoader)
    CircularProgressIndicator()
    else null
}
