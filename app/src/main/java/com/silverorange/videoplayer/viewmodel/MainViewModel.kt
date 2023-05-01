package com.silverorange.videoplayer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.api.VideoRepository
import com.silverorange.videoplayer.model.VideoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class MainViewModel(private val videoRepository: VideoRepository) : ViewModel() {

    val videoList = MutableLiveData<List<VideoData>>()
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getAllVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = videoRepository.getAllVideos()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        videoList.postValue(response.body())
                    } else {
                        _errorMessage.postValue("An error occurred...TRY AGAIN!")
                    }
                }
            } catch (e: SocketTimeoutException) {
                _errorMessage.postValue("Error: ${e.message}")
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }
}