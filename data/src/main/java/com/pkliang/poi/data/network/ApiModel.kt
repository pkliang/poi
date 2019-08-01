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
    @SerialName("description") val description: String? = null,
    @SerialName("touched") val touched: String,
    @SerialName("images") val images: List<ImageApiResponse>
)

@Serializable
data class ImageInfoPagesApiResponse(
    @SerialName("title") val title: String,
    @SerialName("imageinfo") val imageInfoApiResponse: List<ImageInfoApiResponse>
)

@Serializable
data class ImageInfoApiResponse(
    @SerialName("url") val url: String,
    @SerialName("descriptionurl") val descriptionurl: String,
    @SerialName("descriptionshorturl") val descriptionshorturl: String
)

@Serializable
data class QueryApiResponse<P>(
    @SerialName("geosearch") val geosearch: List<ArticleApiResponse>? = null,
    @SerialName("pages") val pages: P? = null
)

@Serializable
data class ArticleActionApiResponse(
    @SerialName("query") val query: QueryApiResponse<Map<Long, ArticleDetailApiResponse>>
)

@Serializable
data class ImageInfoActionApiResponse(
    @SerialName("query") val query: QueryApiResponse<Map<String, ImageInfoPagesApiResponse>>
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
