package com.pkliang.poi.data.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaWikiRetrofitService {

    @GET("api.php")
    fun getArticlesByGeoLocation(
        @Query("action") action: String = "query",
        @Query("list") list: String = "geosearch",
        @Query("gsradius") gsradius: Int = 10000,
        @Query("gscoord") gscoord: String,
        @Query("gslimit") gslimit: Int = 50,
        @Query("format") format: String = "json"
    ): Observable<ActionApiResponse>
}
