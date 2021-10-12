package com.example.appgithub.data

import com.example.appgithub.data.response.FollowsResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface GithubUserFollowsApi {
    @GET("/users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowersByUsername(
        @Path("username")username : String
    ) : Call<List<FollowsResponseItem>>


    @GET("/users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowingsByUsername(
        @Path("username")username : String
    ) : Call<List<FollowsResponseItem>>
}