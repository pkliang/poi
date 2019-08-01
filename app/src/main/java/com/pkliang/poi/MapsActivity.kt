package com.pkliang.poi

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.pkliang.poi.core.PermissionDeniedDialog
import com.pkliang.poi.core.isPermissionGranted
import com.pkliang.poi.core.requestPermission
import com.pkliang.poi.core.view.StateData
import com.pkliang.poi.domain.nearby.entity.Article
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject

class MapsActivity : AppCompatActivity(),
    GoogleMap.OnMyLocationClickListener,
    OnMapReadyCallback {

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * [.onRequestPermissionsResult].
     */
    private var permissionDenied = false
    private lateinit var map: GoogleMap

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mapViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        AndroidInjection.inject(this)
        mapViewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationClickListener(this)
        enableMyLocation()
    }

    override fun onMyLocationClick(location: Location) {
        mapViewModel.getNearbyArticles()
    }

    private fun markPOI(list: List<Article>) {
        map.clear()
        list.forEach {
            map.addMarker(
                MarkerOptions().position(
                    LatLng(
                        it.geolocation.lat,
                        it.geolocation.lon
                    )
                ).title(it.title)
            )
        }
        list.firstOrNull()?.apply {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(geolocation.lat, geolocation.lon), ZOOM))
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission to access the location is missing.
            requestPermission(
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                true
            )
        } else {
            // Access to the location has been granted to the app.
            map.isMyLocationEnabled = true
            mapViewModel.mapState.observe(this, Observer {
                when (it) {
                    is StateData.Success -> {
                        progressBar.visibility = View.GONE
                        markPOI(it.responseTo())
                    }
                    is StateData.Uninitialized -> {
                        progressBar.visibility = View.GONE
                        mapViewModel.getNearbyArticles()
                    }
                    is StateData.Loading -> progressBar.visibility = View.VISIBLE
                    is StateData.Error -> {
                        map.clear()
                        progressBar.visibility = View.GONE
                        Snackbar.make(container, it.error.localizedMessage, Snackbar.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() =
        PermissionDeniedDialog.newInstance(true).show(supportFragmentManager, "dialog")

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val ZOOM = 12.0f
    }
}
