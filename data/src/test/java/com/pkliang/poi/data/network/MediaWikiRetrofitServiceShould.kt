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

    @Before
    fun setUp() {
        mediaWikiRetrofitService = RetrofitMock.create(mockWebServerRule.getBaseUrl())
    }

    @Test
    fun `send getArticlesByGeoLocation request to the correct endpoint`() {
        mockWebServerRule.givenMockResponse(fileName = "gssearch.json")


        mediaWikiRetrofitService.getArticlesByGeoLocation(gscoord = geolocation.toGsCoord()).test()


        mockWebServerRule.assertGetRequestSentTo("/api.php?action=query&list=geosearch&format=json&gsradius=10000&gscoord=60.1831906%7C24.9285439&gslimit=50")
    }

    @Test
    fun `receive parsed ActionApiResponse on the getArticlesByGeoLocation request`() {
        mockWebServerRule.givenMockResponse(fileName = "gssearch.json")


        val actionApiResponse = mediaWikiRetrofitService.getArticlesByGeoLocation(gscoord = geolocation.toGsCoord()).testFirstValue()


        assertActionApiResponseForGsSearch(actionApiResponse)
    }

    @Test
    fun `send getArticleDetails request to the correct endpoint`() {
        mockWebServerRule.givenMockResponse(fileName = "pages.json")


        mediaWikiRetrofitService.getArticleDetails(pageId).test()


        mockWebServerRule.assertGetRequestSentTo("/api.php?action=query&prop=info|description|images|coordinates&format=json&pageids=$pageId")
    }

    @Test
    fun `receive parsed ActionApiResponse on the getArticleDetails request`() {
        mockWebServerRule.givenMockResponse(fileName = "pages.json")


        val actionApiResponse = mediaWikiRetrofitService.getArticleDetails(pageId).testFirstValue()


        assertActionApiResponseForPages(actionApiResponse)
    }
}
