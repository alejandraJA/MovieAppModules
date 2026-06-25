package com.me.daggerhilt.ui.theme.main.movies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.daggerhilt.ui.theme.DaggerHiltTheme
import com.me.domain.Movie

@Composable
fun MoviesView(
    list: List<Movie>,
    modifier: Modifier = Modifier,
    onViewMore: (movie: Movie) -> Unit = {}
) {
    val modifierGeneral = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 8.dp)
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            content = {
                items(list.size) { index ->
                    MovieView(
                        movie = list[index],
                        modifier = modifierGeneral,
                        onViewMore = onViewMore
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesViewPreview() {
    DaggerHiltTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            innerPadding
            val movie = Movie(
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
            MoviesView(
                listOf(movie, movie, movie, movie, movie, movie, movie, movie, movie),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}