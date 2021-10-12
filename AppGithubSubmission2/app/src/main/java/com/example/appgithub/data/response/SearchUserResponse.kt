package com.example.appgithub.data.response

import com.google.gson.annotations.SerializedName

class SearchUserResponse {
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean = false

    var items: List<Item>? = null

    @SerializedName("total_count")
    var totalCount: Int = 0

    override fun toString(): String {
        return "SearchResponse size : ${items?.size}"
    }
}


