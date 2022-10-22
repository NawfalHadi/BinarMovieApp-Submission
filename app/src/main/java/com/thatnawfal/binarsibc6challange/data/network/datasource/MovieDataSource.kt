package com.thatnawfal.binarsibc6challange.data.network.datasource

import com.thatnawfal.binarsibc6challange.data.network.ApiService
import com.thatnawfal.binarsibc6challange.data.network.response.ListResponse
import com.thatnawfal.binarsibc6challange.data.network.response.MoviesListItemResponse
import com.thatnawfal.binarsibc6challange.data.network.response.detailresponse.MovieDetailResponse

interface MovieDataSource {
    suspend fun loadNowPlayingMovies(): ListResponse<MoviesListItemResponse>
    suspend fun loadDetailMovie(movieId: Int, language: String): MovieDetailResponse

    suspend fun loadTopRatedMovies(): ListResponse<MoviesListItemResponse>
    suspend fun loadPopularMovies(): ListResponse<MoviesListItemResponse>
    suspend fun loadRecommendedMovies(movieId: Int): ListResponse<MoviesListItemResponse>
}

class MovieDataSourceImpl(
    private val apiService: ApiService
) : MovieDataSource {
    override suspend fun loadNowPlayingMovies(): ListResponse<MoviesListItemResponse> {
        return apiService.getMoviePlaying()
    }

    override suspend fun loadDetailMovie(movieId: Int, language: String): MovieDetailResponse {
        return apiService.getMovieDetail(
            movieId = movieId,
            language = language
        )
    }

    override suspend fun loadTopRatedMovies(): ListResponse<MoviesListItemResponse> {
        return apiService.getMovieTopRated()
    }

    override suspend fun loadPopularMovies(): ListResponse<MoviesListItemResponse> {
        return apiService.getPopularMovie()
    }

    override suspend fun loadRecommendedMovies(movieId: Int): ListResponse<MoviesListItemResponse> {
        return apiService.getRecommendationMovie(movieId)
    }

}