package com.me.daggerhilt.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorScreen(
    message: String,
    resend: Boolean = false,
    reSend: () -> Unit = {}
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = listOf(
                "(≥o≤)",
                "(o^^)o",
                "(>_<)",
                "(='X'=)",
                "(^_^)b",
                "(·.·)",
                "(˚Δ˚)b",
                "(;-;)",
                "(^-^*)",
            ).random(),
            fontSize = 82.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "¡Ups! Algo salió mal",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, start = 32.dp, end = 32.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (resend)
            TextButton(
                onClick = reSend,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Resend",
                    modifier = Modifier.padding(horizontal = 16.dp))
            }
    }
}


@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    DaggerHiltTheme(darkTheme = true) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            innerPadding
            ErrorScreen(
                "Lorem ipsum dolor sit amet consectetur adipiscing elit ",
                true
            )
        }
    }
}