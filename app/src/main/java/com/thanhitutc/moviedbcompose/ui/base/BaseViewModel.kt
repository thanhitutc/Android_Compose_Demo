package com.thanhitutc.moviedbcompose.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thanhitutc.moviedbcompose.ui.ErrorStateType
import com.thanhitutc.moviedbcompose.ui.UiState
import com.thanhitutc.wallpaperpro.data.remote.toBaseException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel : ViewModel() {
    private var _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState>
        get() = _uiState

    // exception handler for coroutine
    private val exceptionHandler by lazy {
        CoroutineExceptionHandler { context, throwable ->
            viewModelScope.launch {
                onError(throwable)
            }
        }
    }
    protected val viewModelScopeExceptionHandler by lazy { viewModelScope + exceptionHandler }

    /**
     * handle throwable when load fail
     */
    protected fun toErrorType(throwable: Throwable): ErrorStateType {
        Timber.e("toErrorType: ${throwable.printStackTrace()}")
        return when (throwable) {
            // case no internet connection
            is UnknownHostException -> {
                ErrorStateType.NoInternetConnection
            }

            is ConnectException -> {
                ErrorStateType.NoInternetConnection
            }
            // case request time out
            is SocketTimeoutException -> {
                ErrorStateType.ConnectTimeout
            }

            else -> {
                // convert throwable to base exception to get error information
                val baseException = throwable.toBaseException()
                when (baseException.httpCode) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        ErrorStateType.UnAuthorized
                    }

                    HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                        ErrorStateType.ServerMaintain
                    }

                    else -> {
                        ErrorStateType.UnknownError(throwable)
                    }
                }
            }
        }
    }

    protected open fun onError(throwable: Throwable) {
        _uiState.value = UiState.Error(toErrorType(throwable))

    }

    protected open fun onLoading() {
        _uiState.value = UiState.Loading
    }

    protected open fun <T> onSuccess(result: T) {
        _uiState.value = UiState.Success(result = result)
    }

    protected open fun onResetState() {
        _uiState.value = UiState.Idle
    }

}