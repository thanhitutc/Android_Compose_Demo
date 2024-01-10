package com.thanhitutc.moviedbcompose.ui.popular

import androidx.lifecycle.viewModelScope
import com.thanhitutc.moviedbcompose.data.repository.MovieRepository
import com.thanhitutc.moviedbcompose.ui.base.BaseViewModel
import com.thanhitutc.moviedbcompose.ui.UiState
import com.thanhitutc.wallpaperpro.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseViewModel() {
    private val movies = mutableListOf<Movie>()

    private var page = 1
    fun getPopularMovies() {
        try {
            onLoading()
            viewModelScope.launch {
                val result = movieRepository.getPopularMovies(page = page)
                Timber.e("thanh_ getPopularMovies: ${result.results.size}")
                movies.addAll(result.results)
                onSuccess<List<Movie>>(movies)
            }
        } catch (e: Exception) {
            Timber.e("thanh_ error: ${e.message}")
            onError(e)
        }

    }

    fun loadMore() {
        if (uiState.value is UiState.Loading) return
        page++
        Timber.d("thanh_ loadMore: $page")
        getPopularMovies()
    }
}