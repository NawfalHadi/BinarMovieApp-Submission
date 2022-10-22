package com.thatnawfal.binarsibc6challange.presentation.logic.account

import androidx.lifecycle.*
import com.thatnawfal.binarsibc6challange.data.local.model.AccountModel
import com.thatnawfal.binarsibc6challange.data.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: LocalRepository
)  : ViewModel()  {

    fun registerAccount(username: String, email : String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.registerAccount(
                username,
                password,
                email
            )
        }
    }

    fun getAccountData(): LiveData<AccountModel> {
        return repository.getAccountData().asLiveData()
    }

    fun getLoginStatus(): LiveData<Boolean> {
        return repository.getLoginStatus().asLiveData()
    }

    // Set Data

    fun setLoginStatus(status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setLoginStatus(status)
        }
    }

    fun setUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setUsername(username)
        }
    }

    fun setEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setEmail(email)
        }
    }

    fun setPassword(password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setPassword(password)
        }
    }

    fun setImage(image: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.setImage(image)
        }
    }

}