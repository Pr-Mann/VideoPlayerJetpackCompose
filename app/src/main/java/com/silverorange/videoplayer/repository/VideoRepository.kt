package com.silverorange.videoplayer.repository

import com.silverorange.videoplayer.api.RetrofitService

class VideoRepository(private val retrofitService: RetrofitService) {
    suspend fun getAllVideos() = retrofitService.getAllVideos()
}