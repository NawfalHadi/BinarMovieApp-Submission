package com.thatnawfal.binarsibc6challange.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.thatnawfal.binarsibc6challange.data.local.model.AccountModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountDataStoreManager(private val ctx : Context) {

    // Authentication
    suspend fun registerAccount(_username: String, _password: String, _email: String){
        ctx.accountDataStore.edit {
            it[username] = _username
            it[password] = _password
            it[email] = _email
            it[isLogin] = false
        }
    }

    fun getLoginStatus(): Flow<Boolean> {
        return ctx.accountDataStore.data.map {
            it[isLogin] ?: false
        }
    }

    // Edit Account Data
    suspend fun setImage(_image: String) {
        ctx.accountDataStore.edit {
            it[image] = _image
        }
    }

    suspend fun setUsername(_username: String){
        ctx.accountDataStore.edit {
            it[username] = _username
        }
    }

    suspend fun setEmail(_email: String) {
        ctx.accountDataStore.edit {
            it[email] = _email
        }
    }

    suspend fun setPassword(_password: String){
        ctx.accountDataStore.edit {
            it[password] = _password
        }
    }

    suspend fun setStatusLogin(_status: Boolean){
        ctx.accountDataStore.edit {
            it[isLogin] = _status
        }
    }

    // Get Account Data
    fun getAccountData() : Flow<AccountModel> {
        return ctx.accountDataStore.data.map {
            AccountModel(
                it[image] ?: "",
                it[username] ?: "",
                it[email] ?: "",
                it [password] ?: "",
                it[isLogin] ?: false
            )
        }
    }

    companion object {
        private const val DATASTORE_NAME = "account_preference"

        private val image = stringPreferencesKey("image_key")
        private val username = stringPreferencesKey("username_key")
        private val password = stringPreferencesKey("password_key")
        private val email = stringPreferencesKey("email_key")
        private val isLogin = booleanPreferencesKey("isLogin_key")

        private val Context.accountDataStore by preferencesDataStore(
            name = DATASTORE_NAME
        )
    }
}