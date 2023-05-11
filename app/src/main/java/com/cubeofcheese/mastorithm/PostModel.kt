package com.cubeofcheese.mastorithm

import android.text.Spanned

data class PostModel(var id: String,
                     var displayName: String,
                     var username: String,
                     var avatar: String,
                     var postContent: Spanned)