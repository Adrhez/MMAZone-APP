package com.example.mmazone.screens.pastEvents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mmazone.R
import com.example.mmazone.data.SettingsManager
import com.example.mmazone.screens.MMAZoneHeader
import com.example.mmazone.screens.MMAZoneTemplate

data class CompletedFight(
    val winner: String,
    val loser: String,
    val weightClass: String,
    val method: String,
    val roundTime: String
)

@Composable
fun PastEventDetails(
    eventId: String,
    onBackClick: () -> Unit = {},
    viewModel: PastEventsViewModel
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val spoilerMode by settingsManager.spoilerModeFlow.collectAsState(initial = false)
    val event = viewModel.getEventById(eventId)

    MMAZoneTemplate {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    MMAZoneHeader()
                    IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(58.dp)) }

            if (event != null) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                        AsyncImage(
                            model = event.posterUrl.ifEmpty { R.drawable.freedom250 },
                            contentDescription = "Event Poster",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent), endY = 200f)))
                        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.95f)), startY = 300f)))
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Arena", color = Color.White, fontWeight = FontWeight.Bold)
                            Text(event.location, color = Color.Gray, fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Event Completed", color = Color.Green, fontWeight = FontWeight.Bold)
                            Text(event.date, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }

                item {
                    Text("OFFICIAL RESULTS", color = Color.Red, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 16.dp))
                }

                if (event.fights.isEmpty()) {
                    item { Text("No results availible.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
                } else {
                    items(event.fights) { fight ->
                        CompletedFightRow(fight = fight, isSpoilerProtected = spoilerMode)
                    }
                }
            } else {
                item { Text("No se pudo cargar el evento.", color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(32.dp)) }
            }
            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}

@Composable
fun CompletedFightRow(fight: CompletedFight, isSpoilerProtected: Boolean) {
    var revealed by remember { mutableStateOf(false) }
    val hideResult = isSpoilerProtected && !revealed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable(enabled = hideResult) { revealed = true },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        border = BorderStroke(1.dp, if (hideResult) Color.DarkGray else Color.Red.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = fight.weightClass, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (hideResult) {
                Box(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF222222), RoundedCornerShape(8.dp)).padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚠️ SPOILER PROTECTED", color = Color(0xFFFFD700), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Tap to reveal official winner & method", color = Color.LightGray, fontSize = 13.sp)
                    }
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(0.7f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👑 ", fontSize = 16.sp)
                            Text(fight.winner, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        }
                        Text("def. ${fight.loser}", color = Color.LightGray, fontSize = 14.sp, modifier = Modifier.padding(start = 24.dp))
                    }
                    Box(modifier = Modifier.background(Color(0xFF8B0000), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text("WIN", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Method: ${fight.method}", color = Color.Gray, fontSize = 12.sp)
                    Text(fight.roundTime, color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PastEventDetailsPreview() {
    PastEventDetails(
        "ufc324",
        onBackClick = TODO(),
        viewModel = TODO()
    )
}