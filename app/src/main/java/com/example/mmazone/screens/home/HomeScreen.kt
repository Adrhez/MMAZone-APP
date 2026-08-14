package com.example.mmazone.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mmazone.R
import com.example.mmazone.auth.GoogleAuthManager
import kotlinx.coroutines.launch

val TitleFont = FontFamily(
    Font(resId = R.font.ethnocentricregular)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onLoginSuccess: () -> Unit = {}, onRegisterClick: () -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthManager = remember { GoogleAuthManager(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(Modifier.fillMaxSize().padding(paddingValues)) {
            Box(Modifier.weight(1.5f)){
                Image(
                    painter = painterResource(id = R.drawable.homeimage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                )
                Text(
                    text = "MMAZone",
                    Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontSize = 40.sp,
                    fontFamily = TitleFont
                )
            }
            Box(Modifier.weight(1f)){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null
                        },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        singleLine = true
                    )
                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    TextButton(
                        content = { Text(text = "Forgot my password") },
                        onClick = {}
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Color(0xFFFF6B6B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Button(
                        enabled = !isLoading,
                        onClick = {
                            errorMessage = null
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Please enter email and password."
                                return@Button
                            }

                            isLoading = true
                            coroutineScope.launch {
                                val result = googleAuthManager.signInWithEmail(email.trim(), password)
                                isLoading = false

                                when (result) {
                                    GoogleAuthManager.AuthResult.Success -> {
                                        onLoginSuccess()
                                    }
                                    is GoogleAuthManager.AuthResult.Error -> {
                                        errorMessage = result.message
                                    }
                                    else -> {}
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Login")
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                val success = googleAuthManager.signInWithGoogle()
                                if (success) {
                                    onLoginSuccess()
                                } else {
                                    errorMessage = "Google sign-in failed or was canceled."
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
                    ) {
                        Text("Login with Google")
                    }

                    TextButton(
                        content = { Text(text = "Register") },
                        onClick = { onRegisterClick() }
                    )
                }
            }
            StandardFooter()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Composable
fun StandardFooter() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "MMAZone 2026")
    }
}