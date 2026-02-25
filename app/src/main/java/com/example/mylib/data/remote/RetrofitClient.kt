package com.example.mylib.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{
    private const val API_URL = "https://mylib-15dd.onrender.com/"

    private val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val authenticationApi: AuthenticationApi by lazy{retrofit.create(AuthenticationApi::class.java)}
    val bookApi: BookApi by lazy{retrofit.create(BookApi::class.java)}
    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java)}
    //Setja flieri api hér
}