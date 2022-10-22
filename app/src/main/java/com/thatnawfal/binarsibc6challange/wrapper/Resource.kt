package com.thatnawfal.binarsibc5challange.wrapper

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/

sealed class Resource<T>(
    val payload: T? = null,
    val message: String? = null,
    val exception: Exception? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Empty<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(exception: Exception?, data: T? = null) : Resource<T>(data, exception = exception)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}