package com.silverorange.videoplayer.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.api.VideoRepository
import com.silverorange.videoplayer.model.VideoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val videoRepository: VideoRepository) : ViewModel() {

    val videoList = MutableLiveData<List<VideoData>>()
    fun getAllVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = videoRepository.getAllVideos()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    videoList.postValue(response.body())
                }
            }
        }
    }
}