package com.thanhitutc.moviedbcompose.data.repository

import com.thanhitutc.moviedbcompose.data.remote.response.GetMovieListResponse

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): GetMovieListResponse

}
