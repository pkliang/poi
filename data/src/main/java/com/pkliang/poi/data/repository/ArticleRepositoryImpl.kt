package com.pkliang.poi.data.repository

import com.pkliang.poi.data.network.MediaWikiRetrofitService
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.entity.ArticleDetails
import com.pkliang.poi.domain.nearby.repository.ArticleRepository
import io.reactivex.Observable
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(private val mediaWikiRetrofitService: MediaWikiRetrofitService) :
    ArticleRepository {
    override fun getArticleDetails(id: Long): Observable<ArticleDetails> {
        return mediaWikiRetrofitService.getArticlesByPageIds(id.toString())
            .map { it.query.pages?.get(id) }
            .flatMap { details ->
                mediaWikiRetrofitService.getImageInfoByTitles(details.images.toTitles()).map { imageInfo ->
                    toArticleDetails(details, imageInfo)
                }
            }
    }

    override fun getNearbyArticlesByGeoLocation(geolocation: Geolocation): Observable<List<Article>> =
        mediaWikiRetrofitService.getArticlesByGscoord(
            gscoord = geolocation.toGsCoord()
        ).map { it.toArticles() }
}
