package com.thatnawfal.binarsibc6challange.data.local.datasource

import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteDao
import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteEntity

interface FavoriteDataSource {
    suspend fun addFavorite(favorite : FavoriteEntity): Long
    suspend fun getFavorite(uid : String) : List<FavoriteEntity>
    suspend fun removeFavorite(favorite: FavoriteEntity): Int
}

class FavoriteDataSourceImpl(
    private var favoriteDao: FavoriteDao
) : FavoriteDataSource {
    override suspend fun addFavorite(favorite: FavoriteEntity): Long {
        return favoriteDao.addFavorite(favorite)
    }

    override suspend fun getFavorite(uid: String): List<FavoriteEntity> {
        return favoriteDao.getFavorite(uid)
    }

    override suspend fun removeFavorite(favorite: FavoriteEntity): Int {
        return favoriteDao.removeFavotite(favorite)
    }


}