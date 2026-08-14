package com.example.mmazone.api

import com.example.mmazone.BuildConfig
import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// =================================================================
// 1. DATA CLASSES
// =================================================================
data class CitoSearchResponse(
    val success: Boolean,
    val data: CitoDataContainer?
)

data class CitoDataContainer(
    val fighters: List<CitoFighter>?,
    val bouts: List<CitoBout>?,
    val events: List<CitoRootEvent>?
)

data class CitoRootEvent(
    val slug: String?,
    val startsAt: String?
)

data class CitoFighter(
    val id: String?,
    val slug: String?,
    val name: String?,
    val nickname: String?,
    val division: String?,
    val recordText: String?,
    val country: String?,
    val age: Int?,
    val bodyImageUrl: String?,
    val headshotUrl: String?
)

data class CitoBout(
    val eventSlug: String?,
    val weightClass: String?,
    val method: String?,
    val resultRound: Int?,
    val resultTime: String?,
    val winnerFighterSlug: String?,
    val fighters: List<CitoBoutFighter>?,
    val event: CitoBoutEvent?
)

data class CitoBoutFighter(
    val fighterSlug: String?,
    val fighterName: String?,
    val outcome: String?
)

data class CitoBoutEvent(
    val eventDate: String?
)

data class CitoEventBoutsResponse(
    val success: Boolean?,
    val data: List<CitoBout>?
)

// =================================================================
// 2. INTERFAZ RETROFIT
// =================================================================
interface CitoApiService {

    @GET("ufc/search")
    suspend fun searchFighter(
        @Query("q") query: String
    ): Response<CitoSearchResponse>

    @GET("ufc/events/{eventSlug}/bouts")
    suspend fun getEventBouts(
        @Path("eventSlug") eventSlug: String
    ): Response<CitoEventBoutsResponse>
}

// =================================================================
// 3. CLIENTE DE RED
// =================================================================
object CitoNetwork {
    private const val BASE_URL = "https://api.citoapi.com/api/v1/"
    private const val API_KEY = BuildConfig.CITO_API_KEY

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("x-api-key", API_KEY)
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val api: CitoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CitoApiService::class.java)
    }
}