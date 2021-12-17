package com.example.share4care.ee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.share4care.R
import com.example.share4care.chooili.model.User
import com.example.share4care.databinding.FragmentProfileBinding
import com.example.share4care.shihan.UserTableRecord
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    val database = Firebase.database
    val myUserRef = database.getReference("User")
    private lateinit var img:ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val username = arguments?.getString("username")

        img = binding.imgUser
        loadUser(object: UserCallback{
            override fun onUserBack(s: UserTableRecord) {
                binding.tvName.text  = s.userName
                binding.tvBirthday.text = s.dob
                binding.tvAddress.text = s.address
                binding.tvPhone.text = s.phone
                binding.tvEmail.text = s.email
                if (s.status == "0"){
                    binding.tvStatus.text = "Unverified User"
                } else {
                    binding.tvStatus.text = "Verified User"
                }

                if(s.imageLink != ""){
                    Glide.with(img.context).load(s.imageLink).into(img)
                }
            }
        }, username!!)

        return binding.root
    }

    private fun loadUser(callback: UserCallback, key:String){
        val ref = myUserRef
        var user = UserTableRecord("", "","","","","","","","","","","","", "")
        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (c in p0.children) {
                        if (c.child("userName").value.toString() == key){
                            val status = c.child("status").value.toString()
                            val userName =  c.child("userName").value.toString()
                            val password =  c.child("password").value.toString()
                            val name =  c.child("name").value.toString()
                            val gender = c.child("gender").value.toString()
                            val dob = c.child("dob").value.toString()
                            val phone = c.child("phone").value.toString()
                            val email = c.child("email").value.toString()
                            val address = c.child("address").value.toString()
                            val isOKU = c.child("isOKU").value.toString()
                            val occupation = c.child("occupation").value.toString()
                            val accountType = c.child("accountType").value.toString()
                            val companyName = c.child("companyName").value.toString()
                            val imageLink = c.child("imageLink").value.toString()

                            user = UserTableRecord(status, userName, password, name, gender, dob, phone, email, address, isOKU, occupation, companyName, accountType, imageLink)
                        }
                    }
                    callback.onUserBack(user)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        ref.addValueEventListener(refListener)
    }

    interface UserCallback{
        fun onUserBack(s: UserTableRecord)
    }

}