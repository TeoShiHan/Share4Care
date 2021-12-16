package com.example.share4care.chooili.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.share4care.R
import com.example.share4care.chooili.model.EST
import com.example.share4care.contentData.Travel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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

        val key1 = currentESTRec.title+currentESTRec.host+currentESTRec.date
        val key2 = currentESTRec.title+currentESTRec.host+currentESTRec.category

        holder.btnVerify.setOnClickListener {
            updateEST(object:UpdateCallback{
                override fun onUpdateBack(s: Boolean) {
                    if (!s){
                        updateEST(object :UpdateCallback{
                            override fun onUpdateBack(s: Boolean) {
                                if (!s)
                                    updateEST(object :UpdateCallback{
                                        override fun onUpdateBack(s: Boolean) {
                                        }
                                    }, myTravelRef, key2)
                            }
                        }, myServiceRef, key2)
                    }
                }
            }, myEventRef, key1)
        }
        holder.btnDelete.setOnClickListener{
            deleteEST(object:DeleteCallback{
                override fun onDeleteBack(s: Boolean) {
                    if (!s){
                        deleteEST(object :DeleteCallback{
                            override fun onDeleteBack(s: Boolean) {
                                if (!s){
                                    deleteEST(object :DeleteCallback{
                                        override fun onDeleteBack(s: Boolean) {
                                        }
                                    }, myTravelRef, key2)
                                }
                            }
                        }, myServiceRef, key2)
                    }
                }
            }, myEventRef, key1)
        }
    }

    override fun getItemCount(): Int {
        return ESTList.size

    }

    private fun updateEST(callback: UpdateCallback, myRef: DatabaseReference, key1: String) {
        var updated:Boolean = false
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (c in p0.children) {
                        if (c.key.toString() == key1) {
                            val verify = HashMap<String, Any>()
                            verify.put("status", 1)
                            myRef.child(key1).updateChildren(verify)
                            updated = true
                        }
                    }
                    callback.onUpdateBack(updated)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        myRef.addValueEventListener(refListener)
    }

    private fun deleteEST(callback: DeleteCallback, myRef: DatabaseReference, key1: String) {
        var updated:Boolean = false
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (c in p0.children) {
                        if (c.key.toString() == key1) {
                            myRef.child(key1).removeValue()
                            updated = true
                        }
                    }
                    callback.onDeleteBack(updated)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        myRef.addValueEventListener(refListener)
    }


    interface UpdateCallback{
        fun onUpdateBack(s:Boolean)
    }

    interface DeleteCallback{
        fun onDeleteBack(s:Boolean)
    }


}