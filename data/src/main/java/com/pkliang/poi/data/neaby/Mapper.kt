package com.pkliang.poi.data.neaby

import com.pkliang.poi.data.network.ActionApiResponse
import com.pkliang.poi.data.network.ArticleApiResponse
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.entity.Article

fun Geolocation.toGsCoord(): String = "$lat|$lon"

fun ActionApiResponse.toArticles(): List<Article> =
    query.list.map {
        it.toArticle()
    }

private fun ArticleApiResponse.toArticle(): Article =
    Article(
        id = pageId,
        title = title,
        geolocation = Geolocation(lat, lon),
        distance = distance.toInt()
    )
