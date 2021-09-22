package com.rozan.liquordeliveryapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rozan.liquordeliveryapplication.R
import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.entity.Cart
import com.rozan.liquordeliveryapplication.entity.Carts
import com.rozan.liquordeliveryapplication.repository.CartRepository
import com.rozan.liquordeliveryapplication.response.CartResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartAdapter(
        val lstcart:MutableList<Carts>,
        val context: Context
):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ailaImage:ImageView
        val ailaPrice: TextView
        val ailaMl: TextView
        val ailaName: TextView
        val ailaQty:TextView
        val btnDelete:ImageView
        val btnAdd:Button
        val btnSub:Button

        init {
            ailaImage=view.findViewById(R.id.imgProfile)
            ailaPrice=view.findViewById(R.id.ailaPrice)
            ailaMl=view.findViewById(R.id.ailaMl)
            ailaName=view.findViewById(R.id.ailaName)
            ailaQty=view.findViewById(R.id.ailaQtty)
            btnDelete=view.findViewById(R.id.btnDelete)
            btnAdd=view.findViewById(R.id.btnAdd)
            btnSub=view.findViewById(R.id.btnSub)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.cart_layout,parent,false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart=lstcart[position]
        holder.ailaPrice.text=cart.ailaId?.ailaPrice.toString()
        holder.ailaMl.text=cart.ailaId?.ailaMl
        holder.ailaName.text=cart.ailaId?.ailaName
        holder.ailaQty.text=cart.ailaQty.toString()

        val imagePath = ServiceBuilder.loadImagePath() + cart.ailaId?.ailaImage!!.split("\\")[1]
        Glide.with(context)
                .load(imagePath)
//                .apply(RequestOptions.skipMemoryCacheOf(true))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .fitCenter()
                .into(holder.ailaImage)

        holder.btnSub.setOnClickListener {
            var quantity=cart.ailaQty

                quantity=quantity!!-1

            holder.ailaQty.text=quantity.toString()
            if(quantity!!<=1){
                holder.btnSub.isClickable=false
            }
        }
        holder.btnAdd.setOnClickListener {
            var quantity=cart.ailaQty
                quantity= quantity!! +1

            holder.ailaQty.text=quantity.toString()
            if(quantity!!>1){
                holder.btnSub.isClickable=true
            }
            }
        holder.btnDelete.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val cartRepository = CartRepository()
                    val response = cartRepository.deleteCart(cart._id!!)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                    context,
                                    "Cart Deleted",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        withContext(Dispatchers.Main) {
                            lstcart.remove(cart)
                            notifyDataSetChanged()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                                context,
                                ex.toString(),
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        }


    override fun getItemCount(): Int {
       return lstcart.size
    }
}