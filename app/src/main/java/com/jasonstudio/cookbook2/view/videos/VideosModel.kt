package com.jasonstudio.cookbook2.view.videos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasonstudio.cookbook2.Network.SpoonacularService
import com.jasonstudio.cookbook2.model.Video
import kotlinx.coroutines.launch
import kotlin.math.max

class VideosModel: ViewModel() {
    val service = SpoonacularService.getInstance()
    val videos = MutableLiveData<MutableList<Video>>()
    val maxBatchSize = 10
    var size = 0
    var maxSize = Int.MAX_VALUE
    val offset: Int
    get() = size - maxBatchSize
    var batchSize: Int = 0

    var query: String? = null
    var remainingQuery: String? = null
    fun getVideos(query: String = "") {
        size += maxBatchSize
        if (size >= maxSize) {
            return
        }
        if (this.query == null) {
            this.query = query
            this.remainingQuery = query
        }
        viewModelScope.launch {
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
                            getVideos()
                            return@launch
                        }
                    } else {
                        maxSize = it.totalResults.toInt()
                        batchSize = it.videos.size
                        videos.postValue(it.videos)
                    }
                }
            }
        }

    }

}