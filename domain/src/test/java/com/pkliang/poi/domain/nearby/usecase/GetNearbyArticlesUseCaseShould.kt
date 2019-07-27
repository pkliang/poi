package com.pkliang.poi.domain.nearby.usecase

import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.repository.ArticleRepository
import com.pkliang.poi.domain.nearby.repository.GeolocationRepository
import com.pkliang.poi.testutils.SchedulerMock
import com.pkliang.poi.testutils.assertResult
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Test

class GetNearbyArticlesUseCaseShould {
    private val scheduleMock = SchedulerMock()
    private val articleRepository = mockk<ArticleRepository>()
    private val geolocationRepository = mockk<GeolocationRepository>()

    private val getNearbyArticleUseCase =
        GetNearbyArticlesUseCase(geolocationRepository, articleRepository, scheduleMock)

    @Test
    fun `call getCurrentGeoLocation() and getNearbyArticleByGeoLocation() when invoked`() {
        val geolocation = mockk<Geolocation>()
        val articles = mockk<List<Article>>()
        every {
            geolocationRepository.getCurrentGeolocation()
        } returns Observable.just(geolocation)

        every {
            articleRepository.getNearbyArticlesByGeoLocation(any())
        } returns Observable.just(articles)


        getNearbyArticleUseCase(Unit).subscribe()


        verify {
            geolocationRepository.getCurrentGeolocation()
            articleRepository.getNearbyArticlesByGeoLocation(geolocation)
        }
        confirmVerified(geolocationRepository, articleRepository)
    }

    @Test
    fun `receive articles when invoked`() {
        val geolocation = mockk<Geolocation>()
        val articles = mockk<List<Article>>()
        every {
            geolocationRepository.getCurrentGeolocation()
        } returns Observable.just(geolocation)

        every {
            articleRepository.getNearbyArticlesByGeoLocation(any())
        } returns Observable.just(articles)


        getNearbyArticleUseCase(Unit).assertResult { it == articles }
    }
}
