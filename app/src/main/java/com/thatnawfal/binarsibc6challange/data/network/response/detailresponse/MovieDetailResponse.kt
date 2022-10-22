package com.thatnawfal.binarsibc6challange.data.network.response.detailresponse


import com.google.gson.annotations.SerializedName
import com.thatnawfal.binarsibc6challange.data.network.response.detailresponse.Genre

data class MovieDetailResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("backdrop_path") // Backdrop
    val backdropPath: String?,
    @SerializedName("genres")
    val genres: List<Genre?>?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?
)