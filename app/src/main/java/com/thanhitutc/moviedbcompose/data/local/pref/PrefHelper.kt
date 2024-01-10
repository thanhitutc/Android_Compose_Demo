package com.thanhitutc.moviedbcompose.data.local.pref

import kotlinx.coroutines.flow.Flow

interface PrefHelper {

    suspend fun isFirstRun(): Flow<Boolean>

    suspend fun clear()
}