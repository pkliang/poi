package com.pkliang.poi.data.network

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

fun assertActionApiResponse(actionApiResponse: ActionApiResponse) {
    assertThat(
        actionApiResponse.query.list.first(), equalTo(
            ArticleApiResponse(
                pageId = 18806750,
                title = "Töölö Sports Hall",
                lat = 60.183333333333,
                lon = 24.925555555556,
                distance = 166.0
            )
        )
    )
}
