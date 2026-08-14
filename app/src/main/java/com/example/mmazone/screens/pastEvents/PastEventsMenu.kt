package com.example.mmazone.screens.pastEvents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mmazone.screens.MMAZoneHeader
import com.example.mmazone.screens.MMAZoneTemplate

@Composable
fun PastEventsMenu(
    onBackClick: () -> Unit = {},
    onEventClick: (String) -> Unit = {},
    viewModel: PastEventsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

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
            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text("PREVIOUS EVENTS", modifier = Modifier.fillMaxWidth(), color = Color.Red, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp, textAlign = TextAlign.Center)
            }
            when (val state = uiState) {
                is PastEventsUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.Red)
                        }
                    }
                }
                is PastEventsUiState.Error -> {
                    item {
                        Text(state.message, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(32.dp))
                    }
                }
                is PastEventsUiState.Success -> {
                    items(state.events) { event ->
                        PastEventCard(
                            title = event.title,
                            date = event.date,
                            location = event.location,
                            onClick = { onEventClick(event.id) }
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}

@Composable
fun PastEventCard(title: String, date: String, location: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.DarkGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(date, color = Color.Gray, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(location, color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}