package com.rozan.liquordeliveryapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rozan.liquordeliveryapplication.entity.User

@Dao
interface UserDAO {
    @Insert  //Inserting into database
    suspend fun registerUser(user: User)

    @Query("select * from User where username=(:username) and password=(:password)")
    suspend fun checkUser(username:String,password:String):User

}