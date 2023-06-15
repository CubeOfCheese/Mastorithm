package com.cubeofcheese.mastorithm.models

data class PostModel(var id: String,
                     var displayName: String,
                     var username: String,
                     var avatar: String,
                     var postContent: String,
                     val imagePreview: String?,
                     val presenceReason: String?,
                     val repliesCount: Int,
                     val reblogsCount: Int,
                     val favoritesCount: Int
)