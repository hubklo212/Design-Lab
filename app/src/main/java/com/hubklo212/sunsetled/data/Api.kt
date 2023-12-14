package com.hubklo212.sunsetled.data

import com.hubklo212.sunsetled.data.model.GeneralInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
//?lat={latitude}&lng={longitude}
    @GET("json"/*?lat=50.0&lng=19.9*/)
    suspend fun getSunset(@Query("lat") latitude: Double,
                          @Query("lng") longitude: Double
    ): GeneralInfo

    companion object {
        const val BASE_URL = "https://api.sunrisesunset.io/"
    }
}