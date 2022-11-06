package com.thatnawfal.binarsibc6challange.presentation.logic.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.thatnawfal.binarsibc5challange.wrapper.Resource
import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteEntity
import com.thatnawfal.binarsibc6challange.data.repository.LocalRepository
import com.thatnawfal.binarsibc6challange.data.repository.NetworkRepository
import com.thatnawfal.binarsibc6challange.wrapper.Event
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

    private val _listFavoriteResult = MutableLiveData<List<FavoriteEntity>>()
    val listFavoriteResult : LiveData<List<FavoriteEntity>> = _listFavoriteResult

    val newFavorite = MutableLiveData<Resource<String>>()
    val isFavorited = MutableLiveData<Boolean>()

    val msgSnackBar = MutableLiveData<Event<String>>()
    val db = Firebase.firestore

    // Local

//    fun getFavoriteLocal(uid : String) {
//        viewModelScope.launch(Dispatchers.IO){
//            listFavoriteResult.postValue(Resource.Loading())
//            val data = localRepository.getFavorite(uid)
//            delay(1000)
//            viewModelScope.launch(Dispatchers.Main){
//                listFavoriteResult.postValue(data)
//            }
//        }
//    }

    // Firebase

    fun getFavoriteNetwork() {
        viewModelScope.launch {
            val listFavoriteEntity = mutableListOf<FavoriteEntity>()
            db.collection("favorite")
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        val favorite = FavoriteEntity(
                            document.id.toInt(),
                            document.data["uid"] as String?,
                            document.data["poster"] as String?
                        )
                        listFavoriteEntity.add(favorite)
                        Log.d("Firebase", "${document.id} => ${document.data["poster"].toString()}")
                    }
                    _listFavoriteResult.postValue(listFavoriteEntity)
                }
                .addOnFailureListener {
                    Log.w("Firebase", "Error getting documents.", it)
                }

        }
    }

    fun addFavoriteNetwork(filmId: Int, uid: String, poster:String) {
        viewModelScope.launch(Dispatchers.IO) {
            newFavorite.postValue(Resource.Loading())
            val data = networkRepository.addFavorite(
                filmId, uid, poster
            )
            delay(1000)
            viewModelScope.launch(Dispatchers.Main) {
                newFavorite.postValue(data)
                msgSnackBar.value = Event("Added to Favorite")
            }
        }
    }

    fun deleteFavoriteNetwork(filmId: Int) {
        viewModelScope.launch {
            val deleting = networkRepository.deleteFavorite(filmId)
            delay(500)
            if (deleting){
                msgSnackBar.value = Event("Remove From Favorite")
            }
        }
    }

    fun checkFavoriteNetwork(filmId: Int){
        viewModelScope.launch {
            val checkFav = networkRepository.checkFavorite(filmId)
            if (checkFav) {
                isFavorited.postValue(true)
            } else {
                isFavorited.postValue(false)
            }
        }
    }
}