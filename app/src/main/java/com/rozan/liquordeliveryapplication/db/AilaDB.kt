package com.rozan.liquordeliveryapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rozan.liquordeliveryapplication.dao.AilaDAO
import com.rozan.liquordeliveryapplication.dao.CartDAO
import com.rozan.liquordeliveryapplication.dao.FavoritesDAO
import com.rozan.liquordeliveryapplication.dao.UserDAO
import com.rozan.liquordeliveryapplication.entity.Aila
import com.rozan.liquordeliveryapplication.entity.Cart
import com.rozan.liquordeliveryapplication.entity.Favorites
import com.rozan.liquordeliveryapplication.entity.User

@Database(
        entities =[(User::class),(Aila::class),(Cart::class),(Favorites::class)],
        version = 3,
        exportSchema = false
)
abstract class AilaDB:RoomDatabase() {
    abstract fun getUserDAO():UserDAO  //instance of UserDAO interface to access the function
    abstract fun getAilaDAO():AilaDAO
    abstract fun getCartDAO():CartDAO
    abstract fun getFavoritesDAO():FavoritesDAO
    companion object{
        @Volatile
        private var instance:AilaDB?=null
        fun getInstance(context: Context):AilaDB{
            if (instance==null){
                synchronized(AilaDB::class){
                    instance=buildDatabase(context)
                }
            }
            return instance!!
        }

        //function for building database
        private fun buildDatabase(context: Context)=
                Room.databaseBuilder(
                        context.applicationContext,
                        AilaDB::class.java,
                        "AilaDB"
                ).fallbackToDestructiveMigration().build()
    }
}