package com.thanhitutc.moviedbcompose.data.remote.api

import com.thanhitutc.wallpaperpro.data.model.Movie
import com.thanhitutc.moviedbcompose.data.remote.response.GetMovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET(ApiPath.DISCOVER_MOVIE)
    suspend fun getDiscoverMovie(@QueryMap hashMap: HashMap<String, String> = HashMap()): GetMovieListResponse

    @GET(ApiPath.MOVIE_POPULAR)
    suspend fun getPopularMovies(@Query("page") page: Int): GetMovieListResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") movieId: String): Movie

}