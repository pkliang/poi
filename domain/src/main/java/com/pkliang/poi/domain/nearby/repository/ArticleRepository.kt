package com.pkliang.poi.domain.nearby.repository

import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.entity.ArticleDetails
import io.reactivex.Observable

interface ArticleRepository {
    fun getNearbyArticlesByGeoLocation(geolocation: Geolocation): Observable<List<Article>>
    fun getArticleDetails(id: Long): Observable<ArticleDetails>
}
