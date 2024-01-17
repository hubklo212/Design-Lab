package com.hubklo212.sunsetled.data
/*
 * File: Repository.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: Interface defining the contract for repositories handling sunset-related data.
 *              It declares a single function (defined in RepositoryImpl.kt file)
 *              to asynchronously fetch sunset time data as a Flow of RequestResult<GeneralInfo>.
 */
import com.hubklo212.sunsetled.data.model.GeneralInfo
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getSunsetTime(lat : Double, lng : Double): Flow<RequestResult<GeneralInfo>>
}