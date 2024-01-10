package com.thanhitutc.moviedbcompose.ui.popular.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.thanhitutc.moviedbcompose.R
import com.thanhitutc.moviedbcompose.ui.theme.MovieDBComposeTheme
import com.thanhitutc.wallpaperpro.data.model.Movie

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
) {
    Card(
        modifier = modifier
            .background(color = Color.Black)
            .padding(4.dp)
            .fillMaxWidth()
            .height(300.dp),
    ) {
        ConstraintLayout(
            modifier = modifier.fillMaxSize()
        ) {
            val (thumbnail, rate, title, releaseDate) = createRefs()
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500/${movie.posterPath}",
                contentDescription = "thumbnail",
                modifier = modifier
                    .constrainAs(thumbnail) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(title.top)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    },
                contentScale = ContentScale.Crop
            )
            MovieRate(
                modifier = modifier
                    .constrainAs(rate) {
                        top.linkTo(thumbnail.top, margin = 12.dp)
                        end.linkTo(thumbnail.end, margin = 12.dp)
                    },
                rate = movie.voteAverage ?: 0.0
            )
            Text(
                text = movie.title ?: "",
                modifier = modifier.constrainAs(title) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(releaseDate.top)
                    width = Dimension.fillToConstraints
                },
                style = MaterialTheme.typography.titleSmall.copy(color = Color.White),
                textAlign = TextAlign.Start,
                maxLines = 1
            )
            Text(
                text = movie.releaseDate ?: "",
                modifier = modifier.constrainAs(releaseDate) {
                    start.linkTo(title.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                textAlign = TextAlign.Start
            )

        }
    }
}

@Composable
fun MovieRate(modifier: Modifier = Modifier, rate: Double) {
    Box(modifier = modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
        Text(
            text = String.format("%.1f", rate),
            style = TextStyle(fontSize = 10.sp, color = Color.White),
            modifier = modifier
                .drawBehind {
                    drawCircle(
                        color = Color.Red,
                        radius = this.size.maxDimension
                    )
                }
                .padding(2.dp)
        )
    }
}

//@Preview
//@Composable
//fun MovieRatePreview() {
//    MovieDBComposeTheme {
//        MovieRate(rate = "8.5")
//    }
//}

@Preview
@Composable
fun MovieItemPreview() {
    MovieDBComposeTheme {
        MovieItem(
            movie = Movie(
                id = "1",
                title = "Hello World",
                voteAverage = 8.5,
                releaseDate = "10/20/2024"
            )
        )
    }
}