package com.hubklo212.sunsetled.data
/*
 * File: RequestResult.kt
 * Author: hubklo212
 * Date: Dec 2023
 * Description: A sealed class representing the result of an operation that can either
 *              be successful or an error. It includes a generic type 'T' for the result
 *              data and optional message for error cases.
 */
sealed class RequestResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    // Represents a successful result with optional data.
    class Success<T>(data:T?):RequestResult<T>(data)

    // Represents an error result with optional data and an error message.
    class Error<T>(data:T? = null, message: String):RequestResult<T>(data, message)
}