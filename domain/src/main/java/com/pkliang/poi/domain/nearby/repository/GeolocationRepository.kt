package com.pkliang.poi.domain.nearby.repository

import com.pkliang.poi.domain.core.entity.Geolocation
import io.reactivex.Observable

interface GeolocationRepository {
    fun getCurrentGeolocation(): Observable<Geolocation>
}
