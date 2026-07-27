package com.me.daggerhilt.ui.sing.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.daggerhilt.R
import com.me.daggerhilt.ui.theme.DaggerHiltTheme

@Composable
fun RegisterView(
    onSingIn: (user: String, password: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 8.dp),
) {
    val user = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val  commonModifier: Modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 8.dp)
    Column(modifier = modifier.padding()) {
        Text(
            text = stringResource(R.string.register_now),
            modifier = commonModifier
                .padding(top = 16.dp)
        )
        TextField(
            value = user.value,
            onValueChange = {
                user.value = it
            },
            label = { Text(text = stringResource(R.string.user)) },
            modifier = commonModifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
        )
        TextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            label = { Text(text = stringResource(R.string.password)) },
            modifier = commonModifier
                .fillMaxWidth(),
        )
        Button(
            onClick = {
                onSingIn(user.value, password.value)
            },
            modifier = commonModifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.register))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    DaggerHiltTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RegisterView(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}