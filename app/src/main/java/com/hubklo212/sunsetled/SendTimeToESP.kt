package com.hubklo212.sunsetled
/*
 * File: SendTimeToESP.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: Utility class for handling location-related operations. It provides a function
 *              to fetch the last known location using the FusedLocationProviderClient, checking
 *              for location permissions and requesting them if necessary. The fetched location
 *              is then passed to the provided onSuccess lambda.
 */

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

fun sendDataToESP(espIP : String, dataToSend: String, onResult: (String) -> Unit) {
    val urlString = "http://$espIP/receiveData" // Replace with your ESP8266 IP address

    Thread {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true

            // Send data

            val outputStream: OutputStream = connection.outputStream
            outputStream.write("data=$dataToSend".toByteArray())
            Log.d("POST", "sending")
            outputStream.flush()
            outputStream.close()

            // Get response from server
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val result = "Data sent successfully"
                Log.d("POST", "$result. Response Code: $responseCode")
                Handler(Looper.getMainLooper()).post {
                    onResult(result)
                }
            } else {
                val result = "Failed to send data. Response code: $responseCode"
                Log.d("POST", result)
                Handler(Looper.getMainLooper()).post {
                    onResult(result)
                }
            }
        } catch (e: Exception) {
            val result = "Exception: ${e.message}"
            Log.d("POST", "Result: $result")
            Handler(Looper.getMainLooper()).post {
                onResult(result)
            }
        }
    }.start()
}
