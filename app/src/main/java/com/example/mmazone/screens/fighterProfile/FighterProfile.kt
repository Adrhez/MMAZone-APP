package com.example.mmazone.screens.fighterProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mmazone.R
import com.example.mmazone.data.SettingsManager
import com.example.mmazone.screens.MMAZoneHeader
import com.example.mmazone.screens.MMAZoneTemplate

@Composable
fun FighterProfile(
    fighterId: String,
    onBackClick: () -> Unit,
    viewModel: FighterViewModel = viewModel()
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val spoilerMode by settingsManager.spoilerModeFlow.collectAsState(initial = false)

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(fighterId) {
        viewModel.loadFighter(fighterId)
    }

    MMAZoneTemplate {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            when (val state = uiState) {
                is FighterUiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Cargando estadísticas en vivo...", color = Color.Gray, fontSize = 14.sp)
                    }
                }

                is FighterUiState.Error -> {
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚠️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(state.message, color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadFighter(fighterId) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000))
                        ) {
                            Text("REINTENTAR")
                        }
                    }
                }

                is FighterUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                MMAZoneHeader()
                                IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)) {
                                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                                }
                            }
                        }
                        item {
                            Row(modifier = Modifier.fillParentMaxHeight(0.5f).fillMaxWidth()) {

                                AsyncImage(
                                    model = state.imageUrl,
                                    contentDescription = state.name,
                                    modifier = Modifier.fillMaxSize().weight(1f),
                                    contentScale = ContentScale.Fit,
                                    placeholder = painterResource(id = R.drawable.topuria),
                                    error = painterResource(id = R.drawable.topuria)
                                )

                                Column(
                                    modifier = Modifier.weight(1f).fillMaxHeight().padding(start = 16.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = state.name,
                                        color = Color.White,
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 6.dp)
                                    )
                                    if (state.nickname.isNotBlank()) {
                                        Text(
                                            text = state.nickname,
                                            color = Color.Red,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 1.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    Text(
                                        text = state.division,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))

                                    Text(
                                        text = state.record,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "Edad: ${state.age}",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        letterSpacing = 2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "País: ${state.country}",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 2.dp, color = Color.Red.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "HISTORIAL DE COMBATES",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        items(state.pastFights) { fight ->
                            PastFightItem(
                                result = fight.first,
                                opponent = fight.second,
                                method = fight.third,
                                isSpoilerProtected = spoilerMode
                            )
                        }

                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }
        }
    }
}


@Composable
fun PastFightItem(result: String, opponent: String, method: String, isSpoilerProtected: Boolean = false) {
    var revealed by remember { mutableStateOf(false) }
    val hideResult = isSpoilerProtected && !revealed

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable(enabled = hideResult) { revealed = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hideResult) {
            Box(
                modifier = Modifier
                    .background(Color.DarkGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("SPOILER", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }
        } else {
            Text(
                text = result,
                color = if (result == "W") Color.Green else Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        Column(modifier = Modifier.weight(1f).padding(start = if (hideResult) 12.dp else 0.dp)) {
            Text(text = opponent, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = method, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FighterProfilePreview() {
    FighterProfile(fighterId = "1", onBackClick = {})
}