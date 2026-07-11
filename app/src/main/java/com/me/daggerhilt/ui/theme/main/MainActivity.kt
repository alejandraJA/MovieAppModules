package com.me.daggerhilt.ui.theme.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.me.daggerhilt.ui.theme.DaggerHiltTheme
import com.me.daggerhilt.ui.theme.main.movies.MoviesView
import com.me.domain.Movie
import com.me.domain.MovieUiState
import com.me.domain.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            val moviesState by viewModel.movies.collectAsStateWithLifecycle(
                UiState.Loading()
            )
            DaggerHiltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (moviesState) {
                        is UiState.Success<MovieUiState> -> {
                            val moviesUiState = moviesState as UiState.Success<MovieUiState>
                            MoviesView(
                                list = moviesUiState.data.movies,
                                page = moviesUiState.data.currentPage,
                                totalPages = moviesUiState.data.totalPages,
                                modifier = Modifier.padding(innerPadding),
                                onViewMore = onViewMore()
                            )
                        }

                        is UiState.Error -> (moviesState as UiState.Error<MovieUiState>).message

                        is UiState.Loading -> {

                        }
                    }
                }
            }
        }
    }

    fun onViewMore(): (Movie) -> Unit = { movie ->

    }
}