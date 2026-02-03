package com.corvit.corvit_lms.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://corvit.analogenterprises.ae/api/mobile/"

    val api: CorvitApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CorvitApi::class.java)
    }
}