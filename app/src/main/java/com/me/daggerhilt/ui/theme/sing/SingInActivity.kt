package com.me.daggerhilt.ui.theme.sing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.me.daggerhilt.ui.theme.DaggerHiltTheme
import com.me.daggerhilt.ui.theme.main.MainActivity
import com.me.daggerhilt.ui.theme.sing.view.LoginView
import com.me.daggerhilt.ui.theme.sing.view.RegisterView
import com.me.domain.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SingInActivity : ComponentActivity() {
    private lateinit var viewModel: SingInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SingInViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            val userRegisteredState by viewModel.userRegisteredState.collectAsStateWithLifecycle(
                initialValue = UiState.Loading()
            )

            DaggerHiltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPaging ->
                    when (val state = userRegisteredState) {
                        is UiState.Success -> {
                            if (state.data) LoginView(
                                onLogin = onLogin(),
                                modifierColumn = Modifier.padding(innerPaging),
                            )
                            else RegisterView(
                                onSingIn = onRegister(),
                                modifierColumn = Modifier.padding(innerPaging),
                            )
                        }

                        is UiState.Loading -> {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPaging),
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is UiState.Error -> {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPaging),
                            ) {
                                Text(text = state.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onRegister(): (user: String, password: String) -> Unit = { user, password ->
        lifecycleScope.launch {
            viewModel.registerUser(user, password)
            startActivity(Intent(this@SingInActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun onLogin(): (user: String, password: String) -> Unit = { user, password ->
        lifecycleScope.launch {
            if (viewModel.login(user, password)) {
                startActivity(Intent(this@SingInActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@SingInActivity, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
