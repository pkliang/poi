package com.pkliang.poi.data.network

import com.pkliang.poi.data.neaby.toGsCoord
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.testutils.MockWebServerRule
import com.pkliang.poi.testutils.RetrofitMock
import com.pkliang.poi.testutils.testFirstValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MediaWikiRetrofitServiceShould {

    @Rule
    @JvmField
    val mockWebServerRule = MockWebServerRule()

    private lateinit var mediaWikiRetrofitService: MediaWikiRetrofitService

    private val geolocation = Geolocation(60.1831906, 24.9285439)
    private val pageId = "18806750"
    private val titles = "File:Helsinki.vaakuna.svg|File:KristusKyrkanHelsinki.jpg"

    @Before
    fun setUp() {
        mediaWikiRetrofitService = RetrofitMock.create(mockWebServerRule.getBaseUrl())
    }

    @Test
    fun `send getArticlesByGscoord request to the correct endpoint`() {
        mockWebServerRule.givenMockResponse(fileName = "gssearch.json")


        mediaWikiRetrofitService.getArticlesByGscoord(gscoord = geolocation.toGsCoord()).test()


        mockWebServerRule.assertGetRequestSentTo("/api.php?action=query&list=geosearch&format=json&gsradius=10000&gscoord=60.1831906%7C24.9285439&gslimit=50")
    }

    @Test
    fun `receive parsed ArticleActionApiResponse on the getArticlesByGscoord request`() {
        mockWebServerRule.givenMockResponse(fileName = "gssearch.json")


        val actionApiResponse = mediaWikiRetrofitService.getArticlesByGscoord(gscoord = geolocation.toGsCoord()).testFirstValue()


        assertArticleActionApiResponseByGscoord(actionApiResponse)
    }

    @Test
    fun `send getArticlesByPageIds request to the correct endpoint`() {
        mockWebServerRule.givenMockResponse(fileName = "pages.json")


        mediaWikiRetrofitService.getArticlesByPageIds(pageId).test()


        mockWebServerRule.assertGetRequestSentTo("/api.php?action=query&prop=info|description|images|coordinates&format=json&pageids=$pageId")
    }

    @Test
    fun `receive parsed ArticleActionApiResponse on the getArticlesByPageIds request`() {
        mockWebServerRule.givenMockResponse(fileName = "pages.json")


        val actionApiResponse = mediaWikiRetrofitService.getArticlesByPageIds(pageId).testFirstValue()


        assertArticleActionApiResponseByPageIds(actionApiResponse)
    }

    @Test
    fun `send getImageInfoByTitles request to the correct endpoint`() {
        mockWebServerRule.givenMockResponse(fileName = "imageinfo.json")


        mediaWikiRetrofitService.getImageInfoByTitles(titles).test()


        mockWebServerRule.assertGetRequestSentTo("/api.php?action=query&prop=imageinfo&iiprop=url&format=json&titles=File%3AHelsinki.vaakuna.svg%7CFile%3AKristusKyrkanHelsinki.jpg")
    }

    @Test
    fun `receive parsed ImageInfoActionApiResponse on the getImageInfoByTitles request`() {
        mockWebServerRule.givenMockResponse(fileName = "imageinfo.json")


        val actionApiResponse = mediaWikiRetrofitService.getImageInfoByTitles(titles).testFirstValue()


        assertImageInfoActionApiResponseByTitles(actionApiResponse)
    }
}
