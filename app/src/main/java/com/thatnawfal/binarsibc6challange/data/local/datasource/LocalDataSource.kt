package com.thatnawfal.binarsibc6challange.data.local.datasource

import com.thatnawfal.binarsibc6challange.data.local.datastore.AccountDataStoreManager
import com.thatnawfal.binarsibc6challange.data.local.model.AccountModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface LocalDataSource {
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

class LocalDataSourceImpl (
        private val dataStoreManager: AccountDataStoreManager
    ) : LocalDataSource {

    //Authentication
    override suspend fun registerAccount(username: String, password: String, email: String) {
        dataStoreManager.registerAccount(username, password, email)
    }

    override fun getLoginStatus(): Flow<Boolean> {
        return dataStoreManager.getLoginStatus()
    }

    //Edit Account Data
    override suspend fun setImage(image: String) {
        dataStoreManager.setImage(image)
    }

    override suspend fun setUsername(username: String) {
        dataStoreManager.setUsername(username)
    }

    override suspend fun setEmail(email: String) {
        dataStoreManager.setEmail(email)
    }

    override suspend fun setPassword(password: String) {
        dataStoreManager.setPassword(password)
    }

    override suspend fun setLoginStatus(status: Boolean) {
        dataStoreManager.setStatusLogin(status)
    }

    //Get Account Data
    override fun getAccountData(): Flow<AccountModel> {
        return dataStoreManager.getAccountData()
    }

}


