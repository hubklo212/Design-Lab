package com.hubklo212.sunsetled
/*
 * File: RetrofitInstance.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: Singleton object responsible for providing a configured Retrofit instance
 *              with an OkHttpClient and a GsonConverterFactory for API communication.
 */

import com.hubklo212.sunsetled.data.Api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Configure logging interceptor for HTTP request/response logging.
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configure OkHttpClient with the logging interceptor.
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    // Create and configure Retrofit instance for API communication.
    val api: Api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Api.BASE_URL)
        .client(client)
        .build()
        .create(Api::class.java)

}