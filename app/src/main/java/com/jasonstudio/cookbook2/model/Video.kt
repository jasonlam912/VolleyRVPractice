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
) {
    companion object {
        fun getDemo(): Video {
            return Video(
                title = "Keto Diet Update!!",
                shortTitle = "Keto Diet Update",
                youTubeID = "rKCOQU3ptIE",
                rating = 1.0,
                views = 537,
                thumbnail = "https://i.ytimg.com/vi/rKCOQU3ptIE/mqdefault.jpg",
                length = 389L
            )
        }
    }
}

