package com.jasonstudio.cookbook2.model

data class VideosResponse (
        val videos: MutableList<Video>,
        val totalResults: Long
)