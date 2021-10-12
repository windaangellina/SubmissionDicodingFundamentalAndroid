package com.example.appgithub.data

import com.example.appgithub.data.response.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubSearchUserApi {
    // Function that makes the network request, blocking the current thread
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchResults(
        @Query("q") login : String
    ) : Call<SearchUserResponse>
}