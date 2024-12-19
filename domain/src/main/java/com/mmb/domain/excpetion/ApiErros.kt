package com.mmb.domain.excpetion

sealed class ApiError : Throwable() {
    data object NetworkError : ApiError()
    data object Timeout : ApiError()
    data object UnknownError : ApiError()
}