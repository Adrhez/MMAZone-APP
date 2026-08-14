package com.example.mmazone.screens.aboutUs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mmazone.screens.MMAZoneHeader
import com.example.mmazone.screens.MMAZoneTemplate

@Composable
fun AboutUs(onBackClick: () -> Unit = {}) {
    MMAZoneTemplate {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // --- 1. CABECERA ---
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    MMAZoneHeader()
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // --- 2. SOBRE EL PROYECTO ---
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = "THE PROJECT",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "MMAZone is an independent, non-profit personal portfolio application. Built from the ground up to demonstrate modern Android development architectures, UI/UX design capabilities, and reactive programming using Kotlin & Jetpack Compose.",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(28.dp)) }

            // --- 3. TARJETA DEL DESARROLLADOR ---
            item {
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "DEVELOPED BY",
                                color = Color.Red,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            // ⚠️ CAMBIA ESTO POR TU NOMBRE REAL O TU APODO DE GITHUB
                            Text(
                                text = "Adrián Hernández",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Android / Mobile Software Engineer",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // --- 4. ESCUDO LEGAL (FAIR USE DISCLAIMER) ---
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = "COPYRIGHT & FAIR USE",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)), // Gris oscuro de fondo
                        border = BorderStroke(1.dp, Color.DarkGray),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "FAIR USE NOTICE (17 U.S.C. § 107)",
                                color = Color(0xFFFFD700),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "This application is an educational resource created strictly for non-commercial evaluation. All promotional fight posters, athlete likenesses, event names, logos, and registered trademarks (including UFC® and Zuffa, LLC) are the exclusive property of their respective copyright holders.\n\n" +
                                        "The display of low-resolution promotional material within this software qualifies under the 'Fair Use' doctrine of the United States Copyright Act of 1976 for purposes of sports commentary, informational public archiving, and non-profit educational display. MMAZone is not affiliated with, sponsored by, or endorsed by the Ultimate Fighting Championship.",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutUsPreview() {
    AboutUs()
}