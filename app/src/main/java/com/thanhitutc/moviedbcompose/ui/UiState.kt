package com.thanhitutc.moviedbcompose.ui

sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data class Success<T>(val result: T) : UiState
    data class Error(val error: ErrorStateType) : UiState
}

sealed class ErrorStateType {
    data object NoInternetConnection : ErrorStateType()
    data object ConnectTimeout : ErrorStateType()
    data object ForceUpdateApp : ErrorStateType()
    data object UnAuthorized : ErrorStateType()
    data object ServerMaintain : ErrorStateType()
    data object LimitRequest : ErrorStateType()
    class UnknownError(val throwable: Throwable) : ErrorStateType()
}