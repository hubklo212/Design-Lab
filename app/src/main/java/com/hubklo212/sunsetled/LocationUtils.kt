package com.hubklo212.sunsetled

// LocationUtils.kt

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

object LocationUtils {

    fun fetchLocation(context: Context, fusedLocationProviderClient: FusedLocationProviderClient, onSuccess: (Double, Double) -> Unit) {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                onSuccess(it.latitude, it.longitude)
            } else {
                // Handle the case where the last known location is null
                Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()

                //https://api.sunrisesunset.io/json?lat="$latitude"&lng="$longitude"
            }
        }
    }
}
