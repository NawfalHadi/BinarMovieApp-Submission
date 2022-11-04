package com.thatnawfal.binarsibc6challange.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {

    // Add To Room
    @Insert
    suspend fun addFavorite(favorite : FavoriteEntity) : Long

    // Get From Room
    @Query("SELECT * FROM FAVORITE WHERE id = :uid")
    suspend fun getFavorite(uid: String): List<FavoriteEntity>

    // Delete From Room
    @Delete
    suspend fun removeFavotite(favorite: FavoriteEntity) : Int

}