package com.rozan.ailawaros.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rozan.ailawaros.R
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.entity.Aila
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AilaAdapter(
        val lstAila: MutableList<Aila>,
        val context: Context
):RecyclerView.Adapter<AilaAdapter.AilaViewHolder>()
{
    class AilaViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ailaImage:ImageView
        val ailaPrice:TextView
        val ailaName:TextView

        init {
            ailaImage=view.findViewById(R.id.itemimage)
            ailaPrice=view.findViewById(R.id.itemname)
            ailaName=view.findViewById(R.id.itemrate)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AilaViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return AilaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AilaViewHolder, position: Int) {
        val aila=lstAila[position]
        holder.ailaPrice.text="Rs. ${aila.ailaPrice.toString()}"
        holder.ailaName.text=aila.ailaName.toString()

        val imagePath = ServiceBuilder.loadImagePath() + aila.ailaImage!!.split("\\")[1]
        Glide.with(context)
                .load(imagePath)
                .into(holder.ailaImage)

        }

    override fun getItemCount(): Int {
        return lstAila.size
    }
}