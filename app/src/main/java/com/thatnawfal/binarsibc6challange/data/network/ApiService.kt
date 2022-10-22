package com.thatnawfal.binarsibc6challange.data.network

import com.thatnawfal.binarsibc6challange.BuildConfig
import com.thatnawfal.binarsibc6challange.data.network.response.ListResponse
import com.thatnawfal.binarsibc6challange.data.network.response.MoviesListItemResponse
import com.thatnawfal.binarsibc6challange.data.network.response.detailresponse.MovieDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/now_playing")
    suspend fun getMoviePlaying(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ) : ListResponse<MoviesListItemResponse>

    @GET("movie/{id}?")
    suspend fun getMovieDetail(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String
    ) : MovieDetailResponse

    @GET("movie/top_rated")
    suspend fun getMovieTopRated(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ) : ListResponse<MoviesListItemResponse>

    @GET("movie/popular")
    suspend fun getPopularMovie(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ) : ListResponse<MoviesListItemResponse>

    @GET("movie/{id}/recommendations")
    suspend fun getRecommendationMovie(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ) : ListResponse<MoviesListItemResponse>
}