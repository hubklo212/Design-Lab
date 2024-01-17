package com.hubklo212.sunsetled.data
/*
 * File: RepositoryImpl.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: Implementation of the Repository interface responsible for fetching sunset data
 *              from an external API using Retrofit. It utilizes kotlinx.coroutines.flow to
 *              represent asynchronous data flow and RequestResult class to encapsulate success or
 *              a few most common error outcomes.
 */
import android.util.Log
import com.hubklo212.sunsetled.data.model.GeneralInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class RepositoryImpl(
    private val api: Api
): Repository{

    // Function to fetch sunset time data as a Flow of RequestResult<GeneralInfo>
    override suspend fun getSunsetTime(lat : Double, lng : Double): Flow<RequestResult<GeneralInfo>> {
        return flow{
            val dataFromApi = try{
                // Try fetching sunset data from the API
                api.getSunset(lat,lng)
            } catch(e: IOException){
                // Handle IO exception (network issues) and emit an error RequestResult
                e.printStackTrace()
                emit(RequestResult.Error(message = "Error loading data"))
                return@flow
            } catch(e:HttpException){
                // Handle HTTP exception (non-2xx response) and emit an error RequestResult
                e.printStackTrace()
                emit(RequestResult.Error(message = "Error loading data"))
                return@flow
            } catch(e:Exception){
                // Handle general exceptions and emit an error RequestResult.
                e.printStackTrace()
                emit(RequestResult.Error(message = "Error loading data"))
                return@flow
            }
            // Create a log entry and emit a success RequestResult with the data
            Log.d("PROCESS_D", "COLLECTING ${dataFromApi.results.sunset}")
            emit(RequestResult.Success(dataFromApi))
        }
    }
}