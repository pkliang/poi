package com.pkliang.poi.data.network

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat

fun assertActionApiResponseForGsSearch(actionApiResponse: ActionApiResponse) {
    assertThat(actionApiResponse.query.geosearch, notNullValue())
    assertThat(
        actionApiResponse.query.geosearch?.first(), equalTo(
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

fun assertActionApiResponseForPages(actionApiResponse: ActionApiResponse) {
    assertThat(actionApiResponse.query.pages, notNullValue())
    assertThat(actionApiResponse.query.pages?.keys?.toList()?.first(), equalTo(18806750L))
    assertThat(
        actionApiResponse.query.pages?.values?.toList()?.first(), equalTo(
            ArticleDetailApiResponse(
                pageId = 18806750,
                title = "Töölö Sports Hall",
                touched = "2019-07-26T16:27:03Z",
                coordinates = listOf(CoordinatesApiResponse(60.18333333, 24.92555556)),
                description = "Sports venue in Helsinki, Finland",
                images = listOf(
                    ImageApiResponse("File:Basketball pictogram.svg", 6),
                    ImageApiResponse("File:Boxing pictogram.svg", 6),
                    ImageApiResponse("File:Commons-logo.svg", 6),
                    ImageApiResponse("File:Gymnastics (artistic) pictogram.svg", 6),
                    ImageApiResponse("File:Töölön Kisahalli.jpg", 6),
                    ImageApiResponse("File:Weightlifting pictogram.svg", 6),
                    ImageApiResponse("File:Wrestling pictogram.svg", 6)
                )
            )
        )
    )
}
