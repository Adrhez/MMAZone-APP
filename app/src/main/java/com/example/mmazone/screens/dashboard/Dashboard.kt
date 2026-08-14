package com.example.mmazone.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mmazone.R
import com.example.mmazone.screens.MMAZoneTemplate
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil.compose.AsyncImage
import com.example.mmazone.screens.eventDetails.NextEventViewModel
import com.example.mmazone.screens.eventDetails.EventUiState
import com.example.mmazone.screens.newsDetails.NewsUiState
import com.example.mmazone.screens.newsDetails.NewsViewModel
import com.example.mmazone.screens.fighterProfile.FighterViewModel
import com.example.mmazone.screens.fighterProfile.FighterUiState

val darkGrey = Color(0xFF121212)
val TitleFont = FontFamily(
    Font(resId = R.font.ethnocentricregular)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    onEventClick: () -> Unit = {},
    onFighterClick: () -> Unit = {},
    onNewsClick: (Int) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onResultsClick: () -> Unit = {},
    onRankingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPastEventsClick: () -> Unit = {},
    viewModel: NextEventViewModel = viewModel(),
    newsViewModel: NewsViewModel,
    fighterViewModel: FighterViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val newsState by newsViewModel.uiState.collectAsState()
    val fighterState by fighterViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        newsViewModel.fetchNewsIfNeeded()
        fighterViewModel.loadFighter("topuria")
    }

    val posterModel: Any = when (val state = uiState) {
        is EventUiState.Success -> state.posterUrl
        else -> R.drawable.freedom250
    }

    val eventTitleText = when (val state = uiState) {
        is EventUiState.Success -> state.title
        else -> "UFC Freedom 250"
    }

    val eventDateText = when (val state = uiState) {
        is EventUiState.Success -> state.date
        else -> "12/06/26"
    }

    val featuredFighterRecord = when (val state = fighterState) {
        is FighterUiState.Success -> state.record
        is FighterUiState.Loading -> "Cargando..."
        else -> "17-0"
    }

    val featuredFighterName = when (val state = fighterState) {
        is FighterUiState.Success -> state.name
        else -> "Ilia Topuria"
    }

    val featuredFighterImage: Any = when (val state = fighterState) {
        is FighterUiState.Success -> state.imageUrl
        else -> R.drawable.topuria3
    }

    MMAZoneTemplate {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { paddingValues ->

            DottedBackground()

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp),
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(bottom = 26.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(24.dp),
                            onClick = onEventClick
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                AsyncImage(
                                    model = posterModel,
                                    contentDescription = "Thumbnail",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                )
                                Column(Modifier.align(Alignment.TopStart)) {
                                    Text(
                                        text = "Next events",
                                        color = Color.White,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                                Column(Modifier.align(Alignment.TopEnd)) {
                                    Text(
                                        text = eventDateText,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                                Column(Modifier.align(Alignment.BottomEnd)) {
                                    Text(
                                        text = eventTitleText,
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(bottom = 26.dp),
                        Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(24.dp),
                            onClick = onFighterClick
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.spaingeorgia),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                AsyncImage(
                                    model = featuredFighterImage,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .align(Alignment.TopCenter),
                                    contentScale = ContentScale.Fit
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                )
                                Column(Modifier.align(Alignment.TopStart)) {
                                    Text(
                                        text = "Featured fighter",
                                        color = Color.White,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                                Column(Modifier.align(Alignment.BottomStart)) {
                                    Text(
                                        text = featuredFighterName,
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                                Column(Modifier.align(Alignment.BottomEnd)) {
                                    Text(
                                        text = featuredFighterRecord,
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    NewsSection(
                        newsState = newsState,
                        onNewsClick = onNewsClick
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(bottom = 16.dp),
                        Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(24.dp),
                            onClick = onRankingsClick
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ank),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                )
                                Column(Modifier.align(Alignment.TopStart)) {
                                    Text(
                                        text = "UFC Rankings",
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(24.dp),
                            onClick = onPastEventsClick
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.fightnight),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                )
                                Column(Modifier.align(Alignment.TopStart)) {
                                    Text(
                                        text = "Previous results",
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(bottom = 16.dp),
                        Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(24.dp),
                            onClick = onAboutClick
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.DarkGray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                )
                                Column(Modifier.align(Alignment.Center)) {
                                    Text(
                                        text = "About us",
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(24.dp),
                            onClick = onSettingsClick
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.DarkGray)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                )
                                Column(Modifier.align(Alignment.Center)) {
                                    Text(
                                        text = "Settings",
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsSection(newsState: NewsUiState, onNewsClick: (Int) -> Unit = {}) {
    val pagerState = rememberPagerState(pageCount = { 3 }) // 3 Páginas

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 26.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = darkGrey)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("News", color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp)
                Text("Read more news", color = Color.Red.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            // EVALUAMOS EL ESTADO DE LA RED
            when (newsState) {
                is NewsUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Red)
                    }
                }
                is NewsUiState.Error -> {
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                        Text(text = "Off-season", color = Color.Gray, fontSize = 14.sp)
                    }
                }
                is NewsUiState.Success -> {
                    val articles = newsState.articles

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val startIndex = page * 3

                            for (i in 0..2) {
                                val articleIndex = startIndex + i
                                if (articleIndex < articles.size) {
                                    val article = articles[articleIndex]
                                    NewsItem(
                                        modifier = Modifier.weight(1f),
                                        title = article.title ?: "Breaking News",
                                        imageUrl = article.urlToImage ?: "",
                                        dateText = article.publishedAt?.substring(5, 10) ?: "Today",
                                        onNewsClick = { onNewsClick(articleIndex) }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        repeat(3) { index ->
                            val color = if (pagerState.currentPage == index) Color.Red else Color.DarkGray
                            Box(modifier = Modifier.padding(3.dp).clip(CircleShape).background(color).size(6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String,
    dateText: String,
    onNewsClick: () -> Unit
) {
    Column(modifier = modifier) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.3f),
            onClick = onNewsClick
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Article Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.freedom250),
                error = painterResource(id = R.drawable.freedom250)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "MMA", color = Color.Gray, fontSize = 8.sp)
            Text(text = dateText, color = Color.Gray, fontSize = 8.sp)
        }
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

        // CAPA SÓLIDA
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



@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    Dashboard(
        newsViewModel = viewModel()

    )
}
