package com.me.daggerhilt.ui.main.movies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.me.daggerhilt.R
import com.me.daggerhilt.ui.theme.DaggerHiltTheme
import com.me.domain.Constants
import com.me.domain.Movie

@Composable
fun MovieView(
    movie: Movie,
    modifier: Modifier = Modifier,
    onViewMore: (movie: Movie) -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
    ) {
        Row {
            if (movie.posterPath.isNotEmpty())
                AsyncImage(
                    model = Constants.BASE_URL_IMAGES + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .height(150.dp)
                )
            Column(Modifier.fillMaxWidth(1f)) {
                Text(
                    text = movie.title,
                    modifier = modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = movie.originalTitle,
                    modifier = modifier,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = movie.overview,
                    modifier = modifier,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = { onViewMore(movie) },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp, bottom = 8.dp),

                    ) {
                    Text(stringResource(R.string.view_more))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieViewPreview() {
    DaggerHiltTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MovieView(
                Movie(
                    id = 1,
                    like = false,
                    adult = false,
                    backdropPath = "",
                    genreIds = listOf(),
                    originalLanguage = "en",
                    originalTitle = "Lorem ipsum dolor sit amet",
                    overview = "Lorem ipsum dolor sit amet consectetur adipiscing elit ",
                    popularity = 0.0,
                    posterPath = "",
                    releaseDate = "",
                    title = "Lorem ipsum dolor sit amet",
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            )
        }
    }
}