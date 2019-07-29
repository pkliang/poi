package com.pkliang.poi.testutils

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

open class MockWebServerRule : TestRule {

    companion object {
        private const val GET = "GET"
        private const val POST = "POST"
        private const val PATCH = "PATCH"
        private const val PUT = "PUT"
        private const val DELETE = "DELETE"
        private const val OK_200 = 200
    }

    val server = MockWebServer()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                server.start()
                base.evaluate()
                server.shutdown()
            }
        }
    }

    fun getBaseUrl(path: String = "/") = server.url(path).toString()

    fun givenMockResponse(responseCode: Int = OK_200, fileName: String? = null) {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(responseCode)
        val fileResponse = ResourceLoader.loadContentFromFile(fileName)
        mockResponse.setBody(fileResponse)
        server.enqueue(mockResponse)
    }

    fun assertGetRequestSentTo(url: String) = getTakeRequest().run {
        assertThat(path, equalTo(url))
        assertThat(method, equalTo(GET))
    }

    fun assertPostRequestSentTo(url: String) = getTakeRequest().run {
        assertThat(path, equalTo(url))
        assertThat(method, equalTo(POST))
    }

    fun assertPatchRequestSentTo(url: String) = getTakeRequest().run {
        assertThat(path, equalTo(url))
        assertThat(method, equalTo(PATCH))
    }

    fun assertPutRequestSentTo(url: String) = getTakeRequest().run {
        assertThat(path, equalTo(url))
        assertThat(method, equalTo(PUT))
    }

    fun assertDeleteRequestSentTo(url: String) = getTakeRequest().run {
        assertThat(path, equalTo(url))
        assertThat(method, equalTo(DELETE))
    }

    fun assertRequestContainsHeader(key: String, expectedValue: String, requestIndex: Int = 0) {
        val recordedRequest = getRecordedRequestByIndex(requestIndex)
        val value = recordedRequest?.getHeader(key)
        assertThat(value, equalTo(expectedValue))
    }

    fun assertRequestBodyEquals(fileName: String) = getTakeRequest().run {
        assertThat(body.readUtf8(), equalTo(ResourceLoader.loadContentFromFile(fileName)))
    }

    private fun getRecordedRequestByIndex(requestIndex: Int): RecordedRequest? =
        (0..requestIndex).map { getTakeRequest() }.last()

    private fun getTakeRequest() = server.takeRequest()
}
