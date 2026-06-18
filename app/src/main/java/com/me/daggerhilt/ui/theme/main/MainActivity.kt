package com.me.daggerhilt.ui.theme.main

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import com.me.daggerhilt.ui.theme.DaggerHiltTheme
import com.me.daggerhilt.ui.theme.main.movies.MoviesView
import com.me.domain.Movie
import com.me.domain.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            val moviesState by viewModel.movies.collectAsStateWithLifecycle(
                UiState.Loading(listOf())
            )
            DaggerHiltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (moviesState is UiState.Success) {
                        val list = (moviesState as UiState.Success<List<Movie>>).data
                        MoviesView(
                            list = list,
                            modifier = Modifier.padding(innerPadding),
                            onViewMore = onViewMore()
                        )
                    }
                }
            }
        }
    }

    fun onViewMore(): (Movie) -> Unit = { movie ->

    }
}