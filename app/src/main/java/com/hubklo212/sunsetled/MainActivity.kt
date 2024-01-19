package com.hubklo212.sunsetled
/*
 * File: MainActivity.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: ViewModel class responsible for managing sunset information
 *              powered by SunriseSunset.io. It communicates with the Repository to fetch sunset
 *              data and exposes the results through a MutableStateFlow. The ViewModel also handles
 *              UI interactions and updates using Compose.
 */


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hubklo212.sunsetled.data.RepositoryImpl
import com.hubklo212.sunsetled.presentation.SunsetViewModel
import com.hubklo212.sunsetled.ui.theme.SunsetLEDTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    // Initialization of SunsetViewModel with Retrofit-backed Repository using viewModels delegate.
    @Suppress("UNCHECKED_CAST")
    private val mainVm by viewModels<SunsetViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SunsetViewModel(RepositoryImpl(RetrofitInstance.api))
                        as T
            }
        }
    })

    // Handling location services
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            SunsetLEDTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Button click triggers location fetch and SunsetViewModel update
                    mainVm.UpdateButton {
                        LocationUtils.fetchLocation(
                            this,
                            fusedLocationProviderClient
                        ) { latitude, longitude ->
                            Toast.makeText(
                                applicationContext,
                                "$latitude $longitude",
                                Toast.LENGTH_SHORT
                            ).show()
                            mainVm.updateSunsetTime(latitude, longitude)
                        }
                    }
                    // Collect and show error toast when triggered by SunsetViewModel
                    val sunsetTime = mainVm.sunset.collectAsState().value
                    val context = LocalContext.current

                    //
                    var dataToSend = sunsetTime.results.sunset
                    // Default ESP8266 device IP address (likely the reason of errors, to delete later)
                    var espIP : String = "192.168.137.102"
                    var parsedTime by remember { mutableStateOf("") }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(48.dp))
                        // Button to initiate data send
                        Button(
                            onClick = {
                                sendDataToESP(espIP, dataToSend) { result ->
                                    // Handle the result as needed (e.g., update UI or show a toast)
                                    Log.d("POST", "Result: $result")
                                }
                            }
                        ) {
                            Text(text = "Send")
                        }
                        mainVm.TextFieldWithLabelAndPlaceHolder { text ->
                            parsedTime = text
                        }
                        // Determine the data to send based on user input
                        dataToSend = if (parsedTime.isNotBlank()) {
                            // Use the parsedTime if it's not blank
                            parsedTime
                        } else {
                            // Use the original sunset time if parsedTime is blank
                            sunsetTime.results.sunset
                        }
                        // Text field for ESP8266 device IP input
                        mainVm.TextFieldEspIP {text2 ->
                            espIP = text2
                        }
                        Text(text = "Data to send: $dataToSend")
                    }



                    LaunchedEffect(key1 = mainVm.showErrorToastChannel) {
                        mainVm.showErrorToastChannel.collectLatest { show ->
                            if (show) {
                                Toast.makeText(
                                    context,
                                    "Error: couldn't get your localisation",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                    // Display loading indicator or sunset time based on availability
                    if (sunsetTime.results.sunset.isBlank()) {
                        Row(modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            content = {
                                CircularProgressIndicator()
                            })
                    } else {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = sunsetTime.results.sunset)
                        }
                    }
                }
            }
        }
    }
}

