package com.example.share4care

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Service
import com.example.share4care.contentData.Travel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.share4care.databinding.ActivityHomeBinding
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.maps.model.LatLng

import android.location.Geocoder
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.template.androidtemplate.ui.main.RecyclerAdapter
import java.io.IOException


class HomeActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityHomeBinding
    private lateinit var activityType: String
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var recyclerAdapter: RecyclerAdapter

    var markersAll:MutableList<Marker> = mutableListOf()

    var listEvent:MutableList<Event> = mutableListOf(
        Event("Event Marker One","a","Event", "Event Marker One", "2020/10/10", "123 street", 39.150, 152.190, "0123456789", "blah@gmail.com"),
        Event("Event Marker Two","b","Event","Event Marker Two","2020/11/11","456 avenue",45.175, 200.150, "9876543210", "blah@hotmail.com")
    )

    var listService:MutableList<Service> = mutableListOf(
        Service("Service Marker One","c","Event", "Handsign Interpreter", "123 street", 50.150, 185.190, "0123456789", "blah@email.com"),
        Service("Service Marker Two","d","Event", "Transport", "123 street", 75.150, 250.190, "0123456789", "blah@email.com")
    )

    var listTravel:MutableList<Travel> = mutableListOf(
        Travel("Travel Marker One","c","Event", "Accessible Bus Stop", "123 street", 55.150, 200.190,"0123456789", ".@gmail.com"),
        Travel("Travel Marker Two","d","Event", "Accessible Public Toilet", "123 street", 80.150, 275.190, "9876543210", "$$$@email.com")
    )

    var eventData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            val addedEvent=data?.getSerializableExtra(EVENT) as Event
            listEvent.add(addedEvent)
            val latlng=LatLng(addedEvent.latitude, addedEvent.longtitude)
            val newMarker = mMap.addMarker(MarkerOptions()
                        .position(latlng)
                        .title(addedEvent.title)
                        .snippet(addedEvent.description)
                        .icon(bitmapDescriptorFromVector(this, R.drawable.ic_map_pin_filled_orange_48dp,R.drawable.baseline_event_20)))
            markersAll.add(newMarker!!)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))

        }
    }
    var serviceData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            val addedService=data?.getSerializableExtra(SERVICE) as Service
            listService.add(addedService)
            val latlng=LatLng(addedService.latitude, addedService.longtitude)
            val newMarker = mMap.addMarker(MarkerOptions()
                .position(latlng)
                .title(addedService.title)
                .snippet(addedService.description)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_map_pin_filled_blue_48dp,R.drawable.baseline_miscellaneous_services_20)))
            markersAll.add(newMarker!!)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))

        }
    }
    var travelData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            val addedTravel=data?.getSerializableExtra(TRAVEL) as Travel
            listTravel.add(addedTravel)
            val latlng=LatLng(addedTravel.latitude, addedTravel.longtitude)
            val newMarker = mMap.addMarker(MarkerOptions()
                .position(latlng)
                .title(addedTravel.title)
                .snippet(addedTravel.description)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_map_pin_filled_green_48dp,R.drawable.baseline_accessible_20)))
            markersAll.add(newMarker!!)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))

        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        binding.addButton.setOnClickListener(){
            val typeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_choose_type, null)
            val dialog= AlertDialog.Builder(this)
                .setTitle("Select A Type")
                .setView(typeFormView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Continue", null)
                .show()

            val typeSpinner=typeFormView.findViewById<Spinner>(R.id.typeSpinner)
            ArrayAdapter.createFromResource(this, R.array.type_array, android.R.layout.simple_spinner_item)
                .also{adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    typeSpinner.adapter=adapter
                }

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(){
                activityType=typeSpinner.selectedItem.toString()
                when(activityType){
                    "Event" -> {
                        val intent = Intent(this, RegisterEventActivity::class.java)
                        eventData.launch(intent)
                    }
                    "Service" -> {
                        val intent = Intent(this, RegisterServiceActivity::class.java)
                        serviceData.launch(intent)
                    }
                    "Travel" -> {
                        val intent = Intent(this, RegisterTravelActivity::class.java)
                        travelData.launch(intent)
                    }
                }
            }
        }

        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // on below line we are getting the location name from search view.
                val location: String = binding.svLocation.query.toString()

                // below line is to create a list of address where we will store the list of all address.
                var addressList: List<Address>? = null

                // checking if the entered location is null or not.
                if (location != null || location != "") {

                    // on below line we are creating and initializing a geo coder.
                    val geocoder:Geocoder = Geocoder(this@HomeActivity)
                    try {
                        // on below line we are getting location from the location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 2)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    // on below line we are getting the location from our list a first position.
                    val address: Address = addressList!!.get(0)

                    // on below line we are creating a variable for our location where we will add our locations latitude and longitude.
                    val latLng = LatLng(address.getLatitude(), address.getLongitude())

                    // on below line we are adding marker to that position.
                    mMap.addMarker(MarkerOptions().position(latLng).title(location))

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        mapFragment.getMapAsync(this)

        val navController = Navigation.findNavController(this, R.id.container)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)

        val bottomSheetParent = findViewById<ConstraintLayout>(R.id.bottom_sheet_parent)
        val mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetParent)
        mBottomSheetBehaviour.apply {
            peekHeight=100
            this.state = BottomSheetBehavior.STATE_SETTLING
            isFitToContents = false
            halfExpandedRatio = 0.45f
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        recyclerAdapter = RecyclerAdapter()
        binding.recyclerView.adapter = recyclerAdapter
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or mo ve the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //adding markers for location under "Event"
        val boundsBuilder= LatLngBounds.builder()
        for(s in listEvent){
            val latlng=LatLng(s.latitude, s.longtitude)
            boundsBuilder.include(latlng)
            val markerE = mMap.addMarker(MarkerOptions()
                        .position(latlng)
                        .title(s.title)
                        .snippet(s.description)
                        .icon(bitmapDescriptorFromVector(this, R.drawable.ic_map_pin_filled_orange_48dp,R.drawable.baseline_event_20)))
            markersAll.add(markerE!!)

        }
        for(s in listService){
            val latlng=LatLng(s.latitude, s.longtitude)
            boundsBuilder.include(latlng)
            val markerS = mMap.addMarker(MarkerOptions()
                        .position(latlng)
                        .title(s.title)
                        .snippet(s.description)
                        .icon(bitmapDescriptorFromVector(this, R.drawable.ic_map_pin_filled_blue_48dp,R.drawable.baseline_miscellaneous_services_20)))
            markersAll.add(markerS!!)
        }
        for(s in listTravel){
            val latlng=LatLng(s.latitude, s.longtitude)
            boundsBuilder.include(latlng)
            val markerT = mMap.addMarker(MarkerOptions()
                        .position(latlng)
                        .title(s.title)
                        .snippet(s.description)
                        .icon(bitmapDescriptorFromVector(this, R.drawable.ic_map_pin_filled_green_48dp,R.drawable.baseline_accessible_20)))
            markersAll.add(markerT!!)
        }

        // Move the camera to an area covering most of the markers
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 10))
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        TODO("Not yet implemented")
    }
    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorBackgroundId:Int, @DrawableRes vectorDrawableResourceId: Int ): BitmapDescriptor? {
        val background = ContextCompat.getDrawable(context, vectorBackgroundId)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(
            37,
            20,
            vectorDrawable.intrinsicWidth + 37,
            vectorDrawable.intrinsicHeight + 20
        )
        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object{
        const val EVENT = "com.example.share4care.EVENT"
        const val SERVICE = "com.example.share4care.SERVICE"
        const val TRAVEL = "com.example.share4care.TRAVEL"
    }




}