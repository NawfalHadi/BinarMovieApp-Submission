package com.thatnawfal.binarsibc6challange.data.network.firebase

import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteEntity

interface StorageDataSource {
    suspend fun getFavorite(uid: String) : List<FavoriteEntity>
    suspend fun addFavorite(filmId : Int, uid: String, poster: String) : String
    suspend fun deleteFavorite(filmId: Int) : Boolean
    suspend fun checkFavorite(filmId: Int): Boolean
}

class StorageDataSourceImpl(
    private val firebase: StorageFirebase
) : StorageDataSource {

    override suspend fun getFavorite(uid: String): List<FavoriteEntity> {
        return firebase.getFavorite(uid)
    }

    override suspend fun addFavorite(filmId: Int, uid: String, poster: String): String {
        return firebase.addFavorite(
            uid,
            filmId,
            poster
        )
    }

    override suspend fun deleteFavorite(filmId: Int) : Boolean {
        return firebase.deleteFavorite(filmId)
    }

    override suspend fun checkFavorite(filmId: Int): Boolean {
        return firebase.checkFavorite(filmId)
    }

}