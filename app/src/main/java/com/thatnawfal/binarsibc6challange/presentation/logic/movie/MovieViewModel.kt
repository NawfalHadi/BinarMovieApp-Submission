package com.thatnawfal.binarsibc6challange.presentation.logic.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatnawfal.binarsibc5challange.wrapper.Resource
import com.thatnawfal.binarsibc6challange.data.network.response.ListResponse
import com.thatnawfal.binarsibc6challange.data.network.response.MoviesListItemResponse
import com.thatnawfal.binarsibc6challange.data.network.response.detailresponse.MovieDetailResponse
import com.thatnawfal.binarsibc6challange.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {

    val loadAllCategory = MutableLiveData(0)

    val nowPlayingMovies = MutableLiveData<Resource<ListResponse<MoviesListItemResponse>>>()
    val topRatedListMovies = MutableLiveData<Resource<ListResponse<MoviesListItemResponse>>>()
    val popularListMovies = MutableLiveData<Resource<ListResponse<MoviesListItemResponse>>>()

    fun loadNowPlayingMovies(){
        nowPlayingMovies.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val list =  repository.loadNowPlayingMovies()
            delay(200)
            viewModelScope.launch(Dispatchers.Main) {
                nowPlayingMovies.postValue(list)
            }
        }
    }

    fun loadTopRatedMovies(){
        topRatedListMovies.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.loadTopRatedMovies()
            viewModelScope.launch(Dispatchers.Main) {
                topRatedListMovies.postValue(list)
            }
        }
    }

    fun loadPopularMovies(){
        popularListMovies.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.loadPopularMovies()
            viewModelScope.launch(Dispatchers.Main) {
                popularListMovies.postValue(list)
            }
        }
    }

    // DetailMovies

    val resultListRecommended = MutableLiveData<Resource<ListResponse<MoviesListItemResponse>>>()
    val resultDetailMovie = MutableLiveData<Resource<MovieDetailResponse>>()

    fun loadDetailMovie(movieId: Int, language:String){
        resultDetailMovie.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val movie = repository.loadDetailMovie(movieId, language)
            viewModelScope.launch(Dispatchers.Main) {
                resultDetailMovie.postValue(movie)
            }
        }
    }

    fun loadRecommendedMovies(movieId: Int){
        resultListRecommended.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val movies = repository.loadRecommendedMovies(movieId)
            viewModelScope.launch(Dispatchers.Main) {
                resultListRecommended.postValue(movies)
            }
        }
    }
}