package com.me.daggerhilt.ui.main.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.me.domain.Movie
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Movie Detail Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MovieDetailScreenPreview() {
    MaterialTheme {
        MovieDetailScreen(
            movie = MoviePreviewData.movie
        )
    }
}

@Preview(
    name = "Movie Detail Screen - Sin sinopsis",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MovieDetailScreenWithoutOverviewPreview() {
    MaterialTheme {
        MovieDetailScreen(
            movie = MoviePreviewData.movie.copy(
                overview = "",
                like = false,
                adult = true,
                video = true
            )
        )
    }
}

private object MoviePreviewData {
    val movie = Movie(
        id = 550,
        like = true,
        adult = false,
        backdropPath = "/hZkgoQYus5vegHoetLkCJzb17zJ.jpg",
        genreIds = listOf(18, 53, 35),
        originalLanguage = "en",
        originalTitle = "Fight Club",
        overview = "Un trabajador de oficina insomne y un vendedor de jabón forman un club clandestino que se convierte en algo mucho más peligroso.",
        popularity = 89.42,
        posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
        releaseDate = "1999-10-15",
        title = "El club de la pelea",
        video = false,
        voteAverage = 8.4,
        voteCount = 28500
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: Movie,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLikeClick: (Movie) -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
            .testTag(MovieDetailTestTags.SCREEN),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = movie.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag(MovieDetailTestTags.BACK_BUTTON)
                    ) {
                        Text(text = "‹", style = MaterialTheme.typography.headlineMedium)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onLikeClick(movie) },
                        modifier = Modifier.testTag(MovieDetailTestTags.LIKE_BUTTON)
                    ) {
                        Text(
                            text = if (movie.like) "♥" else "♡",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            MovieBackdrop(movie = movie)

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MovieMainInfo(movie = movie)

                MovieFlags(movie = movie)

                MovieOverview(overview = movie.overview)

                MovieStats(movie = movie)

                MovieGenres(genreIds = movie.genreIds)
            }
        }
    }
}

@Composable
private fun MovieBackdrop(
    movie: Movie
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        AsyncImage(
            model = movie.backdropPath.toTmdbImageUrl(size = "w780"),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize().testTag(MovieDetailTestTags.BACKDROP),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.25f)
                )
        )
    }
}


@Composable
private fun MovieMainInfo(
    movie: Movie
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = movie.posterPath.toTmdbImageUrl(size = "w500"),
            contentDescription = movie.title,
            modifier = Modifier
                .width(120.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .testTag(MovieDetailTestTags.POSTER),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = movie.title,
                modifier = Modifier.testTag(MovieDetailTestTags.MAIN_TITLE),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            if (movie.originalTitle != movie.title) {
                Text(
                    text = movie.originalTitle,
                    modifier = Modifier.testTag(MovieDetailTestTags.ORIGINAL_TITLE),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Estreno: ${movie.releaseDate.ifBlank { "Sin fecha" }}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Idioma original: ${movie.originalLanguage.uppercase()}",
                style = MaterialTheme.typography.bodyMedium
            )

            RatingBadge(
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount
            )
        }
    }
}

@Composable
private fun RatingBadge(
    voteAverage: Double,
    voteCount: Int
) {
    Card(
        shape = RoundedCornerShape(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = "★ ${"%.1f".format(voteAverage)} / 10  ·  $voteCount votos",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag(MovieDetailTestTags.RATING),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun MovieFlags(
    movie: Movie
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(
            onClick = {},
            label = {
                Text(if (movie.like) "Favorita" else "No favorita")
            }
        )

        AssistChip(
            onClick = {},
            label = {
                Text(if (movie.adult) "+18" else "Apta")
            }
        )

        AssistChip(
            onClick = {},
            label = {
                Text(if (movie.video) "Con video" else "Sin video")
            }
        )
    }
}

@Composable
private fun MovieOverview(
    overview: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Sinopsis",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = overview.ifBlank { "No hay sinopsis disponible." },
            modifier = Modifier.testTag(MovieDetailTestTags.OVERVIEW),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MovieStats(
    movie: Movie
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Información",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        InfoCard(label = "ID", value = movie.id.toString())
        InfoCard(label = "Popularidad", value = "%.2f".format(movie.popularity))
        InfoCard(label = "Promedio de votos", value = "%.1f".format(movie.voteAverage))
        InfoCard(label = "Total de votos", value = movie.voteCount.toString())
    }
}

@Composable
private fun InfoCard(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MovieGenres(
    genreIds: List<Int>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Géneros",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        if (genreIds.isEmpty()) {
            Text(
                text = "Sin géneros registrados.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genreIds.forEach { genreId ->
                    FilterChip(
                        selected = false,
                        onClick = {},
                        label = {
                            Text(text = genreId.toString())
                        }
                    )
                }
            }
        }
    }
}

private fun String.toTmdbImageUrl(size: String): String? {
    if (isBlank()) return null

    return if (startsWith("http")) {
        this
    } else {
        "https://image.tmdb.org/t/p/$size$this"
    }
}

object MovieDetailTestTags {
    const val SCREEN = "movie_detail_screen"
    const val BACK_BUTTON = "movie_detail_back_button"
    const val LIKE_BUTTON = "movie_detail_like_button"
    const val MAIN_TITLE = "movie_detail_main_title"
    const val ORIGINAL_TITLE = "movie_detail_original_title"
    const val OVERVIEW = "movie_detail_overview"
    const val RATING = "movie_detail_rating"
    const val POSTER = "movie_detail_poster"
    const val BACKDROP = "movie_detail_backdrop"
}