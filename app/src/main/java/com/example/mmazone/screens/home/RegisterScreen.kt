package com.example.mmazone.screens.home

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit = {}, onLoginClick: () -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthManager = remember { GoogleAuthManager(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

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

                    if (successMessage != null) {
                        Text(
                            text = successMessage!!,
                            color = Color(0xFF4ECDC4),
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
                            successMessage = null

                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "All fields are required."
                                return@Button
                            }
                            if (!isEmailValid(email)) {
                                errorMessage = "Please enter a valid email address."
                                return@Button
                            }
                            if (password.length < 6) {
                                errorMessage = "Password must be at least 6 characters."
                                return@Button
                            }

                            isLoading = true
                            coroutineScope.launch {
                                val result = googleAuthManager.signUpWithEmail(email.trim(), password)
                                isLoading = false

                                when (result) {
                                    GoogleAuthManager.AuthResult.VerificationSent -> {
                                        successMessage = "Account created! Check your inbox for the verification link."
                                        email = ""
                                        password = ""
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
                            Text("Register")
                        }
                    }
                    TextButton(
                        content = { Text(text = "Log in") },
                        onClick = { onLoginClick() }
                    )
                }
            }
            StandardFooter()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}