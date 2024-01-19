package com.hubklo212.sunsetled.presentation
/*
 * File: SunsetViewModel.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: ViewModel class responsible for managing sunset information. It communicates
 *              with the Repository to fetch sunset data and exposes the results through
 *              a MutableStateFlow. The ViewModel also handles UI interactions and updates
 *              using Compose.
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubklo212.sunsetled.data.Repository
import com.hubklo212.sunsetled.data.RequestResult
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

    // MutableStateFlow to hold sunset information with initial default values
    private val _sunset = MutableStateFlow(GeneralInfo(Results(sunset = "00:00:00 PM", timezone = "GMT+0")))
    val sunset = _sunset.asStateFlow()

    // Channel for triggering the display of error toast messages in the UI
    private val _showErrorToastChannel = Channel<Boolean>(Channel.CONFLATED)
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    // Function to update sunset information based on provided latitude and longitude
    fun updateSunsetTime(lat : Double, lng : Double){
        viewModelScope.launch {
            repository.getSunsetTime(lat, lng).collectLatest { result ->
                when(result){
                    is RequestResult.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                    is RequestResult.Success -> {
                        result.data?.let{ sunset ->
                            _sunset.update { sunset }
                        }
                    }
                }
            }
        }
    }

    // This function implements the textbox for parsing the time string
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextFieldWithLabelAndPlaceHolder(onTextChange: (String) -> Unit) {
        var text by remember { mutableStateOf(TextFieldValue("")) }

        TextField(
            value = text,
            onValueChange = {
                text = it
                onTextChange(it.text)
            },
            label = { Text(text = "Parse time value manually") },
            placeholder = { Text(text = "h:mm:ss aa - i.e. 11:47:23 PM") },
        )

    }

    // Text field for entering the esp ip address
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextFieldEspIP(onTextChange: (String) -> Unit) {
        var text2 by remember { mutableStateOf(TextFieldValue("")) }

        TextField(
            value = text2,
            onValueChange = {
                text2 = it
                onTextChange(it.text)
            },
            label = { Text(text = "Type in the ESP IP") },
            placeholder = { Text(text = "0.0.0.0") },
        )

    }
    // Composable function for rendering an update button with auto-update checkbox
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
                    // Trigger the provided onClick function only if the button is enabled
                    if (isButtonEnabled) {
                        onClick()
                    }
                },
                modifier = Modifier,
                enabled = isButtonEnabled // Button becomes grayed out when checkbox is checked.
            ) {
                Text("Update manually")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Checkbox for enabling/disabling auto-update
                /*TODO*/
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text("Auto-update // TODO")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

