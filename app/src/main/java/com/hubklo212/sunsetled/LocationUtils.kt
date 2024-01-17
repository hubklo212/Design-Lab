package com.hubklo212.sunsetled
/*
 * File: LocationUtils.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: Utility class for handling location-related operations. It provides a function
 *              to fetch the last known location using the FusedLocationProviderClient, checking
 *              for location permissions and requesting them if necessary. The fetched location
 *              is then passed to the provided onSuccess lambda.
 */

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

object LocationUtils {

    fun fetchLocation(context: Context,
                      fusedLocationProviderClient: FusedLocationProviderClient,
                      onSuccess: (Double, Double) -> Unit) {

        // Obtain the last known location task from the FusedLocationProviderClient
        val task = fusedLocationProviderClient.lastLocation

        // Check for location permissions, request if not granted, and return if not granted
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        // Add a listener to the task to handle the result (last known location)
        task.addOnSuccessListener {
            if (it != null) {
                // If the location is available, invoke the provided onSuccess lambda
                onSuccess(it.latitude, it.longitude)
            } else {
                // Handle the case where the last known location is null and display a toast message
                Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
