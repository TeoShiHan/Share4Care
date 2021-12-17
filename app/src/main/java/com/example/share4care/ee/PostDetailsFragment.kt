package com.example.share4care.ee

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.share4care.R
import com.example.share4care.contentData.PostDetails
import com.example.share4care.databinding.FragmentPostDetailsBinding
import com.example.share4care.shihan.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.HashMap

class PostDetailsFragment : Fragment() {

    private lateinit var rootLayout : View
    val database = Firebase.database
    private lateinit var postDetailsRef : DatabaseReference
    private var isLiked : Boolean = false
    private var isDisliked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        rootLayout = binding.root

        val estKey =  when(requireArguments().getString("estType")){
            "Events" -> requireArguments().getString("title")+requireArguments().getString("host")+requireArguments().getString("date")
            "Services" -> requireArguments().getString("title")+requireArguments().getString("host")+requireArguments().getString("category")
            "Travels" -> requireArguments().getString("title")+requireArguments().getString("host")+requireArguments().getString("category")
            else -> requireArguments().getString("title")+requireArguments().getString("host")+requireArguments().getString("date")
        }

        postDetailsRef = database.getReference("${requireArguments().getString("estType")}").child(estKey)

        val username = requireArguments().getString("username")

        binding.apply{

            val postDetailsListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        tvAddress.text = dataSnapshot.child("address").value.toString()
                        tvContacts.text = dataSnapshot.child("contactNumber").value.toString()
                        tvDateTime.text = dataSnapshot.child("date").value.toString()
                        tvDesc.text = dataSnapshot.child("description").value.toString()
                        tvHost.text = dataSnapshot.child("host").value.toString()
                        tvLike.text = if (dataSnapshot.child("likes").value!=null) (dataSnapshot.child("likes").value as HashMap<*,*>).keys.size.toString() else "0"

                        if(dataSnapshot.child("likes").value!=null){
                            tvLike.text = (dataSnapshot.child("likes").value as HashMap<*,*>).keys.size.toString()
                            if(dataSnapshot.child("likes").hasChild(username.toString())){
                                isLiked = true
                            }
                        }else{
                            tvLike.text = "0"
                        }

                        if(dataSnapshot.child("dislikes").value!=null){
                            tvDislike.text = (dataSnapshot.child("dislikes").value as HashMap<*,*>).keys.size.toString()
                            if(dataSnapshot.child("likes").hasChild(username.toString())){
                                isDisliked = true
                            }
                        }else{
                            tvDislike.text = "0"
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("onCancel", "loadEST:onCancelled", databaseError.toException())
                }
            }
            postDetailsRef.addValueEventListener(postDetailsListener)

            iconLike.setOnClickListener(){
                onClickIconEffect(iconLike)
                if(!isLiked){
                    postDetailsRef.child("likes").child(username!!).setValue("username:${username}")
                    isLiked = true
                }else{
                    postDetailsRef.child("likes").child(username!!).removeValue()
                    isLiked = false
                }
            }
            iconDislike.setOnClickListener(){
                onClickIconEffect(iconDislike)
                if(!isDisliked){
                    postDetailsRef.child("dislikes").child(username!!).setValue("username:${username}")
                    isDisliked = true
                }else{
                    postDetailsRef.child("dislikes").child(username!!).removeValue()
                    isDisliked = false
                }
            }
            iconSave.setOnClickListener(){
                onClickIconEffect(iconSave)
            }
            iconReport.setOnClickListener(){
                iconReport.setColorFilter(R.color.colorReport)
                val reportReason = arrayOf(
                    "Sexual content",
                    "Duplicated",
                    "Hateful or abusive content",
                    "Fake content",
                    "Content not accurate or misleading",
                    "Irrelevant content"
                )

                var selectedReason = reportReason[selectedReportReasonIndex]

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Report content")
                    .setSingleChoiceItems(reportReason, selectedReportReasonIndex){ dialog, which ->
                        selectedReportReasonIndex = which
                        selectedReason = reportReason[which]
                    }
                    .setPositiveButton("OK"){ dialog, which ->
                        showSnackbar("Content has been reported")
                        iconReport.clearColorFilter()
                    }
                    .setNegativeButton("Cancel"){ dialog, which ->
                        iconReport.clearColorFilter()
                    }
                    .show()
            }
            iconShare.setOnClickListener(){
                iconShare.setColorFilter(R.color.colorShare)
                val shareIntent:Intent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, requireArguments().getString("title"))
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Host: ${requireArguments().getString("host")} \nDate: ${requireArguments().getString("date")} \nAddress: ${requireArguments().getString("address")} \nContact Number: ${requireArguments().getString("contactNumber")}\n")
                shareIntent.type = "text/plain"
                startActivity(shareIntent)
                onClickIconEffect(iconShare)
            }

            tvExternalLink.setOnClickListener(){
                val url : String = tvExternalLink.text .toString()
                var webpage : Uri = Uri.parse(url)

                if (!url.startsWith("http://") || !url.startsWith("https://")) {
                    webpage = Uri.parse("https://$url")
                }

                val intent = Intent(Intent.ACTION_VIEW, webpage)
//                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
//                }

            }

            tvContacts.setOnClickListener(){
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tvContacts.text}"))
                startActivity(intent)
            }
        }

        return binding.root
    }

    var selectedReportReasonIndex = 0

    private fun showSnackbar(msg:String){
        Snackbar.make(rootLayout,msg,Snackbar.LENGTH_LONG).show()
    }

    private fun onClickIconEffect(iv:ImageView){
        iv.setColorFilter(R.color.colorLike)
        clearIVColorFilter(iv)
    }

    private fun clearIVColorFilter(iv:ImageView){
        Handler(Looper.myLooper()!!).postDelayed({
            iv.clearColorFilter()
        },250)
    }

    fun loadPostDetails(){

    }
}