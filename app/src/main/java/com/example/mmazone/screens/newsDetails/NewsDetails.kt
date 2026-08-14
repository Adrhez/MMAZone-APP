package com.example.mmazone.screens.newsDetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mmazone.R
import com.example.mmazone.screens.MMAZoneHeader
import com.example.mmazone.screens.MMAZoneTemplate
import com.example.mmazone.screens.newsDetails.NewsViewModel

@Composable
fun NewsDetails(
    newsIndex: Int,
    onBackClick: () -> Unit = {},
    viewModel: NewsViewModel
){
    val uriHandler = LocalUriHandler.current
    val article = viewModel.getArticleByIndex(newsIndex)
    MMAZoneTemplate {
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
            if (article != null) {
                item {
                    Text(
                        text = article.title ?: "No Title",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )

                    AsyncImage(
                        model = article.urlToImage,
                        contentDescription = "Cover Image",
                        modifier = Modifier.fillMaxWidth().height(250.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.freedom250),
                        error = painterResource(id = R.drawable.freedom250)
                    )
                }

                item {
                    Text(
                        text = article.description ?: "Content not availible",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp),
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { article.url?.let { uriHandler.openUri(it) } },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        Text("READ ARTICLE", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(64.dp))
                }
            } else {
                item {
                    Text("Couldn't load the content.", color = Color.Red, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}