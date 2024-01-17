package com.hubklo212.sunsetled.data
/*
 * File: Api.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: Retrofit API interface for communicating with an external service
 *              to fetch sunset-related data. It defines a single endpoint for getting
 *              sunset information based on latitude and longitude using the HTTP GET method.
 */
import com.hubklo212.sunsetled.data.model.GeneralInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    // Example API request: https://api.sunrisesunset.io/json?lat=38.907192&lng=-77.036873
    @GET("json")
    suspend fun getSunset(@Query("lat") latitude: Double,
                          @Query("lng") longitude: Double
    ): GeneralInfo

    // Companion object holding the base URL for the API
    companion object {
        const val BASE_URL = "https://api.sunrisesunset.io/"
    }
}