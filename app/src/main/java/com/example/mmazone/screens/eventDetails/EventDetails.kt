package com.example.mmazone.screens.eventDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mmazone.screens.MMAZoneHeader
import com.example.mmazone.screens.MMAZoneTemplate

@Composable
fun EventDetails(
    onBackClick: () -> Unit = {},
    viewModel: NextEventViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MMAZoneTemplate {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (val state = uiState) {
                is EventUiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading structured fight card...", color = Color.Gray, fontSize = 14.sp)
                    }
                }

                is EventUiState.Error -> {
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚠️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(state.message, color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.fetchNextEvent() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000))
                        ) {
                            Text("TRY AGAIN")
                        }
                    }
                }

                is EventUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {

                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                MMAZoneHeader()
                                IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)) {
                                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(58.dp)) }

                        // HERO POSTER
                        item {
                            EventHeaderHeroRemote(posterUrl = state.posterUrl)
                        }

                        // BARRA DE ARENA Y FECHA
                        item {
                            EventInfoSectionRemote(date = state.date, arena = state.arena, country = state.country)
                        }

                        // ==========================================
                        // 1. SECCIÓN: MAIN EVENT
                        // ==========================================
                        if (state.mainEvent != null) {
                            item { EventSectionTitle("MAIN EVENT") }
                            item {
                                MainEventCardItem(fightName = state.mainEvent.first, weightClass = state.mainEvent.second)
                            }
                        }

                        // ==========================================
                        // 2. SECCIÓN: MAIN CARD
                        // ==========================================
                        if (state.mainCard.isNotEmpty()) {
                            item { EventSectionTitle("MAIN CARD") }
                            items(state.mainCard) { fight ->
                                FightCardItemRemote(fightName = fight.first, weightClass = fight.second)
                            }
                        }

                        // ==========================================
                        // 3. SECCIÓN: PRELIMS
                        // ==========================================
                        if (state.prelims.isNotEmpty()) {
                            item { EventSectionTitle("PRELIMINARY CARD") }
                            items(state.prelims) { fight ->
                                FightCardItemRemote(fightName = fight.first, weightClass = fight.second)
                            }
                        }

                        item { Spacer(modifier = Modifier.height(48.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun EventSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Red,
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp, bottom = 12.dp)
    )
}

@Composable
fun MainEventCardItem(fightName: String, weightClass: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f)), // Borde dorado
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "👑 " + weightClass.uppercase(), color = Color(0xFFFFD700), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = fightName, color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun EventHeaderHeroRemote(posterUrl: String) {
    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        AsyncImage(
            model = posterUrl,
            contentDescription = "Live Event Poster",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent), endY = 200f)))
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.95f)), startY = 300f)))
    }
}

@Composable
fun EventInfoSectionRemote(date: String, arena: String, country: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.LocationOn, "Location", tint = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(arena, color = Color.White, fontWeight = FontWeight.Bold)
            Text(country, color = Color.Gray, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.DateRange, "Time", tint = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Confirmed Date", color = Color.White, fontWeight = FontWeight.Bold)
            Text(date, color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FightCardItemRemote(fightName: String, weightClass: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(fightName, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(2.dp))
        Text(weightClass, color = Color.Gray, fontSize = 13.sp)
    }
}