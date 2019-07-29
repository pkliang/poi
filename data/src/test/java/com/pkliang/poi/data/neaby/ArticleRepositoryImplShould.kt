package com.pkliang.poi.data.neaby

import com.pkliang.poi.data.network.ArticleActionApiResponse
import com.pkliang.poi.data.network.ArticleApiResponse
import com.pkliang.poi.data.network.MediaWikiRetrofitService
import com.pkliang.poi.data.network.QueryApiResponse
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.testutils.assertResult
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class ArticleRepositoryImplShould {

    private val mediaWikiRetrofitService = mockk<MediaWikiRetrofitService>()
    private val articleRepository = ArticleRepositoryImpl(mediaWikiRetrofitService)

    private val actionApiResponse = ArticleActionApiResponse(
        QueryApiResponse(listOf(ArticleApiResponse(1, "title", 0.0, 0.0, 0.0)))
    )
    private val geolocation = Geolocation(0.0, 0.0)

    @Before
    fun setup() {
        every {
            mediaWikiRetrofitService.getArticlesByGscoord(gscoord = any())
        } returns Observable.just(actionApiResponse)
    }

    @Test
    fun `call getArticlesByGeoLocation when getNearbyArticlesByGeoLocation called`() {
        articleRepository.getNearbyArticlesByGeoLocation(geolocation).subscribe()


        verify { mediaWikiRetrofitService.getArticlesByGscoord(gscoord = any()) }
        confirmVerified(mediaWikiRetrofitService)
    }

    @Test
    fun `receive articles when getNearbyArticlesByGeoLocation called`() {
        articleRepository.getNearbyArticlesByGeoLocation(geolocation).assertResult {
            it == listOf(Article(1, "title", geolocation, 0))
        }
    }
}
