package com.thanhitutc.moviedbcompose.data.repository.impl

import com.thanhitutc.moviedbcompose.data.repository.MovieRepository
import com.thanhitutc.moviedbcompose.data.remote.api.ApiService
import com.thanhitutc.moviedbcompose.data.remote.response.GetMovieListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class MovieRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    @Named("io") private val ioDispatcher: CoroutineDispatcher
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): GetMovieListResponse = withContext(ioDispatcher) {
        apiService.getPopularMovies(page)
    }


}