package com.template.androidtemplate.ui.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.share4care.R
import com.example.share4care.contentData.Event

public class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var context: Context? = null

    private val title = arrayOf("d116df5d116df5",
        "36ffc7536ffc75", "f5cfe78f5cfe78f", "5b876285b876",
        "db8d14edb8d14e", "9913dc49913dc4", "e120f96e120f96",
        "466251b466251b")

    private val desc = arrayOf("d116df d116df5d116df5d1 16df5d116df5d116 df5d116df5d116df5 setEllipsize  setEllipsize setEllipsize setEllipsize  ",
        "36ffc7536ff c7536ffc7536ffc 7536ffc7536ffc 7536ffc75 36ffc75 f5cfe78f5cfe78f5cfe78f5cfe78", "f5cfe78f5cfe78f5cfe78f 5cfe78f5c fe78f5cfe7 8f5cfe78f5cfe78", "5b876285 b876285b8762 8b876285b8 628b876285b87628",
        "db8d14edb8d14edb8d14ed b8d14edb8d14edb8d14 edb8d14edb8d14edb8 d14edb8d14edb8d14 edb8d14e", "9913dc49913dc49913dc49913dc499 13dc49913d c49913dc49913dc499 13dc49913dc4991 3dc49913dc4", "e120f96e120f9 6e120f96e120f96 e120f96e120f96e 120f96",
        "466251b4 66251b466251b466 251b466251b46 6251b466251b466 251b251b251b")

    private val like = arrayOf("1","20","999","9999","99999","9999999","9999999","99999999")

    private val dislike = arrayOf("1","20","999","9999","99999","9999999","9999999","99999999")

//    private val listEvent = arrayListOf<Event>(
//        Event("a","a","a","a","a","a",10.1,10.1,"123123","asd@gmail.com","https:gogole.com",1),
//        Event("a","a","a","a","a","a",10.1,10.1,"123123","asd@gmail.com","https:gogole.com",1),
//        Event("a","a","a","a","a","a",10.1,10.1,"123123","asd@gmail.com","https:gogole.com",1),
//        Event("a","a","a","a","a","a",10.1,10.1,"123123","asd@gmail.com","https:gogole.com",1)
//    )


    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tvTitle : TextView
        lateinit var tvDesc : TextView
        lateinit var tvLike : TextView
        lateinit var tvDislike : TextView
        lateinit var ivPost : ImageView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDesc = itemView.findViewById(R.id.tvDesc)
            tvLike = itemView.findViewById(R.id.tvLike)
            tvDislike = itemView.findViewById(R.id.tvDislike)
            ivPost = itemView.findViewById(R.id.ivPost)

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

//                (itemView.context as AppCompatActivity)
//                    .supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.homeFragment2,PostDetailFragment())
//                    .commit()

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
        viewHolder.tvTitle.text = title[i]
        viewHolder.tvDesc.text = desc[i]
        viewHolder.tvLike.text = like[i]
        viewHolder.tvDislike.text = dislike[i]
    }

    override fun getItemCount(): Int {
        return title.size
    }
}