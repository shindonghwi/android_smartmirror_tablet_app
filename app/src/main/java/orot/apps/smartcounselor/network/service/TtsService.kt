package orot.apps.smartcounselor.network.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import orot.apps.smartcounselor.network.api.TtsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TtsService {
    val BASE_URL = "https://freetts.com"
    fun getTtsService(): TtsApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
//            .addNetworkInterceptor(Interceptor { chain ->
//                chain.proceed(
//                    chain.request().newBuilder()
//                        .addHeader(
//                            "User-Agent",
//                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
//                        ).build()
//                )
//            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TtsApi::class.java)
    }
}

val ttsService = TtsService.getTtsService()
