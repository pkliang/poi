package com.pkliang.poi.domain.nearby.entity

import com.pkliang.poi.domain.core.entity.Geolocation

data class Article(
    val id: Long,
    val title: String,
    val geolocation: Geolocation,
    val distance: Int
)
