package com.example.mmazone.screens.newsDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmazone.BuildConfig
import com.example.mmazone.api.NewsArticle
import com.example.mmazone.api.NewsNetwork
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<NewsArticle>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState

    fun fetchNewsIfNeeded() {
        if (_uiState.value is NewsUiState.Success) {
            return
        }

        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                val response = NewsNetwork.api.getLatestNews(apiKey = BuildConfig.NEWS_API_KEY)
                val articles = response.body()?.articles

                if (response.isSuccessful && !articles.isNullOrEmpty()) {
                    val validArticles = articles.filter {
                        !it.title.isNullOrBlank() && !it.urlToImage.isNullOrBlank()
                    }
                    val dashboardArticles = validArticles.take(9)

                    _uiState.value = NewsUiState.Success(dashboardArticles)
                } else {
                    _uiState.value = NewsUiState.Error("Couldn't load news")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = NewsUiState.Error("Network error.")
            }
        }
    }

    fun getArticleByIndex(index: Int): NewsArticle? {
        val currentState = _uiState.value
        if (currentState is NewsUiState.Success) {
            return currentState.articles.getOrNull(index)
        }
        return null
    }
}