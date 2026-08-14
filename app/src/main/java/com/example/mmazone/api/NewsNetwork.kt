package com.example.mmazone.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>?
)

data class NewsArticle(
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?
)


interface NewsApiService {
    @GET("v2/everything?q=UFC&searchIn=title&sortBy=publishedAt&language=en")
    suspend fun getLatestNews(
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}

object NewsNetwork {
    private const val BASE_URL = "https://newsapi.org/"

    val api: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}