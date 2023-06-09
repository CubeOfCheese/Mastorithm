package com.cubeofcheese.mastorithm.models

import com.google.gson.annotations.SerializedName

data class TestData(
    val id: String,
    val in_reply_to_account_id: String,
    val replies_count: Int,
    val reblogs_count: Int,
    val favourites_count: Int,
    val reblogged: Boolean,
    val content: String,
    val reblog: TestData,
    @SerializedName("media_attachments")
    val mediaAttachments: ArrayList<MediaAttachment>,
    val account: AccountModel
)