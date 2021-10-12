package com.example.appgithub.data.response

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("avatar_url")
    var avatarUrl: String,
    @SerializedName("events_url")
    var eventsUrl: String,
    @SerializedName("followers_url")
    var followersUrl: String,
    @SerializedName("following_url")
    var followingUrl: String,
    @SerializedName("gists_url")
    var gistsUrl: String,
    @SerializedName("gravatar_id")
    var gravatarId: String,
    @SerializedName("html_url")
    var htmlUrl: String,
    var id: Int,
    var login: String,
    @SerializedName("node_id")
    var nodeId: String,
    @SerializedName("organizations_url")
    var organizationsUrl: String,
    @SerializedName("received_events_url")
    var receivedEventsUrl: String,
    @SerializedName("repos_url")
    var reposUrl: String,
    var score: Double,
    @SerializedName("site_admin")
    var siteAdmin: Boolean,
    @SerializedName("starred_url")
    var starredUrl: String,
    @SerializedName("subscriptions_url")
    var subscriptionsUrl: String,
    var type: String,
    var url: String
)