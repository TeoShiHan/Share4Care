package com.example.share4care.ee

import android.content.Intent
import android.graphics.drawable.Drawable
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
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import com.example.share4care.R
import com.example.share4care.databinding.FragmentPostDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class PostDetailsFragment : Fragment() {

    private lateinit var rootLayout : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        rootLayout = binding.root

        val args = arguments
        Log.d("print args",args.toString())

        binding.apply{
            tvAddress.text = requireArguments().getString("address")
            tvContacts.text = requireArguments().getString("contactNumber")
            tvDateTime.text = requireArguments().getString("date")
            tvDesc.text = requireArguments().getString("description")
            tvDislike.text = requireArguments().getInt("dislike").toString()
            tvLike.text = requireArguments().getInt("like").toString()
            tvHost.text = requireArguments().getString("host")

            iconLike.setOnClickListener(){
                onClickIconEffect(iconLike)
            }
            iconDislike.setOnClickListener(){
                onClickIconEffect(iconDislike)
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
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, args!!.getString("title"))
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Host: ${args!!.getString("host")} \nDate: ${args!!.getString("date")} \nAddress: ${args!!.getString("address")} \nContact Number: ${args!!.getString("contactNumber")}\n")
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
}