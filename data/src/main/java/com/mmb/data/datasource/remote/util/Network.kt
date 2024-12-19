package com.mmb.data.datasource.remote.util

import com.mmb.domain.excpetion.ApiError
import java.io.IOException
import java.net.SocketTimeoutException
import retrofit2.HttpException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> = try {
    Result.success(apiCall())
} catch (e: HttpException) {
    Result.failure(ApiError.NetworkError)
} catch (e: SocketTimeoutException) {
    Result.failure(ApiError.Timeout)
} catch (e: IOException) {
    Result.failure(ApiError.NetworkError)
} catch (e: Exception) {
    Result.failure(ApiError.UnknownError)
}