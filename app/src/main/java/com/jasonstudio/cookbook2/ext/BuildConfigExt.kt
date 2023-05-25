package com.jasonstudio.cookbook2.ext


object BuildConfigObject {
    fun getS() = if (com.jasonstudio.cookbook2.BuildConfig.isTLS) "s" else ""
}