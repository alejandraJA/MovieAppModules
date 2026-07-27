package com.me.daggerhilt.ui.main.movies

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.daggerhilt.ui.theme.DaggerHiltTheme
import com.me.domain.Movie

@Composable
fun MoviesView(
    list: List<Movie>,
    modifier: Modifier = Modifier,
    page: Int = 1,
    totalPages: Int = 1,
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
            },
            modifier = Modifier.weight(1f)
        )
        Row(
            modifier = modifierGeneral
                .fillMaxWidth(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "<-")
            }
            Text(
                text = "Page $page of $totalPages",
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
            TextButton(
                onClick = { },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "->")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesViewPreview() {
    DaggerHiltTheme(darkTheme = true) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                listOf(movie, movie, movie),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}