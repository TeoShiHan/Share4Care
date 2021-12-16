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
import com.example.share4care.contentData.Service
import com.example.share4care.contentData.Travel
import com.example.share4care.ee.HomeFragment


public class EventRecyclerViewAdapter(var eventList: ArrayList<Any>, var clickedItem: HomeFragment) : RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>(), Filterable {

    private var eventImmutableList : ArrayList<Any> = eventList.clone() as ArrayList<Any>

//    fun setData(eventList: ArrayList<Event>){
//        this.eventList = eventList
//        notifyDataSetChanged()
//    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc : TextView = itemView.findViewById(R.id.tvDesc)
        val tvLike : TextView = itemView.findViewById(R.id.tvLike)
        val tvDislike : TextView = itemView.findViewById(R.id.tvDislike)
        val ivPost : ImageView = itemView.findViewById(R.id.ivPost)
    }

    interface ClickedItem{
        fun clickedItem(any: Any)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = eventList[position]

        var title: String? = null
        var description: String? = null
        var image: String? = null
        var like: ArrayList<String> = arrayListOf()
        var dislike: ArrayList<String> = arrayListOf()

        when(currentItem) {
            is Event -> {
                title = currentItem.title
                description = currentItem.description
                image = currentItem.image
                like = currentItem.like as ArrayList<String>
                dislike = currentItem.dislike as ArrayList<String>
            }
            is Service -> {
                title = currentItem.title
                description = currentItem.description
                image = currentItem.image
                like = currentItem.like as ArrayList<String>
                dislike = currentItem.dislike as ArrayList<String>
            }
            is Travel -> {
                title = currentItem.title
                description = currentItem.description
                image = currentItem.image
                like = currentItem.like as ArrayList<String>
                dislike = currentItem.dislike as ArrayList<String>
            }
            else -> {
                title = ""
                description = ""
                image = ""
                like = arrayListOf()
                dislike = arrayListOf()
            }
        }

        holder.tvTitle.text = title
        holder.tvDesc.text = description
        Glide.with(holder.ivPost.context).load(image).into(holder.ivPost)
        holder.tvLike.text = like?.size.toString()
        holder.tvDislike.text = dislike?.size.toString()

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
                    var newESTList = ArrayList<Any>()
                    for(est in eventImmutableList){
                        var isContains = false
                        when(est){
                            is Event -> {
                                isContains = (
                                    est.title.lowercase().contains(searchChar) ||
                                    est.description.lowercase().contains(searchChar) ||
                                    est.host.lowercase().contains(searchChar) ||
                                    est.category.lowercase().contains(searchChar)
                                )
                            }
                            is Service -> {
                                isContains = (
                                    est.title.lowercase().contains(searchChar) ||
                                    est.description.lowercase().contains(searchChar) ||
                                    est.host.lowercase().contains(searchChar) ||
                                    est.category.lowercase().contains(searchChar)
                                    )
                            }
                            is Travel -> {
                                isContains = (
                                    est.title.lowercase().contains(searchChar) ||
                                    est.description.lowercase().contains(searchChar) ||
                                    est.host.lowercase().contains(searchChar) ||
                                    est.category.lowercase().contains(searchChar)
                                    )
                            }
                        }

                        if(isContains){
                            newESTList.add(est)
                        }
                    }
                    filterResults.count = newESTList.size
                    filterResults.values = newESTList
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                eventList = p1!!.values as ArrayList<Any>
                notifyDataSetChanged()
            }

        }
    }

    fun pushToTop(index:Int){
        val eventItem = eventImmutableList.get(index)
        val newEventList = eventImmutableList.clone() as ArrayList<Any>
        newEventList.removeAt(index)
        newEventList.add(0,eventItem)
        eventList = newEventList
        notifyDataSetChanged()
    }
}