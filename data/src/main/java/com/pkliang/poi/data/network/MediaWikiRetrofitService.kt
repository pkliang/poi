package com.pkliang.poi.data.network

import com.pkliang.poi.data.network.NetworkConstants.Api.ACTION_ENDPOINT
import com.pkliang.poi.data.network.NetworkConstants.Api.ACTION_QUERY
import com.pkliang.poi.data.network.NetworkConstants.Api.FORMAT_JSON
import com.pkliang.poi.data.network.NetworkConstants.Api.IIPRPO_URL
import com.pkliang.poi.data.network.NetworkConstants.Api.LIST_GEOSEARCH
import com.pkliang.poi.data.network.NetworkConstants.Api.PROPS
import com.pkliang.poi.data.network.NetworkConstants.Api.PROP_IMAGE_INFO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaWikiRetrofitService {

    @GET("$ACTION_ENDPOINT?$ACTION_QUERY&$LIST_GEOSEARCH&$FORMAT_JSON")
    fun getArticlesByGscoord(
        @Query("gsradius") gsradius: Int = 10000,
        @Query("gscoord") gscoord: String,
        @Query("gslimit") gslimit: Int = 50
    ): Observable<ArticleActionApiResponse>

    @GET("$ACTION_ENDPOINT?$ACTION_QUERY&$PROPS&$FORMAT_JSON")
    fun getArticlesByPageIds(@Query("pageids") pageids: String): Observable<ArticleActionApiResponse>

    @GET("$ACTION_ENDPOINT?$ACTION_QUERY&$PROP_IMAGE_INFO&$IIPRPO_URL&$FORMAT_JSON")
    fun getImageInfoByTitles(@Query("titles") titles: String): Observable<ImageInfoActionApiResponse>
}
