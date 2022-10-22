package com.thatnawfal.binarsibc6challange.data.repository

import com.thatnawfal.binarsibc5challange.wrapper.Resource
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
}

class LocalRepositroyImpl(
    private val localDataSource: LocalDataSource
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

}
