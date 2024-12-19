package com.mmb.data.datasource.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityResponse(
    @SerialName("name")
    val name: String,
)