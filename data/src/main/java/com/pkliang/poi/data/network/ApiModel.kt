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
data class ArticleDetailApiResponse(
    @SerialName("pageid") val pageId: Long,
    @SerialName("title") val title: String,
    @SerialName("coordinates") val coordinates: List<CoordinatesApiResponse>,
    @SerialName("description") val description: String,
    @SerialName("touched") val touched: String,
    @SerialName("images") val images: List<ImageApiResponse>
)

@Serializable
data class QueryApiResponse(
    @SerialName("geosearch") val geosearch: List<ArticleApiResponse>? = null,
    @SerialName("pages") val pages: Map<Long, ArticleDetailApiResponse>? = null
)

@Serializable
data class ActionApiResponse(
    @SerialName("query") val query: QueryApiResponse
)

@Serializable
data class CoordinatesApiResponse(
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double
)

@Serializable
data class ImageApiResponse(
    @SerialName("title") val title: String,
    @SerialName("ns") val ns: Int
)
