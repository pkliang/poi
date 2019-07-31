package com.pkliang.poi.data.repository

import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.domain.nearby.repository.GeolocationRepository
import io.reactivex.Observable
import javax.inject.Inject

class GeolocationRepositoryImpl @Inject constructor(
    private val rxLocation: RxLocation,
    private val locationRequest: LocationRequest
) : GeolocationRepository {
    override fun getCurrentGeolocation(): Observable<Geolocation> =
        rxLocation.settings().checkAndHandleResolution(locationRequest)
            .flatMapObservable { rxLocation.location().updates(locationRequest) }
            .map { it.toGeoLocation() }
}
