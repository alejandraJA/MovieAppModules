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
            MoviesView(
                listOf(
                    Movie(
                        1,
                        "Movie 1",
                        "Movie 1",
                        overview = "Overview 1",
                        posterPath = "/alf3JOPP7EYP0iO24gwe5YfRnqo.jpg",
                        like = true
                    ),
                    Movie(
                        2,
                        "Movie 2",
                        "Movie 2",
                        overview = "Overview 2",
                        posterPath = "/alf3JOPP7EYP0iO24gwe5YfRnqo.jpg",
                        like = true
                    ),
                    Movie(
                        3,
                        "Movie 3",
                        "Movie 3",
                        overview = "Overview 3",
                        posterPath = "/alf3JOPP7EYP0iO24gwe5YfRnqo.jpg",
                        like = true
                    ),
                    Movie(
                        4,
                        "Movie 4",
                        "Movie 4",
                        overview = "Overview 4",
                        posterPath = "/alf3JOPP7EYP0iO24gwe5YfRnqo.jpg",
                        like = true
                    ),
                ), modifier = Modifier.padding(innerPadding)
            )
        }
    }
}