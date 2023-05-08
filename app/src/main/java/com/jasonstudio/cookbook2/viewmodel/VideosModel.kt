package com.jasonstudio.cookbook2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasonstudio.cookbook2.Network.SpoonacularService
import com.jasonstudio.cookbook2.model.Video
import com.jasonstudio.cookbook2.util.LogUtil
import kotlinx.coroutines.launch

class VideosModel: ViewModel() {
    private val service = SpoonacularService.getInstance()
    val videos = MutableLiveData<MutableList<Video>>()
    val maxBatchSize = 10
    var size = 0
    var maxSize = Int.MAX_VALUE
    val offset: Int
    get() = size - maxBatchSize
    var batchSize: Int = 0
    val isLastData: Boolean
    get() = size >= maxSize
    var isLoading = false

    var query: String? = null
    var remainingQuery: String? = null
    fun getVideos(query: String = "", isReload: Boolean = false) {
        if (isLastData) {
            return
        }
        if (!isReload) {
            size += maxBatchSize
        }

        if (this.query == null) {
            this.query = query
            this.remainingQuery = query
        }
        viewModelScope.launch {
            isLoading = true
            val result = service.getVideos(
                query = this@VideosModel.query!!,
                offset = offset,
                number = maxBatchSize,
            )
            if (result.isSuccessful) {
                result.body()?.let {
                    if (it.totalResults.toInt() == 0) {
                        this@VideosModel.apply {
                            this.query = remainingQuery?.substringAfterLast(" ") ?: ""
                            remainingQuery = remainingQuery?.substringBeforeLast(" ") ?: ""
                            LogUtil.log(
                                this.query,
                                remainingQuery,
                            )
                            getVideos(isReload = true)
                            return@launch
                        }
                    } else {
                        maxSize = it.totalResults.toInt()
                        batchSize = it.videos.size
                        videos.postValue(it.videos)
                        isLoading = false
                    }
                }
            }
        }
    }

    fun getMoreVideos(lastVisiblePos: Int) {
        if (!isLoading && lastVisiblePos == size) {
            getVideos()
        }
    }

}