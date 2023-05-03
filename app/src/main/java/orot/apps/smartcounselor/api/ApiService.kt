package orot.apps.smartcounselor.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okio.IOException
import orot.apps.smartcounselor.model.remote.SmhResponseData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

object retrofitClient {
    private const val BASE_URL = "http://saturn.mago52.com:9501/"
    val apiService: ApiService get() = instance.create(ApiService::class.java)

    class AppInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain) = with(chain) {
            val newRequest = request().newBuilder()
                .build()
            proceed(newRequest)
        }
    }

    private val instance = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().run {
            addInterceptor(AppInterceptor())
            connectTimeout(100, TimeUnit.SECONDS)
            readTimeout(300, TimeUnit.SECONDS)
            writeTimeout(300, TimeUnit.SECONDS)
            build()
        })
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

interface ApiService {
    @POST("smh/run")
    suspend fun postRecommendation(@Body requestBody: RequestBody): Response<SmhResponseData>
}
