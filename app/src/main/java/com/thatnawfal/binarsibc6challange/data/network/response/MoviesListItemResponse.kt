package com.thatnawfal.binarsibc6challange.data.network.response


import com.google.gson.annotations.SerializedName

data class MoviesListItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("title")
    val title: String?
)