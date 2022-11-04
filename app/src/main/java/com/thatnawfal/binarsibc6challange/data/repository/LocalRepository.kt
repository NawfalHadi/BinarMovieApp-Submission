package com.thatnawfal.binarsibc6challange.data.repository

import com.thatnawfal.binarsibc5challange.wrapper.Resource
import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteEntity
import com.thatnawfal.binarsibc6challange.data.local.datasource.FavoriteDataSource
import com.thatnawfal.binarsibc6challange.data.local.datasource.LocalDataSource
import com.thatnawfal.binarsibc6challange.data.local.datastore.AccountDataStoreManager
import com.thatnawfal.binarsibc6challange.data.local.model.AccountModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.singleOrNull

interface LocalRepository {
    //Authentication
    suspend fun registerAccount(username: String, password:String, email:String)
    fun getLoginStatus() : Flow<Boolean>

    //Edit Account Data
    suspend fun setImage(image: String)
    suspend fun setUsername(username: String)
    suspend fun setEmail(email: String)
    suspend fun setPassword(password: String)
    suspend fun setLoginStatus(status: Boolean)

    //Get Account Data
    fun getAccountData() : Flow<AccountModel>

    // Add Favorite
    suspend fun addFavorite(favorite : FavoriteEntity) : Resource<Long>

    // Remove Favorite
    suspend fun removeFavorite(favorite: FavoriteEntity) : Resource<Int>

    // Get List Favorite
    suspend fun getFavorite(uid: String) : Resource<List<FavoriteEntity>>

}

class LocalRepositroyImpl(
    private val localDataSource: LocalDataSource,
    private val favoriteDataSource: FavoriteDataSource
) : LocalRepository{
    override suspend fun registerAccount(username: String, password: String, email: String) {
        localDataSource.registerAccount(username, password, email)
    }

    override fun getLoginStatus(): Flow<Boolean> {
        return localDataSource.getLoginStatus()
    }

    override suspend fun setImage(image: String) {
        localDataSource.setImage(image)
    }

    override suspend fun setUsername(username: String) {
        localDataSource.setUsername(username)
    }

    override suspend fun setEmail(email: String) {
        localDataSource.setEmail(email)
    }

    override suspend fun setPassword(password: String) {
        localDataSource.setPassword(password)
    }

    override suspend fun setLoginStatus(status: Boolean) {
        localDataSource.setLoginStatus(status)
    }

    override fun getAccountData(): Flow<AccountModel> {
        return localDataSource.getAccountData()
    }

    // Using Favorite Repository

    override suspend fun addFavorite(favorite: FavoriteEntity): Resource<Long> {
        return proceed {
            favoriteDataSource.addFavorite(favorite)
        }
    }

    override suspend fun removeFavorite(favorite: FavoriteEntity): Resource<Int> {
        return proceed {
            favoriteDataSource.removeFavorite(favorite)
        }
    }

    override suspend fun getFavorite(uid: String): Resource<List<FavoriteEntity>> {
        return proceed {
            favoriteDataSource.getFavorite(uid)
        }
    }

    private suspend fun <T> proceed(coroutine: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutine.invoke())
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

}
