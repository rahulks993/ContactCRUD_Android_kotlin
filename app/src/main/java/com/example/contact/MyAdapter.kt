package com.example.contactcrud

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item.view.*

////Binding the data to the UI- use of adapter

class MyAdapter(val context: Context, var contacts: List<Model>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    //    var onShareClick = { _: String, _: String, _: String -> Unit }
    var onCardClick = { _: String, _: String, _: String, _: String, _: Int -> Unit }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(cntct: Model, pos: Int) {
            itemView.txvTitle1.text = cntct.name
            itemView.txvTitle2.text = cntct.number
            itemView.txvTitle3.text = cntct.email

            if (!cntct.imgPth.isNullOrEmpty()) {
                Glide.with(context).load(cntct.imgPth).into(itemView.imageContact)
            } else {
                Glide.with(context).load(R.mipmap.ic_launcher).into(itemView.imageContact)
            }
            /* itemView.imgShare.setOnClickListener {
                 onShareClick(cntct.name, cntct.number, cntct.email)
             }*/
            itemView.setOnClickListener {
                onCardClick(cntct.name, cntct.number, cntct.email, cntct.imgPth, pos)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cntct = contacts[position]
        holder.setData(cntct, position)
    }
}
