package com.pkliang.poi.data.network

object NetworkConstants {

    object HttpRequest {
        const val HTTP_REQUEST_TIMEOUT_SECONDS: Long = 60
    }

    object Api {
        const val API_URL = "https://en.wikipedia.org/w/"
        const val ACTION_ENDPOINT = "api.php"
        const val ACTION_QUERY = "action=query"
        const val LIST_GEOSEARCH = "list=geosearch"
        const val FORMAT_JSON = "format=json"
        const val PROPS = "prop=info|description|images|coordinates"
        const val PROP_IMAGE_INFO = "prop=imageinfo"
        const val IIPRPO_URL = "iiprop=url"
    }
}
