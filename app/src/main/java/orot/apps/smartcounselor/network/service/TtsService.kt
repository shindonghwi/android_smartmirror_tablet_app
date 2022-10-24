package orot.apps.smartcounselor.network.service

import orot.apps.smartcounselor.network.api.TtsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TtsService {
    val BASE_URL = "https://freetts.com"

    fun getTtsService(): TtsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TtsApi::class.java)
    }
}

val ttsService = TtsService.getTtsService()
