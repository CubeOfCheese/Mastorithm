package com.cubeofcheese.mastorithm.models

import android.text.Spanned

data class PostModel(var id: String,
                     var displayName: String,
                     var username: String,
                     var avatar: String,
                     var postContent: Spanned,
                     val imagePreview: String?,
                     val boostedByMessage: String?,
                     val repliesCount: Int,
                     val reblogsCount: Int,
                     val favoritesCount: Int
)