package com.example.share4care.chooili.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.example.try_admin1.R
import com.android.example.try_admin1.models.EST
import com.android.example.try_admin1.models.User
import com.example.share4care.R
import com.example.share4care.chooili.model.EST
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EST_Adapter(private var ESTList: List<EST>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<EST_Adapter.ESTViewHolder>() {

    val database = Firebase.database
    val myEventRef = database.getReference("Events")
    val myServiceRef = database.getReference("Services")
    val myTravelRef = database.getReference("Travels")

    inner class ESTViewHolder(ESTView: View) : RecyclerView.ViewHolder(ESTView), View.OnClickListener {

        var btnDelete : Button = ESTView.findViewById(R.id.btnESTDelete)
        var btnVerify: Button = ESTView.findViewById(R.id.btnESTVerify)
        var title: TextView = ESTView.findViewById(R.id.tfTitle)
        var host: TextView = ESTView.findViewById(R.id.tfHost)
        var description: TextView = ESTView.findViewById(R.id.tfDesc)
        var date: TextView = ESTView.findViewById(R.id.tfDate)
        var contactNumber: TextView = ESTView.findViewById(R.id.tfContactNo)
        var contactEmail: TextView = ESTView.findViewById(R.id.tfEmail)
        var cat: TextView = ESTView.findViewById(R.id.tfCategory)
        var address: TextView = ESTView.findViewById(R.id.tfAddress)

        init {
            ESTView.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val position:Int = bindingAdapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }



    //create view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ESTViewHolder {
        var ESTView = LayoutInflater.from(parent.context)
            .inflate(R.layout.est_view, parent, false)

        return ESTViewHolder(ESTView)
    }

    override fun onBindViewHolder(holder: ESTViewHolder, position: Int) {
        val currentESTRec = ESTList[position]
        holder.title.text = currentESTRec.title
        holder.host.text = currentESTRec.host
        holder.description.text = currentESTRec.description
        holder.date.text = currentESTRec.date
        holder.contactNumber.text = currentESTRec.contactNumber
        holder.contactEmail.text = currentESTRec.contactEmail
        holder.cat.text = currentESTRec.category
        holder.address.text = currentESTRec.address
        holder.btnVerify.setOnClickListener {
            val verify = HashMap<String, Any>()
            print(currentESTRec.title)
            verify.put("status", 1)
            val key1 = currentESTRec.title+currentESTRec.host+currentESTRec.date
            val key2 = currentESTRec.title+currentESTRec.host+currentESTRec.category
            myEventRef.child(key1).updateChildren(verify).addOnFailureListener {
                myServiceRef.child(key2).updateChildren(verify)
                    .addOnFailureListener {
                        myTravelRef.child(key2).updateChildren(verify)
                    }
            }

        }
        holder.btnDelete.setOnClickListener{
            val key1 = currentESTRec.title+currentESTRec.host+currentESTRec.date
            val key2 = currentESTRec.title+currentESTRec.host+currentESTRec.category
            myEventRef.child(key1).removeValue().addOnFailureListener {
                myServiceRef.child(key2).removeValue()
                    .addOnFailureListener {
                        myTravelRef.child(key2).removeValue()
                    }
            }
        }}

    override fun getItemCount(): Int {
        return ESTList.size

    }
}