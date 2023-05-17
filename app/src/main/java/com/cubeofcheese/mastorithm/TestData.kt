package com.cubeofcheese.mastorithm

import com.cubeofcheese.mastorithm.models.AccountModel
import com.cubeofcheese.mastorithm.models.MediaAttachment
import com.google.gson.annotations.SerializedName

data class TestData(
    val id: String,
    val replies_count: Int,
    val reblogs_count: Int,
    val favourites_count: Int,
    val content: String,
    val reblog: TestData,
    @SerializedName("media_attachments")
    val mediaAttachments: ArrayList<MediaAttachment>,
    val account: AccountModel
)