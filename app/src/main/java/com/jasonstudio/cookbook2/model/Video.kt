package com.jasonstudio.cookbook2.model

data class Video (
    val title: String,
    val length: Long,
    val rating: Double,
    val shortTitle: String,
    val thumbnail: String,
    val views: Long,
    val youTubeID: String
)

