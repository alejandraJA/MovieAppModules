package com.me.daggerhilt.ui.sing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.me.daggerhilt.ui.theme.DaggerHiltTheme
import com.me.daggerhilt.ui.main.MainActivity
import com.me.daggerhilt.ui.sing.view.LoginView
import com.me.daggerhilt.ui.sing.view.RegisterView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingInActivity : ComponentActivity() {
    private lateinit var viewModel: SingInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SingInViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            DaggerHiltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPaging ->
                    if (viewModel.isUserRegistered) LoginView(
                        onLogin = onLogin(),
                        modifier = Modifier.padding(innerPaging),
                    )
                    else RegisterView(
                        onSingIn = onRegister(),
                        modifier = Modifier.padding(innerPaging),
                    )
                }
            }
        }
    }

    private fun onRegister(): (user: String, password: String) -> Unit = { user, password ->
        viewModel.registerUser(user, password)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onLogin(): (user: String, password: String) -> Unit = { user, password ->
        if (viewModel.login(user, password)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }
}


