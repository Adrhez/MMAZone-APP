package com.example.mmazone.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mmazone.R
import com.example.mmazone.data.SettingsManager
import com.example.mmazone.screens.dashboard.TitleFont
import com.example.mmazone.screens.dashboard.darkGrey

val darkGrey = Color(0xFF121212)
val TitleFont = FontFamily(
    Font(resId = R.font.ethnocentricregular)
)

@Composable
fun MMAZoneHeader(onProfileClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 40.dp, bottom = 36.dp)
            .padding(horizontal = 1.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "MMAZone",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TitleFont
        )

        Image(
            painter = painterResource(id = R.drawable.usericon),
            contentDescription = "User",
            modifier = Modifier
                .size(45.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .clickable { onProfileClick() },
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun DottedBackground() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val solidHeight = 150f
        val totalHeight = 500f

        val halftoneColor = Color(0xFF8B0000)

        val dotSpacing = 20f
        val maxRadius = 15f

        drawRect(
            color = halftoneColor,
            topLeft = Offset(0f, 0f),
            size = Size(size.width, solidHeight)
        )

        for (y in solidHeight.toInt()..totalHeight.toInt() step dotSpacing.toInt()) {

            val progress = (1f - ((y - solidHeight) / (totalHeight - solidHeight))).coerceIn(0f, 1f)

            if (progress > 0) {
                for (x in 0..(size.width.toInt() + dotSpacing.toInt()) step dotSpacing.toInt()) {
                    drawCircle(
                        color = halftoneColor,
                        radius = maxRadius * progress,
                        center = Offset(x.toFloat(), y.toFloat())
                    )
                }
            }
        }
    }
}

@Composable
fun MMAZoneTemplate(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }

    val oledMode by settingsManager.oledModeFlow.collectAsState(initial = false)

    val backgroundColor = if (oledMode) Color.Black else Color(0xFF121212)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        DottedBackground()
        content()
    }
}