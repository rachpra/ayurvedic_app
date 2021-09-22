package com.rozan.liquordeliveryapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rozan.liquordeliveryapplication.entity.Cart

@Dao
interface CartDAO {
    @Insert
    suspend fun insertCart(allCart:MutableList<Cart>)

    @Query("Select * from Cart")
    suspend fun getAllCart():MutableList<Cart>
}