package com.rozan.liquordeliveryapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rozan.liquordeliveryapplication.entity.Aila

@Dao
interface AilaDAO {
    @Insert
    suspend fun insertAila(allAila:MutableList<Aila>)

    @Query("select * from Aila")
    suspend fun getAila():MutableList<Aila>
}