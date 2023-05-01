package com.silverorange.videoplayer.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.silverorange.videoplayer.api.RetrofitService
import com.silverorange.videoplayer.api.VideoRepository
import com.silverorange.videoplayer.view.MainActivityView
import com.silverorange.videoplayer.view.MainViewModel
import com.silverorange.videoplayer.view.MainViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = VideoRepository(retrofitService)

        viewModel = ViewModelProvider(
            this, MainViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)
        viewModel.getAllVideos()

        viewModel.videoList.observe(this) { videoList ->
            if (videoList.isNotEmpty()) {
                setContent {
                    MainActivityView(videoList.sortedBy { it.publishedAt }.reversed())
                }
            }
        }
    }
}
