package com.pkliang.poi.domain.nearby.entity

import com.pkliang.poi.domain.core.entity.Geolocation

data class ArticleDetails(
    val id: Long,
    val title: String,
    val wikiLink: String,
    val description: String?,
    val geolocation: Geolocation?,
    val images: List<String>?
)
