package com.hubklo212.sunsetled
/*
 * File: SendTimeToESP.kt
 * Author: hubklo212
 * Date: Jan 2023
 * Description: This Kotlin file provides a function, sendDataToESP.
 *              The function sends data (in our case the time string from MainActivity)
 *              to an ESP8266 device using HTTP POST in a background
 *              thread, handling exceptions and logging results. It ensures non-blocking UI by
 *              utilizing a Handler to post outcomes back to the main thread.
 */

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

// Function to send data to an ESP8266 device
fun sendDataToESP(espIP : String, dataToSend: String, onResult: (String) -> Unit) {

    // Construct the URL for the ESP8266 server
    val urlString = "http://$espIP/receiveData" // Replace with your ESP8266 IP address

    // Start a new background thread to perform the network operation
    Thread {
        try {
            val url = URL(urlString) // Create a URL object from the string
            val connection = url.openConnection() as HttpURLConnection // Open a connection to the URL
            connection.requestMethod = "POST"
            connection.doOutput = true

            // Send data to the server
            val outputStream: OutputStream = connection.outputStream
            outputStream.write("data=$dataToSend".toByteArray())
            Log.d("POST", "sending")
            outputStream.flush()
            outputStream.close()

            // Get response from server
            val responseCode = connection.responseCode

            // Check if the data was sent successfully (HTTP_OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val result = "Data sent successfully"
                Log.d("POST", "$result. Response Code: $responseCode")
                Handler(Looper.getMainLooper()).post { // Use a Handler to post the result back to the main (UI) thread
                    onResult(result)
                }
            } else {
                // Handle failure to send data
                val result = "Failed to send data. Response code: $responseCode"
                Log.d("POST", result)
                Handler(Looper.getMainLooper()).post {
                    onResult(result)
                }
            }
        } catch (e: Exception) { // Handle exceptions
            val result = "Exception: ${e.message}"
            Log.d("POST", "Result: $result")
            Handler(Looper.getMainLooper()).post {
                onResult(result)
            }
        }
    }.start() // Start the background thread
}
