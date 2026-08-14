package com.example.mmazone.screens.settings

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mmazone.data.SettingsManager
import com.example.mmazone.screens.MMAZoneHeader
import com.example.mmazone.screens.MMAZoneTemplate
import kotlinx.coroutines.launch

@Composable
fun Settings(onBackClick: () -> Unit){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingsManager = remember { SettingsManager(context) }
    val spoilerMode by settingsManager.spoilerModeFlow.collectAsState(initial = false)
    val oledMode by settingsManager.oledModeFlow.collectAsState(initial = false)

    MMAZoneTemplate {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    MMAZoneHeader()
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Text(
                    text = "PREFERENCES",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }
            item {
                SettingRow(
                    title = "Spoiler Protection",
                    subtitle = "Hide match results for unwatched past events",
                    checked = spoilerMode,
                    onCheckedChange = { isChecked ->
                        scope.launch { settingsManager.setSpoilerMode(isChecked) }
                    }
                )
            }

            item {
                SettingRow(
                    title = "Pure OLED Black",
                    subtitle = "Switch background to true #000000 for AMOLED battery saving",
                    checked = oledMode,
                    onCheckedChange = { isChecked ->
                        scope.launch { settingsManager.setOledMode(isChecked) }
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp, horizontal = 24.dp), color = Color.DarkGray)
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Text("MMAZone v1.0.0 (Build 2026)", color = Color.Gray, fontSize = 12.sp)
                    Text("Database: Firebase Auth + Firestore ready", color = Color.DarkGray, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun SettingRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(0.8f)) {
            Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF8B0000),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}