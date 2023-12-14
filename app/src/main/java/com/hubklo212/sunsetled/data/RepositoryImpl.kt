package com.hubklo212.sunsetled.data

import android.util.Log
import com.hubklo212.sunsetled.data.model.GeneralInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class RepositoryImpl(
    private val api: Api
): Repository{

    override suspend fun getSunsetTime(lat : Double, lng : Double): Flow<Result<GeneralInfo>> {
        return flow{
            val dataFromApi = try{
                api.getSunset(lat,lng)
            } catch(e: IOException){
                e.printStackTrace()
                emit(Result.Error(message = "Error loading data"))
                return@flow
            } catch(e:HttpException){
                e.printStackTrace()
                emit(Result.Error(message = "Error loading data"))
                return@flow
            } catch(e:Exception){
                e.printStackTrace()
                emit(Result.Error(message = "Error loading data"))
                return@flow
            }
            Log.d("PROCESS_D", "COLLECTING ${dataFromApi.results.sunset}")
            emit(Result.Success(dataFromApi))
        }
    }
}