package com.example.mmazone.screens.pastEvents


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmazone.api.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PastEventsUiState {
    object Loading : PastEventsUiState()
    data class Success(val events: List<ParsedPastEvent>) : PastEventsUiState()
    data class Error(val message: String) : PastEventsUiState()
}

data class ParsedPastEvent(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val posterUrl: String,
    val fights: List<CompletedFight>
)

class PastEventsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PastEventsUiState>(PastEventsUiState.Loading)
    val uiState: StateFlow<PastEventsUiState> = _uiState

    init { fetchPastEvents() }

    fun fetchPastEvents() {
        if (_uiState.value is PastEventsUiState.Success) return

        viewModelScope.launch {
            _uiState.value = PastEventsUiState.Loading
            try {
                val response = NetworkModule.api.getPastUfcEvents()
                val eventsList = response.body()?.events

                if (response.isSuccessful && !eventsList.isNullOrEmpty()) {
                    val lastFiveEvents = eventsList.take(5).map { apiEvent ->
                        val id = apiEvent.strEvent ?: "unknown_id"
                        val title = apiEvent.strEvent ?: "UFC Event"
                        val date = apiEvent.dateEvent ?: "Unknown Date"
                        val location = apiEvent.strVenue ?: "UFC Apex"
                        val posterUrl = apiEvent.strThumb ?: ""
                        val parsedFights = parseResultsText(apiEvent.strResult ?: "")

                        ParsedPastEvent(id, title, date, location, posterUrl, parsedFights)
                    }

                    _uiState.value = PastEventsUiState.Success(lastFiveEvents)
                } else {
                    _uiState.value = PastEventsUiState.Error("No se encontraron eventos pasados.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = PastEventsUiState.Error("Error de red al cargar el historial.")
            }
        }
    }

    fun getEventById(eventId: String): ParsedPastEvent? {
        val currentState = _uiState.value
        if (currentState is PastEventsUiState.Success) {
            return currentState.events.find { it.id == eventId }
        }
        return null
    }
    private fun parseResultsText(rawResult: String): List<CompletedFight> {
        val fights = mutableListOf<CompletedFight>()
        val lines = rawResult.lines().map { it.trim() }.filter { it.isNotEmpty() }

        for (line in lines) {
            if (line.contains("Main Card", ignoreCase = true) || line.contains("Prelims", ignoreCase = true)) continue
            val defToken = listOf(" def. ", " defeated ", " beat ").find { line.contains(it, ignoreCase = true) }

            if (defToken != null) {
                val winner = line.substringBefore(defToken).trim()
                val remainder = line.substringAfter(defToken).trim()

                val loser = if (remainder.contains(" via ", ignoreCase = true)) remainder.substringBefore(" via ").trim() else remainder.substringBefore(" R").trim()
                val method = if (remainder.contains(" via ", ignoreCase = true)) remainder.substringAfter(" via ").trim() else "Decision"

                fights.add(CompletedFight(winner, loser, "UFC Bout", method, "Official"))
            }
        }
        return fights
    }
}