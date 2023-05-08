package com.jasonstudio.cookbook2.model

import com.squareup.moshi.Json

data class Video (
    val title: String,
    val length: Long,
    val rating: Double,
    val shortTitle: String,
    val thumbnail: String,
    val views: Long,
    @field:Json(name = "youTubeId")
    val youTubeID: String
)

