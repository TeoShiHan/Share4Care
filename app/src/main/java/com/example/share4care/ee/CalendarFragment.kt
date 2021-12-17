package com.example.share4care.ee

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.share4care.*
import com.example.share4care.contentData.Event
import com.example.share4care.contentData.Service
import com.example.share4care.contentData.Travel
import com.example.share4care.contentData.UserComment
import com.example.share4care.databinding.FragmentCalendarBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import com.google.android.material.datepicker.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.template.androidtemplate.ui.main.CalendarEventRecyclerViewAdapter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList


class CalendarFragment : Fragment(), CalendarEventRecyclerViewAdapter.ClickedItem {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var calendarEventRecyclerViewAdapter: CalendarEventRecyclerViewAdapter
    private lateinit var recyclerViewLayoutManager: LinearLayoutManager

    val database = Firebase.database
    val myEventRef = database.getReference("Events")
    val myServiceRef= database.getReference("Services")
    val myTravelRef = database.getReference("Travels")
    val myStorageRef = FirebaseStorage.getInstance().getReference("images")

    private lateinit var listEvent: MutableList<Event>

    var listService = mutableListOf<Service>()
    var listTravel = mutableListOf<Travel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        recyclerViewLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = recyclerViewLayoutManager
        binding.recyclerView.setHasFixedSize(true)

        loadEvent(object:EventCallback{
            override fun onEventBack(s: MutableList<Event>) {
                listEvent = s
                calendarEventRecyclerViewAdapter = CalendarEventRecyclerViewAdapter(ArrayList(listEvent),this@CalendarFragment)
                binding.recyclerView.adapter = calendarEventRecyclerViewAdapter

            }
        })

        loadService(object:ServiceCallback{
            override fun onServiceBack(s: MutableList<Service>) {
                listService = s
            }
        })

        loadTravel(object:TravelCallback{
            override fun onTravelBack(s: MutableList<Travel>) {
                listTravel = s
            }
        })

        var fromDate: LocalDate = LocalDate.now()
        var toDate: LocalDate? = null
        binding.dateFromInputLayout.setEndIconOnClickListener{
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.clear()
            calendar.set(Calendar.DAY_OF_MONTH,Calendar.DATE)

            val calendarConstraints = CalendarConstraints.Builder()
            calendarConstraints.setValidator(DateValidatorPointForward.now())

            val listValidators : ArrayList<CalendarConstraints.DateValidator> = arrayListOf()
            var dateValidatorMin = DateValidatorPointForward.now()
            if(toDate!=null){
                var dateValidatorMax = DateValidatorPointBackward.before(toDate!!.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
                listValidators.add(dateValidatorMax)
            }
            listValidators.add(dateValidatorMin)
            val validators = CompositeDateValidator.allOf(listValidators)
            calendarConstraints.setValidator(validators)

            val dateFromPicker = MaterialDatePicker.Builder.datePicker()
            dateFromPicker.setCalendarConstraints(calendarConstraints.build())
            dateFromPicker.setSelection(fromDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
            val builder = dateFromPicker.setTitleText("From").build()

            builder.show(
                childFragmentManager,
                "SELECT_FROM_DATE"
            )

            builder.addOnPositiveButtonClickListener { date ->
                binding.dateFromEditText.setText(builder.headerText)
                val fromLocalDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                fromDate = fromLocalDate
                if(toDate!=null){
                    calendarEventRecyclerViewAdapter.filterDate(fromDate, toDate!!)
                }else{
                    calendarEventRecyclerViewAdapter.filterDate(fromDate)
                }
            }
        }

        binding.dateToInputLayout.setEndIconOnClickListener{
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.clear()
            calendar.set(Calendar.DAY_OF_MONTH,Calendar.DATE)

            val calendarConstraints = CalendarConstraints.Builder()
            calendarConstraints.setValidator(DateValidatorPointForward.from(fromDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()))

            val dateFromPicker = MaterialDatePicker.Builder.datePicker()
            dateFromPicker.setCalendarConstraints(calendarConstraints.build())
            if(toDate!=null){
                dateFromPicker.setSelection(toDate!!.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
            }
            val builder = dateFromPicker.setTitleText("To").build()

            builder.show(
                childFragmentManager,
                "SELECT_TO_DATE"
            )

            builder.addOnPositiveButtonClickListener { date ->
                binding.dateToEditText.setText(builder.headerText)
                val toLocalDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
                toDate = toLocalDate
                if(toDate!=null){
                    calendarEventRecyclerViewAdapter.filterDate(fromDate, toDate!!)
                }else{
                    calendarEventRecyclerViewAdapter.filterDate(fromDate)
                }
            }
        }

        return binding.root

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
                        val like = if(c.child("like").exists()) c.child("like").value as HashMap<*,*> else hashMapOf<String,String>()
                        val dislike = if(c.child("dislike").exists()) c.child("dislike").value as HashMap<*,*> else hashMapOf<String,String>()
                        val save = if(c.child("save").exists()) c.child("save").value as HashMap<*,*> else hashMapOf<String,String>()
                        val comment = if(c.child("comment").exists()) c.child("comment").value as MutableList<UserComment> else mutableListOf()

                        list.add(Event(title, host, category, description, date, address, latitude , longtitude, contactNumber, contactEmail, image, status, like, dislike, save, comment))
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
                        val like = if(c.child("like").exists()) c.child("like").value as HashMap<*,*> else hashMapOf<String,String>()
                        val dislike = if(c.child("dislike").exists()) c.child("dislike").value as HashMap<*,*> else hashMapOf<String,String>()
                        val save = if(c.child("save").exists()) c.child("save").value as HashMap<*,*> else hashMapOf<String,String>()
                        val comment = if(c.child("comment").exists()) c.child("comment").value as MutableList<UserComment> else mutableListOf()

                        list.add(Service(title, host, category, description, address, latitude , longtitude, contactNumber, contactEmail, image, status, like, dislike, save, comment))
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
                        val like = if(c.child("like").exists()) c.child("like").value as HashMap<*,*> else hashMapOf<String,String>()
                        val dislike = if(c.child("dislike").exists()) c.child("dislike").value as HashMap<*,*> else hashMapOf<String,String>()
                        val save = if(c.child("save").exists()) c.child("save").value as HashMap<*,*> else hashMapOf<String,String>()
                        val comment = if(c.child("comment").exists()) c.child("comment").value as MutableList<UserComment> else mutableListOf()

                        list.add(Travel(title, host, category, description, address, latitude , longtitude, contactNumber, contactEmail, image, status, like, dislike, save, comment))
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

    override fun clickedItem(event: Event) {
        var event1 = event
        val intent = Intent(context, ESTActivity::class.java)
        intent.putExtra("eventObj",event1)
        startActivity(intent)
        Log.e("eventtt","==>"+event1.title)
    }
}