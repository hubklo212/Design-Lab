package com.hubklo212.sunsetled.data.model
/*
 * File: Results.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: Data class representing the 'results' object received from sunrisesunset.io.
 */

import com.google.gson.annotations.SerializedName

data class Results(

    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("timezone")
    val timezone: String,

)