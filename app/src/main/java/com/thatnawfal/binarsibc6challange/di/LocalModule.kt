package com.thatnawfal.binarsibc6challange.di

import android.content.Context
import com.thatnawfal.binarsibc6challange.data.local.database.AppDatabase
import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteDao
import com.thatnawfal.binarsibc6challange.data.local.datasource.FavoriteDataSource
import com.thatnawfal.binarsibc6challange.data.local.datasource.FavoriteDataSourceImpl
import com.thatnawfal.binarsibc6challange.data.local.datasource.LocalDataSource
import com.thatnawfal.binarsibc6challange.data.local.datasource.LocalDataSourceImpl
import com.thatnawfal.binarsibc6challange.data.local.datastore.AccountDataStoreManager
import com.thatnawfal.binarsibc6challange.data.repository.LocalRepository
import com.thatnawfal.binarsibc6challange.data.repository.LocalRepositroyImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    // Providing Data Store

    @Provides
    @Singleton
    fun provideAccountDataStoreManager(
        @ApplicationContext ctx: Context
    ) : AccountDataStoreManager{
        return AccountDataStoreManager(ctx)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource (
        dataStoreManager: AccountDataStoreManager
    ) : LocalDataSource {
        return LocalDataSourceImpl(dataStoreManager)
    }

    // Providing AppDatabase

    @Provides
    fun provideAppDatabase (
        @ApplicationContext ctx: Context
    ) : AppDatabase {
        return AppDatabase.getInstance(ctx)
    }

    //  Providing Favorite Room

    @Provides
    fun provideFavoriteDao(
        @ApplicationContext ctx: Context
    ) : FavoriteDao {
        return provideAppDatabase(ctx).favoriteDao()
    }

    @Provides
    fun provideFavoriteDataSource(
        favoriteDao : FavoriteDao
    ) : FavoriteDataSource {
        return FavoriteDataSourceImpl(favoriteDao)
    }

    //  Providing Local Repository

    @Provides
    @Singleton
    fun provideLocalRepository (
        localDataSource: LocalDataSource,
        favoriteDataSource: FavoriteDataSource
    ) : LocalRepository {
        return LocalRepositroyImpl(
            localDataSource,
            favoriteDataSource
        )
    }
}