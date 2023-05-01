package com.silverorange.videoplayer.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.api.RetrofitService
import com.silverorange.videoplayer.api.VideoRepository
import com.silverorange.videoplayer.model.VideoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel() : ViewModel() {

    val videoList = MutableLiveData<List<VideoData>>()
    var mainRepository = VideoRepository(RetrofitService.getInstance())

    fun getAllVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = mainRepository.getAllVideos()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    videoList.postValue(response.body())
                }
            }
        }
    }
}