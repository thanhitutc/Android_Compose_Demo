package com.thanhitutc.moviedbcompose.ui.popular

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.thanhitutc.moviedbcompose.ui.UiState
import com.thanhitutc.moviedbcompose.ui.popular.composables.MovieItem
import com.thanhitutc.wallpaperpro.data.model.Movie
import timber.log.Timber

@Composable
fun PopularScreen(
    viewModel: PopularViewModel
) {
    val scrollState = rememberScrollState()
    val endReached by remember {
        derivedStateOf {
            scrollState.value == scrollState.maxValue
        }
    }

    LaunchedEffect(endReached) {
        viewModel.loadMore()
    }

    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is UiState.Loading -> {
            // show loading
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        is UiState.Success<*> -> {
            // show data
            val movies = (uiState.value as UiState.Success<List<Movie>>).result

            LazyVerticalGridLoadMore(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 16.dp
                ),
                items = movies.toMutableList(),
                content = { _, movie ->
                    MovieItem(movie = movie)


                }, loadMore = {
                    viewModel.loadMore()

                }
            )
        }

        is UiState.Error -> {
            // show error
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Text(text = "${((uiState.value as UiState.Error).error.toString())}")
            }
        }

        else -> {
            // idle
            LaunchedEffect(true) {
                viewModel.getPopularMovies()
            }
        }
    }
}

@Composable
fun <T> LazyVerticalGridLoadMore(
    modifier: Modifier,
    columns: GridCells,
    items: MutableList<T>,
    contentPadding: PaddingValues,
    loadMore: () -> Unit,
    content: @Composable (Int, T) -> Unit
) {
    val listState = rememberLazyGridState()
    val firstVisibleIndex = remember { mutableIntStateOf(listState.firstVisibleItemIndex) }
    LazyVerticalGrid(
        state = listState,
        columns = columns,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        itemsIndexed(items) { index, item ->
            content(index, item)
        }

    }
    if (listState.shouldLoadMore(firstVisibleIndex)) {
        Timber.e("thanh_ load more detect")
        loadMore()
    }
}

fun LazyGridState.shouldLoadMore(rememberedIndex: MutableState<Int>): Boolean {
    val firstVisibleIndex = this.firstVisibleItemIndex
    if (rememberedIndex.value != firstVisibleIndex) {
        rememberedIndex.value = firstVisibleIndex
        return layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
    }
    return false
}

