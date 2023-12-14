package com.hubklo212.sunsetled.data

import com.hubklo212.sunsetled.data.model.GeneralInfo
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getSunsetTime(lat : Double, lng : Double): Flow<Result<GeneralInfo>>
}