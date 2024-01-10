package com.thanhitutc.moviedbcompose.data.remote.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadService {
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}