package com.hubklo212.sunsetled.data.model
/*
 * File: GeneralInfo.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: Data class representing the 'generalInfo' object received from sunrisesunset.io.
 */

import com.google.gson.annotations.SerializedName

data class GeneralInfo(
    @SerializedName("results")
    val results: Results,

)