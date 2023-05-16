package com.cubeofcheese.mastorithm

import com.cubeofcheese.mastorithm.models.AccountModel
import com.cubeofcheese.mastorithm.models.MediaAttachment
import com.google.gson.annotations.SerializedName

data class TestData(
    val id: String,
    val content: String,
    val reblog: TestData,
    @SerializedName("media_attachments")
    val mediaAttachments: ArrayList<MediaAttachment>,
    val account: AccountModel
)