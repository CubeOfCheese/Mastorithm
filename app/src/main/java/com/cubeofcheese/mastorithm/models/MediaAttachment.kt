package com.cubeofcheese.mastorithm.models

data class MediaAttachment (var type: String,
    var url: String,
    var preview_url: String,
    var description: String? = null)