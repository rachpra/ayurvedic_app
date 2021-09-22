package com.rozan.liquordeliveryapplication.response

import com.rozan.liquordeliveryapplication.entity.Cart
import com.rozan.liquordeliveryapplication.entity.Carts

data class CartResponse(
  val success:Boolean?=null,
  val message:String?=null,
  val data:MutableList<Carts>?=null
)