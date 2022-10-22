package com.thatnawfal.binarsibc6challange.di

import androidx.datastore.preferences.protobuf.Api
import com.thatnawfal.binarsibc6challange.BuildConfig
import com.thatnawfal.binarsibc6challange.data.network.ApiService
import com.thatnawfal.binarsibc6challange.data.network.datasource.MovieDataSource
import com.thatnawfal.binarsibc6challange.data.network.datasource.MovieDataSourceImpl
import com.thatnawfal.binarsibc6challange.data.repository.NetworkRepository
import com.thatnawfal.binarsibc6challange.data.repository.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun logging() : HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun okHtppClient(logging: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @Provides
    @Singleton
    fun provideApiService (client: OkHttpClient) : ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(apiService: ApiService) : MovieDataSource {
        return MovieDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(movieDataSource: MovieDataSource) : NetworkRepository {
        return NetworkRepositoryImpl(movieDataSource)
    }
}