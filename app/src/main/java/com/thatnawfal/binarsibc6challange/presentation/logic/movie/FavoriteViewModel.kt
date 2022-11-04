package com.thatnawfal.binarsibc6challange.presentation.logic.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatnawfal.binarsibc5challange.wrapper.Resource
import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteEntity
import com.thatnawfal.binarsibc6challange.data.repository.LocalRepository
import com.thatnawfal.binarsibc6challange.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    val listFavoriteResult = MutableLiveData<Resource<List<FavoriteEntity>>>()
    val newFavorite = MutableLiveData<Resource<Number>>()

    // Local

    fun getFavoriteLocal(uid : String) {
        viewModelScope.launch(Dispatchers.IO){
            listFavoriteResult.postValue(Resource.Loading())
            val data = localRepository.getFavorite(uid)
            delay(1000)
            viewModelScope.launch(Dispatchers.Main){
                listFavoriteResult.postValue(data)
            }
        }
    }

    // Firebase
}