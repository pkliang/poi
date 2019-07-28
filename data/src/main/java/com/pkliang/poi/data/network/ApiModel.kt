package com.pkliang.poi.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleApiResponse(
    @SerialName("pageid") val pageId: Long,
    @SerialName("title") val title: String,
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double,
    @SerialName("dist") val distance: Double
)

@Serializable
data class QueryApiResponse(
    @SerialName("geosearch") val list: List<ArticleApiResponse>
)

@Serializable
data class ActionApiResponse(
    @SerialName("query") val query: QueryApiResponse
)
