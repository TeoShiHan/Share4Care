package com.template.androidtemplate.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.share4care.R
import com.example.share4care.contentData.Event


public class EventRecyclerViewAdapter(var eventList: ArrayList<Event>, var clickedItem: ClickedItem) : RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>(), Filterable {

    private var eventImmutableList : ArrayList<Event> = eventList.clone() as ArrayList<Event>

    fun setData(eventList: ArrayList<Event>){
        this.eventList = eventList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc : TextView = itemView.findViewById(R.id.tvDesc)
        val tvLike : TextView = itemView.findViewById(R.id.tvLike)
        val tvDislike : TextView = itemView.findViewById(R.id.tvDislike)
        val ivPost : ImageView = itemView.findViewById(R.id.ivPost)
    }

    interface ClickedItem{
        fun clickedItem(event:Event)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = eventList[position]

        holder.tvTitle.text = currentItem.title
        holder.tvDesc.text = currentItem.description
        Glide.with(holder.ivPost.context).load(currentItem.image).into(holder.ivPost)
        holder.tvLike.text = currentItem.like?.size.toString()
        holder.tvDislike.text = currentItem.dislike?.size.toString()

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
                        Log.d("searchChar",searchChar)
                        Log.d("event title",event.title)
                        if(
                            event.title.lowercase().contains(searchChar) ||
                            event.description.lowercase().contains(searchChar) ||
                            event.host.lowercase().contains(searchChar) ||
                            event.category.lowercase().contains(searchChar)
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

//    fun updateList(list:MutableList<Event>){
//        eventList = list
//        notifyDataSetChanged()
//    }
}