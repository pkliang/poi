package com.pkliang.poi.data.repository

import com.pkliang.poi.data.network.ArticleActionApiResponse
import com.pkliang.poi.data.network.ArticleApiResponse
import com.pkliang.poi.data.network.ArticleDetailApiResponse
import com.pkliang.poi.data.network.CoordinatesApiResponse
import com.pkliang.poi.data.network.ImageApiResponse
import com.pkliang.poi.data.network.ImageInfoActionApiResponse
import com.pkliang.poi.data.network.ImageInfoApiResponse
import com.pkliang.poi.data.network.ImageInfoPagesApiResponse
import com.pkliang.poi.data.network.MediaWikiRetrofitService
import com.pkliang.poi.data.network.QueryApiResponse
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.entity.ArticleDetails
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

    private val articleActionApiGeosearchResponse = ArticleActionApiResponse(
        QueryApiResponse(listOf(ArticleApiResponse(1, "title", 1.0, 0.0, 0.0)))
    )

    private val articleActionApiPageIdsResponse = ArticleActionApiResponse(
        QueryApiResponse(
            pages = mapOf(
                1L to ArticleDetailApiResponse(
                    pageId = 1L,
                    title = "title",
                    coordinates = listOf(
                        CoordinatesApiResponse(
                            1.0,
                            0.0
                        )
                    ),
                    description = "description",
                    touched = "touched",
                    images = listOf(
                        ImageApiResponse(
                            title = "imageTitle",
                            ns = 0
                        )
                    )
                )
            )
        )
    )

    private val imageInfoApiResponse = ImageInfoActionApiResponse(
        query = QueryApiResponse(
            pages = mapOf(
                "" to ImageInfoPagesApiResponse(
                    "title", listOf(
                        ImageInfoApiResponse(
                            "imageUrl", "descriptionurl", "descriptionshorturl"
                        )
                    )
                )
            )
        )
    )
    private val geolocation = Geolocation(1.0, 0.0)

    @Before
    fun setup() {
        every {
            mediaWikiRetrofitService.getArticlesByGscoord(gscoord = any())
        } returns Observable.just(articleActionApiGeosearchResponse)

        every {
            mediaWikiRetrofitService.getArticlesByPageIds(any())
        } returns Observable.just(articleActionApiPageIdsResponse)

        every {
            mediaWikiRetrofitService.getImageInfoByTitles(any())
        } returns Observable.just(imageInfoApiResponse)
    }

    @Test
    fun `call getArticlesByGeoLocation when getNearbyArticlesByGeoLocation called`() {
        articleRepository.getNearbyArticlesByGeoLocation(geolocation).subscribe()


        verify { mediaWikiRetrofitService.getArticlesByGscoord(gscoord = "1.0|0.0") }
        confirmVerified(mediaWikiRetrofitService)
    }

    @Test
    fun `receive articles when getNearbyArticlesByGeoLocation called`() {
        articleRepository.getNearbyArticlesByGeoLocation(geolocation).assertResult {
            it == listOf(Article(1, "title", geolocation, 0))
        }
    }

    @Test
    fun `call getArticlesByPageIds and getImageInfoByTitles when getArticleDetails called`() {
        articleRepository.getArticleDetails(1).subscribe()


        verify {
            mediaWikiRetrofitService.getArticlesByPageIds("1")
            mediaWikiRetrofitService.getImageInfoByTitles("imageTitle")
        }
        confirmVerified(mediaWikiRetrofitService)
    }

    @Test
    fun `receive article details when getArticleDetails called`() {
        articleRepository.getArticleDetails(1).assertResult {
            it == ArticleDetails(
                id = 1,
                title = "title",
                wikiLink = "https://en.wikipedia.org/wiki/title",
                description = "description",
                geolocation = geolocation,
                images = listOf("imageUrl")
            )
        }
    }
}
