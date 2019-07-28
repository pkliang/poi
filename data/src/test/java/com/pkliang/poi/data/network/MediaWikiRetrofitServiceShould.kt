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

    @Before
    fun setUp() {
        mediaWikiRetrofitService = RetrofitMock.create(mockWebServerRule.getBaseUrl())
    }

    @Test
    fun `send getArticlesByGeoLocation request to correct endpoint`() {
        mockWebServerRule.givenMockResponse(fileName = "gssearch.json")


        mediaWikiRetrofitService
            .getArticlesByGeoLocation(gscoord = geolocation.toGsCoord()).test()


        mockWebServerRule.assertGetRequestSentTo("/api.php?action=query&list=geosearch&gsradius=10000&gscoord=60.1831906%7C24.9285439&gslimit=50&format=json")
    }

    @Test
    fun `receive Parsed ActionApiResponse on getArticlesByGeoLocation request`() {
        mockWebServerRule.givenMockResponse(fileName = "gssearch.json")


        val actionApiResponse = mediaWikiRetrofitService
            .getArticlesByGeoLocation(gscoord = geolocation.toGsCoord()).testFirstValue()


        assertActionApiResponse(actionApiResponse)
    }
}
