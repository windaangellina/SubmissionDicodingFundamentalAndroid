package com.example.appgithub.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{
    private const val BASE_URL = "https://api.github.com/"
    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // by lazy = hanya dibuat sekali saja
    val instanceSearch : GithubSearchUserApi by lazy {
        retrofit.create(GithubSearchUserApi::class.java)
    }

    val instanceDetailUser : GithubDetailUserApi by lazy {
        retrofit.create(GithubDetailUserApi::class.java)
    }

    val instanceFollows : GithubUserFollowsApi by lazy {
        retrofit.create(GithubUserFollowsApi::class.java)
    }
}