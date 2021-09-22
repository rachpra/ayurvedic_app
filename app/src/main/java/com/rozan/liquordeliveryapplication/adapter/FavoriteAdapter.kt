package com.rozan.liquordeliveryapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rozan.liquordeliveryapplication.R
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.entity.Cart
import com.rozan.liquordeliveryapplication.entity.Favorites

class FavoriteAdapter(
        val lstFavorite:MutableList<Favorites>,
        val context: Context
):RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    class FavoriteViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ailaImage: ImageView
        val ailaPrice: TextView
        val ailaMl: TextView
        val ailaName: TextView
        val ailaType:TextView

        init {
            ailaImage=view.findViewById(R.id.imgProfile)
            ailaPrice=view.findViewById(R.id.ailaPrice)
            ailaMl=view.findViewById(R.id.ailaMl)
            ailaName=view.findViewById(R.id.ailaName)
            ailaType=view.findViewById(R.id.ailaType)
    }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.favorites_layout,parent,false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val fav=lstFavorite[position]
        holder.ailaPrice.text=fav.ailaPrice.toString()
        holder.ailaMl.text=fav.ailaMl
        holder.ailaName.text=fav.ailaName
        holder.ailaType.text=fav.ailaType
        val imagePath = ServiceBuilder.loadImagePath() + fav.ailaImage!!.split("\\")[1]
        Glide.with(context)
                .load(imagePath)
                .into(holder.ailaImage)
    }

    override fun getItemCount(): Int {
        return lstFavorite.size
    }
}