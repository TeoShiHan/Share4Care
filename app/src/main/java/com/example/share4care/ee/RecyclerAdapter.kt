package com.template.androidtemplate.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.share4care.MainActivity
import com.example.share4care.R
import com.example.share4care.ee.PostDetailFragment

public class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var context: Context? = null

    private val kode = arrayOf("d116df5",
        "36ffc75", "f5cfe78", "5b87628",
        "db8d14e", "9913dc4", "e120f96",
        "466251b")

    private val kategori = arrayOf("Kekayaan", "Teknologi",
        "Keluarga", "Bisnis",
        "Keluarga", "Hutang",
        "Teknologi", "Pidana")

    private val isi = arrayOf("pertanyaan 9",
        "pertanyaan 11", "pertanyaan 17", "test forum",
        "pertanyaan 12", "pertanyaan 18", "pertanyaan 20",
        "pertanyaan 21")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemKode: TextView
        var itemKategori: TextView
        var itemIsi: TextView

        init {
            itemKode = itemView.findViewById(R.id.kodePertanyaan)
            itemKategori = itemView.findViewById(R.id.kategori)
            itemIsi = itemView.findViewById(R.id.isiPertanyaan)

            itemView.setOnClickListener {
                var position: Int = bindingAdapterPosition
                Log.d("itemview click binding:", position.toString())
//                 Log.d("itemview click :", absoluteAdapterPosition.toString()) // have the same result with code above
                val context = itemView.context
//                val intent = Intent(context, PostDetailActivity::class.java)
//                intent.apply {
//                    putExtra("NUMBER", position)
//                    putExtra("CODE", itemKode.text)
//                    putExtra("CATEGORY", itemKategori.text)
//                    putExtra("CONTENT", itemIsi.text)
//                }
//                context.startActivity(intent)
//                it.findNavController().navigate(R.id.action_recyclerViewItemFragment_to_detailsFragment)

                (itemView.context as AppCompatActivity)
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.homeFragment2,PostDetailFragment())
                    .commit()
//                val fm = (itemView.context as FragmentActivity).supportFragmentManager.findFragmentByTag("asd")
//                val ft = (itemView.context as FragmentActivity).supportFragmentManager.beginTransaction()
//                ft.detach(fm!!)
//                ft.attach(fm)
//                ft.commit()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemKode.text = kode[i]
        viewHolder.itemKategori.text = kategori[i]
        viewHolder.itemIsi.text = isi[i]
    }

    override fun getItemCount(): Int {
        return kode.size
    }
}