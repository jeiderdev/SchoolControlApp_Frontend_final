package com.example.schoolcontrol.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.schoolcontrol.utils.TokenHolder

object ApiClient {
    // private const val BASE_URL = "http://172.30.96.1:8000"
    private const val BASE_URL = "http://192.168.1.7:8000"
    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val authInterceptor = Interceptor { chain ->
        val reqBuilder = chain.request().newBuilder()
        TokenHolder.authHeader()?.let { reqBuilder.addHeader("Authorization", it) }
        chain.proceed(reqBuilder.build())
    }
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor(authInterceptor).addInterceptor(logging).build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
