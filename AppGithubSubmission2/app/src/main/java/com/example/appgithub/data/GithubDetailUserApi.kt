package com.example.appgithub.data

import com.example.appgithub.data.response.DetailUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface GithubDetailUserApi {
    @GET("/users/{username}")
    @Headers("Authorization: token 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")
    fun findUserDetailByUsername(
        @Path("username")username : String
    ) : Call<DetailUserResponse>

}