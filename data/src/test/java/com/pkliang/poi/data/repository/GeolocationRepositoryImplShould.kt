package com.pkliang.poi.data.repository

import android.location.Location
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.pkliang.poi.domain.core.entity.Geolocation
import com.pkliang.poi.testutils.assertResult
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GeolocationRepositoryImplShould {

    private val rxLocation = mockk<RxLocation>()
    private val locationRequest = LocationRequest.create()
    private val lat = 1.0
    private val lon = 2.0
    private val geolocationRepository = GeolocationRepositoryImpl(rxLocation, locationRequest)

    @Test
    fun `receive geolocation when getCurrentGeolocation called` () {
        every {
            rxLocation.location().updates(locationRequest)
        } returns Observable.just(Location("").apply {
            latitude = lat
            longitude = lon
        })
        every {
            rxLocation.settings().checkAndHandleResolution(locationRequest)
        } returns Single.just(true)


        geolocationRepository.getCurrentGeolocation().assertResult {
            it == Geolocation(lat, lon)
        }
    }
}
