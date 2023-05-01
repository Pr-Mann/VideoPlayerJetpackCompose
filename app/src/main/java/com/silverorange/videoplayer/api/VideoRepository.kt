package com.silverorange.videoplayer.api

class VideoRepository(private val retrofitService: RetrofitService) {
    suspend fun getAllVideos() = retrofitService.getAllVideos()
}