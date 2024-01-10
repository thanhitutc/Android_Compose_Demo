package com.thanhitutc.moviedbcompose.data.remote.api

import com.thanhitutc.moviedbcompose.BuildConfig

object ApiPath {
    const val DISCOVER_MOVIE = "3/discover/movie"
    const val DISCOVER_TV = "3/discover/tv"
    const val MOVIE_POPULAR = "3/movie/popular?api_key=${BuildConfig.WALL_HAVEN_API_KEY}"
}