package com.thatnawfal.binarsibc6challange.di

import android.content.Context
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
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

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

    @Provides
    @Singleton
    fun provideLocalRepository (
        localDataSource: LocalDataSource
    ) : LocalRepository {
        return LocalRepositroyImpl(localDataSource)
    }
}