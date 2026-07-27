package com.me.daggerhilt.ui.theme.main.movie

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.me.daggerhilt.ui.main.movie.MovieDetailScreen
import com.me.daggerhilt.ui.main.movie.MovieDetailTestTags
import com.me.domain.Movie
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MovieDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun movieDetailScreen_displaysMovieInformation() {
        val fakeMovie = fakeMovie()
        composeRule.setContent {
            MaterialTheme {
                MovieDetailScreen(
                    movie = fakeMovie
                )
            }
        }

        composeRule
            .onNodeWithTag(MovieDetailTestTags.SCREEN)
            .assertIsDisplayed()

        composeRule
            .onNodeWithTag(MovieDetailTestTags.MAIN_TITLE)
            .assertTextContains("El club de la pelea")

        composeRule
            .onNodeWithTag(MovieDetailTestTags.ORIGINAL_TITLE)
            .assertTextContains("Fight Club")

        composeRule
            .onNodeWithTag(MovieDetailTestTags.OVERVIEW)
            .assertTextContains("Un trabajador de oficina insomne y un vendedor de jabón forman un club clandestino que se convierte en algo mucho más peligroso.")

        composeRule
            .onNodeWithTag(MovieDetailTestTags.RATING)
            .assertTextContains("★ ${"%.1f".format(fakeMovie.voteAverage)} / 10  ·  ${fakeMovie.voteCount} votos")

        composeRule
            .onNodeWithText("Estreno: 1999-10-15")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Idioma original: EN")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Favorita")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Apta")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Sin video")
            .assertIsDisplayed()
    }

    @Test
    fun movieDetailScreen_whenOverviewIsBlank_displaysFallbackText() {
        composeRule.setContent {
            MaterialTheme {
                MovieDetailScreen(
                    movie = fakeMovie().copy(
                        overview = ""
                    )
                )
            }
        }

        composeRule
            .onNodeWithTag(MovieDetailTestTags.OVERVIEW)
            .assertTextContains("No hay sinopsis disponible.")
    }

    @Test
    fun movieDetailScreen_whenMovieIsAdult_displaysAdultChip() {
        composeRule.setContent {
            MaterialTheme {
                MovieDetailScreen(
                    movie = fakeMovie().copy(
                        adult = true
                    )
                )
            }
        }

        composeRule
            .onNodeWithText("+18")
            .assertIsDisplayed()
    }

    @Test
    fun movieDetailScreen_whenMovieHasVideo_displaysVideoChip() {
        composeRule.setContent {
            MaterialTheme {
                MovieDetailScreen(
                    movie = fakeMovie().copy(
                        video = true
                    )
                )
            }
        }

        composeRule
            .onNodeWithText("Con video")
            .assertIsDisplayed()
    }

    @Test
    fun movieDetailScreen_whenLikeButtonIsClicked_callsOnLikeClick() {
        var clickedMovie: Movie? = null
        val movie = fakeMovie()

        composeRule.setContent {
            MaterialTheme {
                MovieDetailScreen(
                    movie = movie,
                    onLikeClick = {
                        clickedMovie = it
                    }
                )
            }
        }

        composeRule
            .onNodeWithTag(MovieDetailTestTags.LIKE_BUTTON)
            .performClick()

        assertEquals(movie, clickedMovie)
    }

    @Test
    fun movieDetailScreen_whenBackButtonIsClicked_callsOnBackClick() {
        var wasBackClicked = false

        composeRule.setContent {
            MaterialTheme {
                MovieDetailScreen(
                    movie = fakeMovie(),
                    onBackClick = {
                        wasBackClicked = true
                    }
                )
            }
        }

        composeRule
            .onNodeWithTag(MovieDetailTestTags.BACK_BUTTON)
            .performClick()

        assertTrue(wasBackClicked)
    }

    private fun fakeMovie(): Movie {
        return Movie(
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
}