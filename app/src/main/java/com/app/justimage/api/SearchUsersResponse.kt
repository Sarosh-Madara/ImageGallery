package com.app.justimage.api

import com.app.justimage.data.GithubUserModel


data class SearchUsersResponse(
        val incomplete_results: Boolean,
        val items: List<GithubUserModel>,
        val total_count: Int
)