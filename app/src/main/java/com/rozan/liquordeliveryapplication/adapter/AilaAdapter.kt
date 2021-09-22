package com.rozan.liquordeliveryapplication.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rozan.liquordeliveryapplication.AilaDetailsActivity
import com.rozan.liquordeliveryapplication.MainActivity
import com.rozan.liquordeliveryapplication.R
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.db.AilaDB
import com.rozan.liquordeliveryapplication.entity.Aila
import com.rozan.liquordeliveryapplication.entity.Favorites
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
        val ailaMl:TextView
        val ailaName:TextView
        val ailaType:TextView
        val imgFavorite:ImageView
        val imgCart:ImageView

        init {
            ailaImage=view.findViewById(R.id.image)
            ailaPrice=view.findViewById(R.id.price)
            ailaMl=view.findViewById(R.id.ml)
            ailaName=view.findViewById(R.id.name)
            ailaType=view.findViewById(R.id.type)
            imgFavorite=view.findViewById(R.id.imgFavorite)
            imgCart=view.findViewById(R.id.imgCart)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AilaViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.show_aila_layout, parent, false)
        return AilaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AilaViewHolder, position: Int) {
        val aila=lstAila[position]
        holder.ailaPrice.text=aila.ailaPrice.toString()
        holder.ailaMl.text=aila.ailaMl.toString()
        holder.ailaName.text=aila.ailaName.toString()
        holder.ailaType.text=aila.ailaType.toString()

        val imagePath = ServiceBuilder.loadImagePath() + aila.ailaImage!!.split("\\")[1]
        Glide.with(context)
                .load(imagePath)
                .into(holder.ailaImage)


        holder.ailaImage.setOnClickListener {
            val intent= Intent(context, AilaDetailsActivity::class.java)
            intent.putExtra("aila", aila)
            context.startActivity(intent)
        }


        CoroutineScope(Dispatchers.IO).launch {
            if (AilaDB.getInstance(context).getFavoritesDAO().loadFavoriteById(aila.ailaId).equals(1)){
                withContext(Main){
                    holder.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                }

            }
            else
                withContext(Main){
                    holder.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }

        }

        holder.imgFavorite.setOnClickListener {

            val id: Int = aila.ailaId.toString().toInt()
            val image: String = aila.ailaImage.toString()
            val name: String = aila.ailaName.toString()
            val ml: String = aila.ailaMl.toString()
            val type: String = aila.ailaType.toString()
            val price: Double = aila.ailaPrice.toString().toDouble()

            val favoriteList = Favorites(id,image,ailaName = name,ailaMl = ml,ailaType = type,ailaPrice = price)
            CoroutineScope(Dispatchers.IO).launch {
                if (!AilaDB.getInstance(context).getFavoritesDAO().loadFavoriteById(id).equals(1)){
                    AilaDB.getInstance(context).getFavoritesDAO().insertFavorite(favoriteList)
                    withContext(Main){
                        holder.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    }
                }
                else {
                    withContext(Main){
                        holder.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }

//                    MainActivity.favoriteDatabase.favoriteDao().delete(favoriteList);

                }
                }
//            var isfav: Boolean = readState()
//            if (isfav) {
//                holder.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
//                CoroutineScope(Dispatchers.IO).launch {
//                    AilaDB.getInstance(context).getFavoritesDAO().insertFavorite(favoriteList)
//                }
//                isfav = false
//                saveState(isfav)
//                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
//            } else {
//                holder.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
//                holder.imgFavorite.setTag("border");
//                isfav = true
//                saveState(isfav)
//                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
//            }





            }
        }


    private fun saveState(isFavourite: Boolean) {
        val aSharedPreferences: SharedPreferences = context.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE)
        val aSharedPreferencesEdit = aSharedPreferences
                .edit()
        aSharedPreferencesEdit.putBoolean("State", isFavourite)
        aSharedPreferencesEdit.commit()
    }

    private fun readState(): Boolean {
        val aSharedPreferences: SharedPreferences = context.getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE)
        return aSharedPreferences.getBoolean("State", true)
    }


    override fun getItemCount(): Int {
        return lstAila.size
    }
}