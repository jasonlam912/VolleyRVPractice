package com.jasonstudio.cookbook2.model

sealed class WSResponse
data class MainActivityWSResponse (
    val time: String,
    val key: String
): WSResponse()

