package orot.apps.smartcounselor.network.api

import orot.apps.smartcounselor.network.model.ConvertTtsEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TtsApi {
    @GET("Home/PlayAudio")
    suspend fun getConvertTts(
        @Query("Language") language: String = "ko-KR",
        @Query("Voice") voice: String = "Seoyeon_Female",
        @Query("TextMessage") msg: String,
        @Query("id") id: String = "Seoyeon",
        @Query("type") type: Int = 1,
    ): Response<ConvertTtsEntity>
}