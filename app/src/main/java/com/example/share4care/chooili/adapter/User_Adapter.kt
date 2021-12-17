package com.example.share4care.chooili.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.share4care.R
import com.example.share4care.chooili.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class User_Adapter(private var userList: List<User>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<User_Adapter.UserViewHolder>() {

    val database = Firebase.database
    val userRef = database.getReference("User")

    inner class UserViewHolder(userView: View) : RecyclerView.ViewHolder(userView),
        View.OnClickListener {

        var userName: TextView = userView.findViewById(R.id.tvName)
        var userPhone: TextView = userView.findViewById(R.id.tvPhone)
        //var userDOB: TextView = userView.findViewById(R.id.tvBirthday)
        //var userEmail: TextView = userView.findViewById(R.id.tvUserEmail)
        //var userAddress: TextView = userView.findViewById(R.id.tvUserAddress)
        //var userIsOKU: TextView = userView.findViewById(R.id.tvOKU)
        //var btnUserVerify: TextView = userView.findViewById(R.id.btnVerify)
        //var btnUserDelete: TextView = userView.findViewById(R.id.btnDelete)

        init {
            userView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    //create view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var userView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_view, parent, false)

        return UserViewHolder(userView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUserRec = userList[position]
        holder.userName.text = currentUserRec.userName
        holder.userPhone.text = currentUserRec.userPhone
        //holder.userDOB.text = currentUserRec.userDOB
        //holder.userEmail.text = currentUserRec.userEmail
        //holder.userAddress.text = currentUserRec.userAddress
        //holder.userIsOKU.text = currentUserRec.userIsOKU

    }
    override fun getItemCount(): Int {
        return userList.size
    }
}
