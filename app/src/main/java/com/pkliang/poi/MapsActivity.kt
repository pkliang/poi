package com.pkliang.poi

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.pkliang.poi.core.PermissionDeniedDialog
import com.pkliang.poi.core.isPermissionGranted
import com.pkliang.poi.core.requestPermission
import com.pkliang.poi.core.view.StateData
import com.pkliang.poi.domain.nearby.entity.Article
import com.pkliang.poi.domain.nearby.entity.ArticleDetails
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.article_details.*
import kotlinx.android.synthetic.main.item_image.view.*
import javax.inject.Inject

class MapsActivity : AppCompatActivity(),
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener,
    OnMapReadyCallback {

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * [.onRequestPermissionsResult].
     */
    private var permissionDenied = false
    private lateinit var map: GoogleMap
    private lateinit var behavior: BottomSheetBehavior<View>

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

        behavior = BottomSheetBehavior.from(bottom_sheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener(this)
        enableMyLocation()
    }

    override fun onMapClick(latLng: LatLng) {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onMyLocationClick(location: Location) {
        mapViewModel.getNearbyArticles()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        mapViewModel.getArticleDetails(marker.snippet.toLong())
        marker.showInfoWindow()
        return true
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
                ).title(it.title).snippet(it.id.toString())
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
            mapViewModel.mapState.observe(this, Observer { renderMap(it) })
            mapViewModel.articleDetailsState.observe(this, Observer { renderBottomSheet(it) })
        }
    }

    private fun renderBottomSheet(it: StateData) =
        when (it) {
            is StateData.Success -> {
                detailsProgressBar.visibility = View.GONE
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                val articleDetails: ArticleDetails = it.responseTo()
                detailsTitle.text = articleDetails.title
                detailsDescription.text = articleDetails.description

                val imageList = articleDetails.images?.filter { url -> !url.contains("svg") }
                if (imageList.isNullOrEmpty()) {
                    recyclerView.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.adapter = ImageAdapter(imageList)
                }
                detailsWikiLink.text = articleDetails.wikiLink
            }
            is StateData.Uninitialized -> {
                detailsProgressBar.visibility = View.GONE
            }
            is StateData.Loading -> {
                detailsProgressBar.visibility = View.VISIBLE
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                detailsTitle.text = ""
            }
            is StateData.Error -> {
                detailsProgressBar.visibility = View.GONE
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                Snackbar.make(container, getString(R.string.error_message), Snackbar.LENGTH_LONG).show()
            }
        }

    private fun renderMap(it: StateData) =
        when (it) {
            is StateData.Success -> {
                progressBar.visibility = View.GONE
                markPOI(it.responseTo())
            }
            is StateData.Uninitialized -> {
                progressBar.visibility = View.GONE
                mapViewModel.getNearbyArticles()
            }
            is StateData.Loading -> {
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                progressBar.visibility = View.VISIBLE
            }
            is StateData.Error -> {
                map.clear()
                progressBar.visibility = View.GONE
                Snackbar.make(container, getString(R.string.error_message), Snackbar.LENGTH_LONG).show()
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

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_image, parent, false)) {
        internal val image: ImageView = itemView.image
    }

    private inner class ImageAdapter internal constructor(private val images: List<String>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Picasso.get().load(images[position]).placeholder(R.mipmap.ic_launcher).noFade().into(holder.image)
        }

        override fun getItemCount(): Int {
            return images.size
        }
    }
}
