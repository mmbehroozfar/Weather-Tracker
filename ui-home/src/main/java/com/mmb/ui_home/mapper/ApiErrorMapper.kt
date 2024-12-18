package com.mmb.ui_home.mapper

import com.mmb.domain.excpetion.ApiError.NetworkError
import com.mmb.domain.excpetion.ApiError.Timeout
import com.mmb.domain.excpetion.ApiError.UnknownError
import com.mmb.ui_home.R
import javax.inject.Inject

class ApiErrorMapper @Inject constructor() {

    fun map(from: Throwable) = when (from) {
        NetworkError -> R.string.no_network_error
        Timeout -> R.string.time_out_error
        UnknownError -> R.string.unknown_error
        else -> R.string.unknown_error
    }
}