package com.template.androidtemplate.ui.main

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.share4care.R
import com.example.share4care.contentData.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter


public class CalendarEventRecyclerViewAdapter(var eventList: ArrayList<Event>, var clickedItem: ClickedItem) : RecyclerView.Adapter<CalendarEventRecyclerViewAdapter.ViewHolder>(), Filterable {

    private var eventImmutableList : ArrayList<Event> = eventList.clone() as ArrayList<Event>

    fun setData(eventList: ArrayList<Event>){
        this.eventList = eventList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvDateAndMonth : TextView = itemView.findViewById(R.id.tvDateAndMonth)
        val tvDateYear : TextView = itemView.findViewById(R.id.tvDateYear)
    }

    interface ClickedItem{
        fun clickedItem(event:Event)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.calendar_post_item, viewGroup, false)

        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = eventList[position]

        //        val sdf = SimpleDateFormat("dd-mm-yyyy")
        val dateInputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val parsedDate = LocalDate.parse(currentItem.date,dateInputFormatter)

        holder.tvTitle.text = currentItem.title
        holder.tvDateYear.text = parsedDate.year.toString()
        holder.tvDateAndMonth.text = "${parsedDate.dayOfMonth}\n${parsedDate.month}"

        holder.itemView.setOnClickListener(){
            clickedItem.clickedItem(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var filterResults = FilterResults()
                if(p0 == null || p0.isEmpty()){
                    filterResults.count = eventImmutableList.size
                    filterResults.values = eventImmutableList
                }else{
                    var searchChar : String = p0.toString().lowercase()
                    var newEventList = ArrayList<Event>()
                    for(event in eventImmutableList){
                        if(
                            event.title.lowercase().contains(searchChar)
                        ){
                            newEventList.add(event)
                        }
                    }
                    filterResults.count = newEventList.size
                    filterResults.values = newEventList
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                eventList = p1!!.values as ArrayList<Event>
                notifyDataSetChanged()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterDate(dateFrom: LocalDate = LocalDate.now(), dateTo: LocalDate? = null){
        var newEventList = ArrayList<Event>()
        for(event in eventImmutableList){
            val dateInputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val eventLocalDate = LocalDate.parse(event.date,dateInputFormatter)
            if(dateTo==null) {
                if(eventLocalDate.isAfter(dateFrom) || eventLocalDate.isEqual(dateFrom)){
                    newEventList.add(event)
                }
            }else{
                if ((eventLocalDate.isAfter(dateFrom) || eventLocalDate.isEqual(dateFrom))
                    && (eventLocalDate.isBefore(dateTo) || eventLocalDate.isEqual(dateTo))
                ){
                    newEventList.add(event)
                }
            }
        }
        eventList = newEventList as ArrayList<Event>
        notifyDataSetChanged()
    }
}