package com.example.share4care.ee

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.share4care.*
import com.example.share4care.R
import com.example.share4care.contentData.*
import com.example.share4care.databinding.FragmentHomeBinding
import com.example.share4care.shihan.roomData.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.template.androidtemplate.ui.main.EventRecyclerViewAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, EventRecyclerViewAdapter.ClickedItem {

    private lateinit var mMap: GoogleMap
    private lateinit var addedEvent:Event
    private lateinit var binding: FragmentHomeBinding
    private lateinit var activityType: String
    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var locationRequest:LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var mapFragment: SupportMapFragment
//    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var eventRecyclerViewAdapter: EventRecyclerViewAdapter
    private lateinit var mBottomSheetBehaviour: BottomSheetBehavior<ConstraintLayout>
    private lateinit var recyclerViewLayoutManager: LinearLayoutManager

    // for saving the data to local database
    private lateinit var traveldb : TravelViewModel
    private lateinit var eventdb : EventViewModel
    private lateinit var servicedb : ServiceViewModel
    val roomCaster = TypeCaster()

    val database = Firebase.database
    val myEventRef = database.getReference("Events")
    val myServiceRef= database.getReference("Services")
    val myTravelRef = database.getReference("Travels")
    val myStorageRef = FirebaseStorage.getInstance().getReference("images")

    var markersAll = mutableListOf<Marker>()
    var mapMarkerEvent: HashMap<String,String> = HashMap<String,String>()
    var mapMarkerService: HashMap<String,String> = HashMap<String,String>()
    var mapMarkerTravel: HashMap<String,String> = HashMap<String,String>()

    var listEvent: MutableList<Event> = mutableListOf<Event>()
    var listService = mutableListOf<Service>()
    var listTravel = mutableListOf<Travel>()
    var listEST = mutableListOf<Any>()


    var eventData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            addedEvent=data?.getSerializableExtra(EVENT) as Event
            listEvent.add(addedEvent)
            val latlng=LatLng(addedEvent.latitude, addedEvent.longtitude)
            val newMarker = mMap.addMarker(MarkerOptions()
                .position(latlng)
                .title(addedEvent.title)
                .snippet(addedEvent.description)
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_pin_filled_orange_48dp,R.drawable.baseline_event_20)))
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
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_pin_filled_blue_48dp,R.drawable.baseline_miscellaneous_services_20)))
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
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_pin_filled_green_48dp,R.drawable.baseline_accessible_20)))
            markersAll.add(newMarker!!)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        // Initialize the room db
        // initialize room db
        eventdb = ViewModelProvider(this)[EventViewModel::class.java]
        servicedb = ViewModelProvider(this)[ServiceViewModel::class.java]
        traveldb = ViewModelProvider(this)[TravelViewModel::class.java]

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val status = arguments?.getString("status")

        if (status!! =="0"){
            binding.addButton.visibility = View.GONE
        }

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        val bottomSheetParent = binding.bottomSheetParent
        mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetParent)
        mBottomSheetBehaviour.apply {
            peekHeight=90
            this.state = BottomSheetBehavior.STATE_SETTLING
            isFitToContents = false
            halfExpandedRatio = 0.45f
            expandedOffset = 200
        }

        recyclerViewLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = recyclerViewLayoutManager
        binding.recyclerView.setHasFixedSize(true)

        if(isConnectedToWifi()){
            loadEvent(object:EventCallback{
                override fun onEventBack(s: MutableList<Event>) {
                    listEvent = s
                    listEST.addAll(listEvent)
                }
            })

            loadService(object:ServiceCallback{
                override fun onServiceBack(s: MutableList<Service>) {
                    listService = s
                    listEST.addAll(listService)
                }
            })

            loadTravel(object:TravelCallback{
                override fun onTravelBack(s: MutableList<Travel>) {
                    listTravel = s
                    listEST.addAll(listTravel)
                    eventRecyclerViewAdapter = EventRecyclerViewAdapter(ArrayList(listEST),this@HomeFragment)
                    binding.recyclerView.adapter = eventRecyclerViewAdapter
                }
            })
        }
        else{

            eventdb.readAllData.observe(viewLifecycleOwner, {
                    returnedValue ->
                val convertedData = roomCaster.convertEventDataListToRuntimeList(returnedValue)
                if (convertedData != null) {
                    listEvent = convertedData.toMutableList()
                    listEST.addAll(convertedData)
                }
            })

            servicedb.readAllData.observe(viewLifecycleOwner,{
                    returnedValue ->
                val convertedData = roomCaster.convertServiceDataListToRuntimeList(returnedValue)
                if (convertedData != null) {
                    listService = convertedData.toMutableList()
                    listEST.addAll(convertedData)
                }
            })

            traveldb.readAllData.observe(viewLifecycleOwner, {
                    returnValue->
                val convertedData = roomCaster.convertTravelDataListToRuntimeList(returnValue)
                if (convertedData != null) {
                    listTravel = convertedData.toMutableList()
                    listEST.addAll(convertedData)
                }
            })

        }


        binding.addButton.setOnClickListener(){
            val typeFormView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_choose_type, null)
            val dialog= AlertDialog.Builder(requireContext())
                .setTitle("Select A Type")
                .setView(typeFormView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Continue", null)
                .show()

            val typeSpinner=typeFormView.findViewById<Spinner>(R.id.typeSpinner)
            ArrayAdapter.createFromResource(requireContext(), R.array.type_array, android.R.layout.simple_spinner_item)
                .also{adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    typeSpinner.adapter=adapter
                }

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(){
                activityType=typeSpinner.selectedItem.toString()
                when(activityType){
                    "Event" -> {
                        val intent = Intent(requireContext(),RegisterEventActivity::class.java)
                        eventData.launch(intent)
                        dialog.dismiss()
                    }
                    "Service" -> {
                        val intent = Intent(requireContext(), RegisterServiceActivity::class.java)
                        serviceData.launch(intent)
                        dialog.dismiss()
                    }
                    "Travel" -> {
                        val intent = Intent(requireContext(), RegisterTravelActivity::class.java)
                        travelData.launch(intent)
                        dialog.dismiss()
                    }
                }
            }
        }

        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svLocation.clearFocus()
                Log.d("seachTextSubmit",query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mBottomSheetBehaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                eventRecyclerViewAdapter!!.filter.filter(newText)
                return false
            }
        })

//        binding.recyclerView.layoutManager = LinearLayoutManager(
//            requireContext(),
//            LinearLayoutManager.VERTICAL, false
//        )
//        recyclerAdapter = RecyclerAdapter()
//        binding.recyclerView.adapter = recyclerAdapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //adding markers for location under "Event"
        //val boundsBuilder= LatLngBounds.builder()
        listEvent.forEachIndexed{ index,s ->
            val latlng=LatLng(s.latitude, s.longtitude)
            //boundsBuilder.include(latlng)
            val markerE = mMap.addMarker(MarkerOptions()
                .position(latlng)
                .title(s.title)
                .snippet(s.description)
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_pin_filled_orange_48dp,R.drawable.baseline_event_20))
            )
            Log.d("log ggMarker",markerE.toString())
            mapMarkerEvent.put(markerE!!.id,"EVENT"+index)
            markersAll.add(markerE!!)
//            mapMarkerObj.put(markerE,s)
        }
        listService.forEachIndexed{ index,s ->
            val latlng=LatLng(s.latitude, s.longtitude)
            //boundsBuilder.include(latlng)
            val markerS = mMap.addMarker(MarkerOptions()
                .position(latlng)
                .title(s.title)
                .snippet(s.description)
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_pin_filled_blue_48dp,R.drawable.baseline_miscellaneous_services_20)))
            mapMarkerService.put(markerS!!.id,"SERVICE"+index)
            markersAll.add(markerS!!)
//            mapMarkerObj.put(markerS,s)
        }
        listTravel.forEachIndexed{ index,s ->
            val latlng=LatLng(s.latitude, s.longtitude)
            //boundsBuilder.include(latlng)
            val markerT = mMap.addMarker(MarkerOptions()
                .position(latlng)
                .title(s.title)
                .snippet(s.description)
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_pin_filled_green_48dp,R.drawable.baseline_accessible_20)))
            markersAll.add(markerT!!)
            mapMarkerTravel.put(markerT!!.id,"TRAVEL"+index)
//            mapMarkerObj.put(markerT,s)
        }

        // Move the camera to an area covering most of the markers
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 10))
        mMap.setOnMarkerClickListener(this)

        mMap.setOnMapClickListener {
            mBottomSheetBehaviour.state=BottomSheetBehavior.STATE_COLLAPSED
        }

        getLocationAccess()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        mBottomSheetBehaviour.state=BottomSheetBehavior.STATE_HALF_EXPANDED
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))

        Log.e("log marker title", marker.title.toString())
        listEST.forEachIndexed { index, est ->
            val title = when(est){
                is Event -> est.title
                is Service -> est.title
                is Travel -> est.title
                else -> "Null"
            }
            if(marker.title == title){
                Log.d("log marker position",index.toString())
                eventRecyclerViewAdapter.pushToTop(index)
                binding.recyclerView.scrollToPosition(0)
            }
        }
//        val value = mapMarkerEvent.get(marker.id)
//        Log.d("log value", value!!)
//        val type = value!!.filter { it.isLetter() }
//        val index = value!!.filter { it.isDigit() }
//        binding.recyclerView.scrollToPosition(1)
//        Log.d("log type",type)
//        Log.d("log index",index)
//
//        when(type){
//            "EVENT" -> binding.recyclerView.scrollToPosition(index.toInt())
//            "SERVICE" -> binding.recyclerView.scrollToPosition(in)
//            "TRAVEL" -> binding.recyclerView.scrollToPosition(index)
//        }

        return true
    }

    private fun getLocationAccess(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            getLocationUpdates()
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1){
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                getLocationAccess()
            } else {
                Toast.makeText(requireContext(),
                    "User did not grant location access permission",
                    Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
    }

    private fun startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun getLocationUpdates(){
        locationRequest = LocationRequest.create().apply {
            interval = 30000
            fastestInterval = 20000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 1.0F
        }
        locationCallback = object:LocationCallback(){
            override fun onLocationResult(locationresult: LocationResult) {
                val location = locationresult.lastLocation
                val latLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(latLng))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        }
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

    private fun loadEvent(callback: EventCallback){
        val ref = myEventRef
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val list = mutableListOf<Event>()
                if (p0.exists()) {
                    for (c in p0.children) {
                        val title =  c.child("title").value.toString()
                        val host =  c.child("host").value.toString()
                        val category =  c.child("category").value.toString()
                        val description = c.child("description").value.toString()
                        val date = c.child("date").value.toString()
                        val address = c.child("address").value.toString()
                        val latitude = c.child("latitude").value as Double
                        val longtitude = c.child("longtitude").value as Double
                        val contactNumber = c.child("contactNumber").value.toString()
                        val contactEmail = c.child("contactEmail").value.toString()
                        val image = c.child("image").value.toString()
                        val status = (c.child("status").value as Long).toInt()
                        val like = if(c.child("like").exists()) c.child("like").value as MutableList<String> else mutableListOf()
                        val dislike = if(c.child("dislike").exists()) c.child("dislike").value as MutableList<String> else mutableListOf()
                        val save = if(c.child("save").exists()) c.child("save").value as MutableList<String> else mutableListOf()
                        val comment = if(c.child("comment").exists()) c.child("comment").value as MutableList<UserComment> else mutableListOf()

                        val tempEvent = Event(title, host, category, description, date, address, latitude , longtitude, contactNumber, contactEmail, image, status, like, dislike, save, comment)

                        list.add(tempEvent)

                        val tempRoomEvent = roomCaster.getEventData(tempEvent)

                        eventdb.addEvent(tempRoomEvent)
                    }

//                    Log.d("log event list", list.toString())
                    callback.onEventBack(list)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }

    private fun loadService(callback: ServiceCallback){
        val ref = myServiceRef
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val list = mutableListOf<Service>()
                if (p0.exists()) {
                    for (c in p0.children) {
                        val title =  c.child("title").value.toString()
                        val host =  c.child("host").value.toString()
                        val category =  c.child("category").value.toString()
                        val description = c.child("description").value.toString()
                        val address = c.child("address").value.toString()
                        val latitude = c.child("latitude").value as Double
                        val longtitude = c.child("longtitude").value as Double
                        val contactNumber = c.child("contactNumber").value.toString()
                        val contactEmail = c.child("contactEmail").value.toString()
                        val image = c.child("image").value.toString()
                        val status = (c.child("status").value as Long).toInt()
                        val like = if(c.child("like").exists()) c.child("like").value as MutableList<String> else mutableListOf()
                        val dislike = if(c.child("dislike").exists()) c.child("dislike").value as MutableList<String> else mutableListOf()
                        val save = if(c.child("save").exists()) c.child("save").value as MutableList<String> else mutableListOf()
                        val comment = if(c.child("comment").exists()) c.child("comment").value as MutableList<UserComment> else mutableListOf()

                        val tempService = Service(title, host, category, description, address, latitude , longtitude, contactNumber, contactEmail, image, status, like, dislike, save, comment)

                        list.add(tempService)

                        val tempRoomService = roomCaster.getServiceData(tempService)
                        servicedb.addService(tempRoomService)
                    }
                    callback.onServiceBack(list)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }

    private fun loadTravel(callback: TravelCallback){
        val ref = myTravelRef
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val list = mutableListOf<Travel>()
                if (p0.exists()) {
                    for (c in p0.children) {
                        val title =  c.child("title").value.toString()
                        val host =  c.child("host").value.toString()
                        val category =  c.child("category").value.toString()
                        val description = c.child("description").value.toString()
                        val address = c.child("address").value.toString()
                        val latitude = c.child("latitude").value as Double
                        val longtitude = c.child("longtitude").value as Double
                        val contactNumber = c.child("contactNumber").value.toString()
                        val contactEmail = c.child("contactEmail").value.toString()
                        val image = c.child("image").value.toString()
                        val status = (c.child("status").value as Long).toInt()
                        val like = if(c.child("like").exists()) c.child("like").value as MutableList<String> else mutableListOf()
                        val dislike = if(c.child("dislike").exists()) c.child("dislike").value as MutableList<String> else mutableListOf()
                        val save = if(c.child("save").exists()) c.child("save").value as MutableList<String> else mutableListOf()
                        val comment = if(c.child("comment").exists()) c.child("comment").value as MutableList<UserComment> else mutableListOf()

                        val tempTravel = Travel(title, host, category, description, address, latitude , longtitude, contactNumber, contactEmail, image, status, like, dislike, save, comment)

                        list.add(tempTravel)

                        val tempRoomTravel = roomCaster.getTravelData(tempTravel)

                        traveldb.addTravel(tempRoomTravel)
                    }
                    callback.onTravelBack(list)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }

    interface EventCallback{
        fun onEventBack(s:MutableList<Event>)
    }

    interface ServiceCallback{
        fun onServiceBack(s:MutableList<Service>)
    }

    interface TravelCallback{
        fun onTravelBack(s:MutableList<Travel>)
    }

    override fun clickedItem(any: Any) {
        var est = any
        val intent = Intent(context, ESTActivity::class.java)
        if(est is Event){
            Log.e("log clickItemEvent",est.title)
            intent.putExtra("eventObj",est as Event)
        }else if(est is Service){
            Log.e("log clickItemService",est.title)
            intent.putExtra("eventObj",est as Service)
        }else if(est is Travel){
            Log.e("log clickItemTravel",est.title)
            intent.putExtra("eventObj",est as Travel)
        }
        startActivity(intent)
    }

    companion object{
        const val EVENT = "com.example.share4care.EVENT"
        const val SERVICE = "com.example.share4care.SERVICE"
        const val TRAVEL = "com.example.share4care.TRAVEL"
    }


    fun isConnectedToWifi(): Boolean {
        val context = activity?.applicationContext
        val connectivity: ConnectivityManager? = null
        var info: NetworkInfo? = null

        val networkManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
        val isConnectedWifi = connectivity?.activeNetworkInfo

        if (isConnectedWifi != null && isConnectedWifi.state == NetworkInfo.State.CONNECTED){
            return true
        }else{
            Toast.makeText(context, "wifi not turned on, showing local data", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}