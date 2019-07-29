package com.pkliang.poi.data.repository

import com.pkliang.poi.data.network.MediaWikiRetrofitService
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.repository.ArticleRepository
import io.reactivex.Observable

class ArticleRepositoryImpl(private val mediaWikiRetrofitService: MediaWikiRetrofitService) : ArticleRepository {
    override fun getNearbyArticlesByGeoLocation(geolocation: Geolocation): Observable<List<Article>> =
        mediaWikiRetrofitService.getArticlesByGscoord(
            gscoord = geolocation.toGsCoord()
        ).map { it.toArticles() }
}
