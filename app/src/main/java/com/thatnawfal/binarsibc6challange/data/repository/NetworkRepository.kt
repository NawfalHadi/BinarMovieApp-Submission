package com.thatnawfal.binarsibc6challange.data.repository

import com.thatnawfal.binarsibc5challange.wrapper.Resource
import com.thatnawfal.binarsibc6challange.data.network.datasource.MovieDataSource
import com.thatnawfal.binarsibc6challange.data.network.response.ListResponse
import com.thatnawfal.binarsibc6challange.data.network.response.MoviesListItemResponse
import com.thatnawfal.binarsibc6challange.data.network.response.detailresponse.MovieDetailResponse

interface NetworkRepository {
    suspend fun loadNowPlayingMovies(): Resource<ListResponse<MoviesListItemResponse>>
    suspend fun loadDetailMovie(movieId: Int, language: String): Resource<MovieDetailResponse>

    suspend fun loadTopRatedMovies(): Resource<ListResponse<MoviesListItemResponse>>
    suspend fun loadPopularMovies(): Resource<ListResponse<MoviesListItemResponse>>
    suspend fun loadRecommendedMovies(movieId: Int): Resource<ListResponse<MoviesListItemResponse>>
}

class NetworkRepositoryImpl(
    private val dataSource: MovieDataSource
) : NetworkRepository {
    override suspend fun loadNowPlayingMovies(): Resource<ListResponse<MoviesListItemResponse>> {
        return loadListData(dataSource.loadNowPlayingMovies())
    }

    override suspend fun loadDetailMovie(movieId: Int, language: String): Resource<MovieDetailResponse> {
        return try {
            val movie = dataSource.loadDetailMovie(movieId, language)
            if (movie.title.isNullOrEmpty()) {
                Resource.Empty()
            } else {
                Resource.Success(movie)
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun loadTopRatedMovies(): Resource<ListResponse<MoviesListItemResponse>> {
        return loadListData(dataSource.loadTopRatedMovies())
    }

    override suspend fun loadPopularMovies(): Resource<ListResponse<MoviesListItemResponse>> {
        return loadListData(dataSource.loadPopularMovies())
    }

    override suspend fun loadRecommendedMovies(movieId: Int): Resource<ListResponse<MoviesListItemResponse>> {
        return loadListData(dataSource.loadRecommendedMovies(movieId))
    }

    private fun loadListData(list: ListResponse<MoviesListItemResponse>): Resource<ListResponse<MoviesListItemResponse>>{
        return try {
            if (list.result.isNullOrEmpty()){
                Resource.Empty()
            } else {
                Resource.Success(list)
            }
        } catch (e : Exception) {
            Resource.Error(e)
        }
    }
}