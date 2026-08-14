package com.example.mmazone.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class SportsDbResponse(val events: List<ApiEvent>?)

data class SportsDbPlayerResponse(
    val players: List<ApiPlayer>?
)

data class ApiPlayer(
    val idPlayer: String?,
    val strPlayer: String?,
    val strNationality: String?,
    val dateBorn: String?,
    val strPosition: String?,
    val strThumb: String?,
    val strCutout: String?,
    val strRender: String?,
    val strDescriptionEN: String?
)

data class ApiEvent(
    val strEvent: String?,
    val dateEvent: String?,
    val strTime: String?,
    val strVenue: String?,
    val strCountry: String?,
    val strThumb: String?,
    val strResult: String?
)

interface UfcApiService {
    @GET("eventsnextleague.php?id=4443")
    suspend fun getNextUfcEvents(): retrofit2.Response<SportsDbResponse>

    @GET("eventspastleague.php?id=4443")
    suspend fun getPastUfcEvents(): retrofit2.Response<SportsDbResponse>
}


object NetworkModule {
    private const val BASE_URL = "https://www.thesportsdb.com/api/v1/json/3/"

    val api: UfcApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UfcApiService::class.java)
    }
}