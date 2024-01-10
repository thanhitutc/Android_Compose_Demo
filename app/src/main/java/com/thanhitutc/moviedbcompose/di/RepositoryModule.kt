package com.thanhitutc.moviedbcompose.di

import com.thanhitutc.moviedbcompose.data.repository.MovieRepository
import com.thanhitutc.moviedbcompose.data.repository.impl.MovieRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideWallpaperRepository(movieRepositoryImpl: MovieRepositoryImp): MovieRepository =
        movieRepositoryImpl
}