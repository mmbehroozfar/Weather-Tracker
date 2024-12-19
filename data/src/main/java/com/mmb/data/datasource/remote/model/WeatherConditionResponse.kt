package com.mmb.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherConditionResponse(
    @SerialName("text")
    val description: String,
    @SerialName("icon")
    val iconUrl: String
)