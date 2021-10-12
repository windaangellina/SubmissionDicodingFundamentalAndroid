package com.example.appgithub.data.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailUserResponse(
    @SerializedName("avatar_url")
    var avatarUrl: String,
    var bio: String? = "undefined",
    var blog: String? = "undefined",
    var company: String? = "undefined",
    @SerializedName("created_at")
    var createdAt: String,
    var email: String? = "undefined",
    @SerializedName("events_url")
    var eventsUrl: String,
    var followers: Int? = 0,
    @SerializedName("followers_url")
    var followersUrl: String,
    var following: Int? = 0,
    @SerializedName("following_url")
    var followingUrl: String,
    @SerializedName("gists_url")
    var gistsUrl: String,
    @SerializedName("gravatar_id")
    var gravatarId: String,
    var hireable: String?,
    @SerializedName("html_url")
    var htmlUrl: String,
    var id: Int,
    var location: String? = "undefined",
    var login: String,
    var name: String? = "undefined",
    @SerializedName("node_id")
    var nodeId: String? = "undefined",
    @SerializedName("organizations_url")
    var organizationsUrl: String,
    @SerializedName("public_gists")
    var publicGists: Int? = 0,
    @SerializedName("public_repos")
    var publicRepos: Int? = 0,
    @SerializedName("received_events_url")
    var receivedEventsUrl: String,
    @SerializedName("repos_url")
    var reposUrl: String,
    @SerializedName("site_admin")
    var siteAdmin: Boolean? = false,
    @SerializedName("starred_url")
    var starredUrl: String,
    @SerializedName("subscriptions_url")
    var subscriptionsUrl: String,
    @SerializedName("twitter_username")
    var twitterUsername: String? = "undefined",
    var type: String,
    @SerializedName("updated_at")
    var updatedAt: String,
    var url: String
) : Parcelable