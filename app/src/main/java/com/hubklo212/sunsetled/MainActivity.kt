package com.hubklo212.sunsetled

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

    private val mainVm by viewModels<SunsetViewModel>(factoryProducer = {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SunsetViewModel(RepositoryImpl(RetrofitInstance.api))
                    as T
            }
        }
    })

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
                    //Instrukcja()
                    mainVm.UpdateButton {
                        LocationUtils.fetchLocation(this, fusedLocationProviderClient) { latitude, longitude ->
                            Toast.makeText(
                                applicationContext,
                                "$latitude $longitude",
                                Toast.LENGTH_SHORT
                            ).show()
                            mainVm.updateSunsetTime(latitude, longitude)
                        }

                    }
                    val sunsetTime = mainVm.sunset.collectAsState().value
                    val context = LocalContext.current
                    
                    LaunchedEffect(key1 = mainVm.showErrorToastChannel){
                        mainVm.showErrorToastChannel.collectLatest { show ->
                            if (show) {
                                Toast.makeText(
                                    context, "Error",Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }
                    if (sunsetTime.results.sunset.isBlank()) {
                        Row(modifier = Modifier.fillMaxSize(),
                            content ={
                                CircularProgressIndicator()
                            })
                    } else {
                        Row(modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                            horizontalArrangement = Arrangement.Center){
                            Text(text = sunsetTime.results.sunset)
                        }
                    }
                }
            }
        }
    }
}

