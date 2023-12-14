package com.hubklo212.sunsetled.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubklo212.sunsetled.data.Repository
import com.hubklo212.sunsetled.data.Result
import com.hubklo212.sunsetled.data.model.GeneralInfo
import com.hubklo212.sunsetled.data.model.Results
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SunsetViewModel(
    private val repository: Repository
): ViewModel() {

    private val _sunset = MutableStateFlow<GeneralInfo>(GeneralInfo(Results(sunset = "00:00", timezone = "GMT+0")))
    val sunset = _sunset.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>(Channel.CONFLATED)
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    fun updateSunsetTime(lat : Double, lng : Double){
        viewModelScope.launch {
            repository.getSunsetTime(lat, lng).collectLatest { result ->
                Log.d("PROCESS_D","Collecting ${result}")
                when(result){
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Success -> {
                        result.data?.let{ sunset ->
                            _sunset.update { sunset }
                        }
                    }
                }
            }
        }
    }
    @Composable
    fun UpdateButton(onClick: () -> Unit) {
        var isChecked by remember { mutableStateOf(false) }
        val isButtonEnabled by remember { derivedStateOf { !isChecked } }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    if (isButtonEnabled) {
                        onClick()
                    }
                },
                modifier = Modifier,
                enabled = isButtonEnabled // przycisk staje sie szary, gdy jest fajka
            ) {
                Text("Update manually")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text("Auto-update")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}