package com.pkliang.poi.data.repository

import android.location.Location
import com.pkliang.poi.data.network.ArticleActionApiResponse
import com.pkliang.poi.data.network.ArticleApiResponse
import com.pkliang.poi.data.network.ArticleDetailApiResponse
import com.pkliang.poi.data.network.CoordinatesApiResponse
import com.pkliang.poi.data.network.ImageApiResponse
import com.pkliang.poi.data.network.ImageInfoActionApiResponse
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.entity.ArticleDetails

fun Geolocation.toGsCoord(): String = "$lat|$lon"

fun ArticleActionApiResponse.toArticles(): List<Article>? =
    query.geosearch?.map {
        it.toArticle()
    }

private fun ArticleApiResponse.toArticle(): Article =
    Article(
        id = pageId,
        title = title,
        geolocation = Geolocation(lat, lon),
        distance = distance.toInt()
    )

fun Location.toGeoLocation() = Geolocation(latitude, longitude)

fun List<ImageApiResponse>.toTitles(): String =
    joinToString("|") {
        it.title
    }

fun CoordinatesApiResponse.toGeolocation(): Geolocation = Geolocation(lat, lon)

fun toArticleDetails(
    details: ArticleDetailApiResponse,
    imageInfo: ImageInfoActionApiResponse
): ArticleDetails = ArticleDetails(
    details.pageId,
    details.title,
    "https://en.wikipedia.org/wiki/" + details.title.replace(" ", "_"),
    details.description,
    details.coordinates.firstOrNull()?.toGeolocation(),
    imageInfo.query.pages?.values?.toList()?.map {
        it.imageInfoApiResponse.first().url
    }
)
