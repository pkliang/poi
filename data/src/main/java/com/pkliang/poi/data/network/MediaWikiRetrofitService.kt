package com.pkliang.poi.data.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaWikiRetrofitService {

    @GET("$API?$ACTION_QUERY&$LIST_GEOSEARCH&$FORMAT_JSON")
    fun getArticlesByGeoLocation(
        @Query("gsradius") gsradius: Int = 10000,
        @Query("gscoord") gscoord: String,
        @Query("gslimit") gslimit: Int = 50
    ): Observable<ActionApiResponse>

    @GET("$API?$ACTION_QUERY&$PROPS&$FORMAT_JSON")
    fun getArticleDetails(@Query("pageids") pageids: String): Observable<ActionApiResponse>

    companion object {
        const val API = "api.php"
        const val ACTION_QUERY = "action=query"
        const val LIST_GEOSEARCH = "list=geosearch"
        const val FORMAT_JSON = "format=json"
        const val PROPS = "prop=info|description|images|coordinates"
    }
}
