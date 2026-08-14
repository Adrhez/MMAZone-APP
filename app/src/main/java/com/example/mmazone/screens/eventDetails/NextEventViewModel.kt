package com.example.mmazone.screens.eventDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmazone.api.CitoNetwork
import com.example.mmazone.api.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class EventUiState {
    object Loading : EventUiState()
    data class Success(
        val title: String,
        val date: String,
        val arena: String,
        val country: String,
        val posterUrl: String,
        val mainEvent: Pair<String, String>?,
        val mainCard: List<Pair<String, String>>,
        val prelims: List<Pair<String, String>>
    ) : EventUiState()
    data class Error(val message: String) : EventUiState()
}

class NextEventViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState

    init {
        fetchNextEvent()
    }

    fun fetchNextEvent() {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            try {
                val response = NetworkModule.api.getNextUfcEvents()
                val upcomingEvents = response.body()?.events

                if (response.isSuccessful && !upcomingEvents.isNullOrEmpty()) {

                    val validEvents = upcomingEvents.filter { event ->
                        val title = event.strEvent ?: ""
                        !title.contains("TBA", ignoreCase = true) && title.isNotBlank()
                    }

                    val mainEventData = validEvents.firstOrNull() ?: upcomingEvents.first()

                    val rawEventTitle = mainEventData.strEvent ?: "UFC Fight Night"
                    val eventDate = mainEventData.dateEvent ?: "TBA"
                    val arenaName = mainEventData.strVenue ?: "UFC Arena"
                    val countryName = mainEventData.strCountry ?: "Ubicación por confirmar"
                    val posterImage = mainEventData.strThumb ?: "https://www.thesportsdb.com/images/media/league/badge/tww2p11547160236.png"

                    var exactSlug = rawEventTitle.lowercase()
                        .replace(":", "")
                        .replace(".", "")
                        .replace(",", "")
                        .replace("'", "")
                        .trim()
                        .replace(Regex("\\s+"), "-")

                    if (rawEventTitle.contains(" vs ", ignoreCase = true)) {
                        try {
                            val leftSide = rawEventTitle.split(Regex(" vs ", RegexOption.IGNORE_CASE))[0].trim()
                            val fighterLastName = leftSide.split(" ").last()

                            // Buscamos al luchador en CitoAPI
                            val searchResponse = CitoNetwork.api.searchFighter(fighterLastName)
                            val eventsList = searchResponse.body()?.data?.events

                            if (searchResponse.isSuccessful && !eventsList.isNullOrEmpty()) {
                                val targetEvent = eventsList.maxByOrNull { it.startsAt ?: "" }
                                if (!targetEvent?.slug.isNullOrEmpty()) {
                                    exactSlug = targetEvent?.slug!!
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    var finalMainEvent: Pair<String, String>? = null
                    var finalMainCard: List<Pair<String, String>> = emptyList()
                    var finalPrelims: List<Pair<String, String>> = emptyList()
                    var citoSuccess = false

                    try {
                        val citoResponse = CitoNetwork.api.getEventBouts(exactSlug)
                        val bouts = citoResponse.body()?.data

                        if (citoResponse.isSuccessful && !bouts.isNullOrEmpty()) {
                            val mappedBouts = bouts.mapNotNull { bout ->
                                val fighter1 = bout.fighters?.getOrNull(0)?.fighterName
                                val fighter2 = bout.fighters?.getOrNull(1)?.fighterName
                                val weight = bout.weightClass ?: "Bout"

                                if (fighter1 != null && fighter2 != null) {
                                    Pair("$fighter1 vs $fighter2", weight)
                                } else null
                            }

                            if (mappedBouts.isNotEmpty()) {
                                finalMainEvent = mappedBouts.first()
                                finalMainCard = mappedBouts.drop(1).take(4)
                                finalPrelims = mappedBouts.drop(5)
                                citoSuccess = true
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    if (!citoSuccess) {
                        var cleanTitle = rawEventTitle
                            .replace(Regex("UFC Fight Night \\d*", RegexOption.IGNORE_CASE), "")
                            .replace(Regex("UFC \\d*", RegexOption.IGNORE_CASE), "")
                            .trim()

                        if (cleanTitle.startsWith(":")) cleanTitle = cleanTitle.substring(1).trim()

                        val extractedMainEvent = cleanTitle.ifBlank { "Cartelera pendiente de confirmación" }
                        finalMainEvent = Pair(extractedMainEvent, "Main Event")
                    }
                    _uiState.value = EventUiState.Success(
                        title = rawEventTitle,
                        date = eventDate,
                        arena = arenaName,
                        country = countryName,
                        posterUrl = posterImage,
                        mainEvent = finalMainEvent,
                        mainCard = finalMainCard,
                        prelims = finalPrelims
                    )

                } else {
                    _uiState.value = EventUiState.Error("No upcoming UFC events found.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = EventUiState.Error("Network error: Check your internet connection.")
            }
        }
    }
}