package com.example.mmazone.screens.rankings

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun Rankings(
    onBackClick: () -> Unit = {},
    onFighterClick: (String) -> Unit = {}
) {
    val rankingsData = mapOf(
        "Men's Pound-for-Pound" to listOf(
            "1" to "Islam Makhachev", "2" to "Ilia Topuria", "3" to "Tom Aspinall",
            "4" to "Alex Pereira", "5" to "Arman Tsarukyan"
        ),
        "Heavyweight" to listOf(
            "C" to "Tom Aspinall", "1" to "Curtis Blaydes", "2" to "Sergei Pavlovich",
            "3" to "Ciryl Gane", "4" to "Jailton Almeida"
        ),
        "Middleweight" to listOf(
            "C" to "Dricus Du Plessis", "1" to "Khamzat Chimaev", "2" to "Sean Strickland",
            "3" to "Israel Adesanya", "4" to "Caio Borralho"
        ),
        "Lightweight" to listOf(
            "C" to "Islam Makhachev", "1" to "Arman Tsarukyan", "2" to "Charles Oliveira",
            "3" to "Justin Gaethje", "4" to "Max Holloway"
        ),
        "Featherweight" to listOf(
            "C" to "Ilia Topuria", "1" to "Max Holloway", "2" to "Diego Lopes",
            "3" to "Movsar Evloev", "4" to "Alexander Volkanovski"
        )
    )

    val categories = rankingsData.keys.toList()
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    MMAZoneTemplate {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
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
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "UFC RANKINGS",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedCategory, color = Color.White, fontSize = 16.sp)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar", tint = Color.Red)
                            }
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color(0xFF222222))
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category, color = Color.White) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
            val currentFighters = rankingsData[selectedCategory] ?: emptyList()

            items(currentFighters) { fighter ->
                RankingItem(
                    rank = fighter.first,
                    name = fighter.second,
                    onClick = { onFighterClick(fighter.second) }
                )
            }
            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}

@Composable
fun RankingItem(rank: String, name: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rank,
                color = if (rank == "C") Color(0xFFFFD700) else Color.Red,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.width(40.dp)
            )

            Text(
                text = name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(thickness = 1.dp, color = Color.DarkGray.copy(alpha = 0.3f))
    }
}

@Preview(showBackground = true)
@Composable
fun RankingsPreview() {
    Rankings()
}