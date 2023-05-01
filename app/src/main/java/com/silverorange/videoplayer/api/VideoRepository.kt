package com.silverorange.videoplayer.api

class VideoRepository constructor(private val retrofitService: RetrofitService) {
    suspend fun getAllVideos() = retrofitService.getAllVideos()
}