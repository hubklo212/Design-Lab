package com.hubklo212.sunsetled.data.model


import com.google.gson.annotations.SerializedName

data class Results(

    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("timezone")
    val timezone: String,

)