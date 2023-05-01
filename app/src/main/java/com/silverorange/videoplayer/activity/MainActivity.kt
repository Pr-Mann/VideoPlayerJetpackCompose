package com.silverorange.videoplayer.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.silverorange.videoplayer.R
import com.silverorange.videoplayer.api.RetrofitService
import com.silverorange.videoplayer.network.ConnectionLiveData
import com.silverorange.videoplayer.repository.VideoRepository
import com.silverorange.videoplayer.view.MainActivityView
import com.silverorange.videoplayer.view.MessageView
import com.silverorange.videoplayer.viewmodel.MainViewModel
import com.silverorange.videoplayer.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = VideoRepository(retrofitService)

        viewModel = ViewModelProvider(
            this, MainViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)
        connectionLiveData = ConnectionLiveData(application)

        connectionLiveData.observe(this) { isConnected ->
            if (isConnected) {
                viewModel.getAllVideos()
            } else {
                viewModel._errorMessage.value = resources.getString(R.string.no_network)
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                setContent {
                    MessageView(message = errorMessage)
                }
            }
        }

        viewModel.videoList.observe(this) { videoList ->
            if (videoList.isNotEmpty()) {
                setContent {
                    MainActivityView(videoList.sortedBy { it.publishedAt }.reversed())
                }
            }
        }
    }
}
