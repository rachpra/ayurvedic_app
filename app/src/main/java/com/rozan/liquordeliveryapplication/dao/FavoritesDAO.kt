package com.rozan.liquordeliveryapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rozan.liquordeliveryapplication.entity.Favorites
@Dao
interface FavoritesDAO {
    @Query("SELECT * FROM favorite")
    fun loadAllFavorite(): MutableList<Favorites>

    @Insert
    fun insertFavorite(favoriteList: Favorites?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFavorite(favoriteList: Favorites?)

    @Delete
    fun deleteFavorite(favoriteList: Favorites?)

    @Query("DELETE FROM favorite WHERE ailaId = :ailaId")
    fun deleteFavoriteWithId(ailaId: Int)

    @Query("SELECT * FROM favorite WHERE ailaId = :id")
    fun loadFavoriteById(id: Int?):MutableList<Favorites>
}