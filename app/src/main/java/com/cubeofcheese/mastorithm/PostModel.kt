package com.cubeofcheese.mastorithm

import android.text.Spanned

data class PostModel(var displayName: String, var username: String, var postContent: Spanned)

//{
//    lateinit var displayName: String
//    lateinit var username: String
//    lateinit var postContent: String
//
//    constructor(displayName: String, username: String, postContent: String) {
//        this.displayName = displayName
//        this.username = username
//        this.postContent = postContent
//    }
//
//}