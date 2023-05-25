package com.jasonstudio.cookbook2.model

data class VideosResponse (
    val videos: MutableList<Video>,
    val totalResults: Long
) {
    companion object {
        fun getDemo(): VideosResponse {
            return VideosResponse(
                mutableListOf(
                    Video.getDemo(),
                    Video.getDemo(),
                ),
                2L
            )
        }
    }
}