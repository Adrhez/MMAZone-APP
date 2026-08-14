package com.example.mmazone.screens.fighterProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mmazone.api.CitoNetwork
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class FighterUiState {
    object Loading : FighterUiState()
    data class Success(
        val name: String,
        val nickname: String,
        val division: String,
        val record: String,
        val age: Int,
        val country: String,
        val imageUrl: String,
        val pastFights: List<Triple<String, String, String>>
    ) : FighterUiState()
    data class Error(val message: String) : FighterUiState()
}

class FighterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<FighterUiState>(FighterUiState.Loading)
    val uiState: StateFlow<FighterUiState> = _uiState

    fun loadFighter(fighterQuery: String) {
        viewModelScope.launch {
            _uiState.value = FighterUiState.Loading
            try {
                val response = CitoNetwork.api.searchFighter(fighterQuery)
                val dataContainer = response.body()?.data
                val targetFighter = dataContainer?.fighters?.firstOrNull()

                if (response.isSuccessful && targetFighter != null) {

                    val nameText = targetFighter.name ?: "Atleta UFC"
                    val nicknameText = targetFighter.nickname?.replace("\"", "") ?: ""
                    val divisionText = (targetFighter.division ?: "MMA") + " Division"

                    val cleanRecord = targetFighter.recordText?.substringBefore(" ") ?: "0-0-0"

                    val ageNumber = targetFighter.age ?: 29
                    val countryText = targetFighter.country ?: "Internacional"

                    val imageModel = targetFighter.bodyImageUrl ?: targetFighter.headshotUrl ?: ""

                    val rawBouts = dataContainer.bouts ?: emptyList()
                    val targetSlug = targetFighter.slug ?: ""

                    val myBouts = rawBouts.filter { bout ->
                        bout.fighters?.any { it.fighterSlug == targetSlug } == true
                    }

                    val sortedBouts = myBouts.sortedByDescending { it.event?.eventDate ?: "" }
                    val fightHistoryList = sortedBouts.mapNotNull { bout ->
                        val opponentObj = bout.fighters?.find { it.fighterSlug != targetSlug }

                        if (opponentObj != null) {
                            val outcomeLetter = if (bout.winnerFighterSlug == targetSlug) "W" else "L"
                            val opponentName = opponentObj.fighterName ?: "Contendiente"
                            val methodFormatted = "${bout.method ?: "DEC"} - R${bout.resultRound ?: 1} (${bout.resultTime ?: "5:00"})"

                            Triple(outcomeLetter, opponentName, methodFormatted)
                        } else {
                            null
                        }
                    }.distinct()

                    _uiState.value = FighterUiState.Success(
                        name = nameText.uppercase(),
                        nickname = nicknameText.uppercase(),
                        division = divisionText,
                        record = cleanRecord,
                        age = ageNumber,
                        country = countryText,
                        imageUrl = imageModel,
                        pastFights = fightHistoryList
                    )

                } else {
                    _uiState.value = FighterUiState.Error("No se encontró al luchador '$fighterQuery' en CitoAPI.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = FighterUiState.Error("Error de red: Comprueba tu conexión a internet.")
            }
        }
    }
}