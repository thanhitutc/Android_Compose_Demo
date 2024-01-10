package com.thanhitutc.moviedbcompose.di

import android.content.Context
import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import com.thanhitutc.moviedbcompose.BuildConfig
import com.thanhitutc.moviedbcompose.data.remote.api.DownloadService
import com.thanhitutc.moviedbcompose.isDevMode
import com.thanhitutc.moviedbcompose.data.remote.api.ApiService
import com.thanhitutc.moviedbcompose.data.remote.api.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private val TIME_OUT = 10L

    @Singleton
    @Provides
    fun provideOkHttpCache(context: Context): Cache =
        Cache(context.cacheDir, (10 * 1024 * 1024).toLong())

    @Singleton
    @Provides
    @Named("logging")
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = if (isDevMode()) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

    @Singleton
    @Provides
    @Named("header")
    fun provideHeaderInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val newUrl = request.url.newBuilder()
//                .addQueryParameter("X-API-Key", BuildConfig.WALL_HAVEN_API_KEY)
                .build()
            val newRequest = request.newBuilder()
                .url(newUrl)
                .method(request.method, request.body)
                .build()
            chain.proceed(newRequest)
        }

    @Singleton
    @Provides
    @Named("mock")
    fun provideMockInterceptor(assetManager: AssetManager): MockInterceptor =
        MockInterceptor(assetManager)

    @Singleton
    @Provides
    @Named("baseOkHttpClient")
    fun provideOkHttpClient(
        @Named("logging") logging: Interceptor,
        @Named("header") header: Interceptor,
//        authenticator: Authenticator,
        @Named("mock") mockInterceptor: MockInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .apply {
                addInterceptor(logging)
//                if (BuildConfig.DEBUG) addInterceptor(OkHttpProfilerInterceptor())
                addInterceptor(header)
//                authenticator(authenticator)
                if (BuildConfig.DEBUG && BuildConfig.MOCK_DATA) addInterceptor(mockInterceptor)
            }
            .build()

    @Singleton
    @Provides
    @Named("baseAppRetrofit")
    fun provideAppRetrofit(
        @Named("baseOkHttpClient") okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideApiService(
        @Named("baseAppRetrofit") retrofit: Retrofit
    ): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    @Named("downloadAppRetrofit")
    fun provideDownloadRetrofit(
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.BASE_URL)
            .client(OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(0, TimeUnit.SECONDS)
                .addNetworkInterceptor { chain ->
                    chain.proceed(chain.request()).also { originalResponse ->
                        originalResponse.body
                    }
                }
                .build())
            .build()

    @Singleton
    @Provides
    fun provideDownloadService(
        @Named("downloadAppRetrofit") retrofit: Retrofit
    ): DownloadService =
        retrofit.create(DownloadService::class.java)

}
