package com.silverorange.videoplayer.view

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.silverorange.videoplayer.R
import com.silverorange.videoplayer.model.VideoData

@Composable
fun MainActivityView(videoList: List<VideoData>) {
    Column {
        TopBarView()
        VideoContent(videoList = videoList)
    }
}

@Composable
private fun VideoContent(videoList: List<VideoData>) {
    var currentVideoIndex by remember { mutableStateOf(0) }
    val currentVideo = videoList[currentVideoIndex]
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VideoPlayer(
            modifier = Modifier.height(220.dp),
            currentVideoIndex = currentVideoIndex,
            videoURL = currentVideo.hlsURL,
            onPreviousClick = {
                currentVideoIndex =
                    if (currentVideoIndex > 0) (currentVideoIndex - 1) else 0
            },
            onNextClick = {
                currentVideoIndex =
                    if (currentVideoIndex < videoList.lastIndex) (currentVideoIndex + 1) else currentVideoIndex
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Text(text = currentVideo.title, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(
                text = "Author: ${currentVideo.author.name}",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = "Description:\n\n ${currentVideo.description}",
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun VideoPlayer(
    modifier: Modifier = Modifier,
    currentVideoIndex: Int,
    videoURL: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                setMediaItem(
                    MediaItem.Builder()
                        .apply { setUri(videoURL) }
                        .build()
                )
                playWhenReady = false
                prepare()
            }
    }

    LaunchedEffect(currentVideoIndex) {
        exoPlayer.apply {
            setMediaItem(
                MediaItem.Builder()
                    .apply { setUri(videoURL) }
                    .build()
            )
            playWhenReady = false
            prepare()
        }
    }

    var shouldShowControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
    var playbackState by remember { mutableStateOf(exoPlayer.playbackState) }
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = modifier) {
        DisposableEffect(lifecycleOwner) {
            val lifecycle = lifecycleOwner.lifecycle
            val observer = object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    exoPlayer.playWhenReady = false
                }

                override fun onPause(owner: LifecycleOwner) {
                    exoPlayer.playWhenReady = false
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    exoPlayer.release()
                }
            }
            lifecycle.addObserver(observer)

            val listener = object : Player.Listener {
                override fun onEvents(
                    player: Player,
                    events: Player.Events
                ) {
                    super.onEvents(player, events)
                    isPlaying = player.isPlaying
                    playbackState = player.playbackState
                }
            }

            exoPlayer.addListener(listener)

            onDispose {
                lifecycle.removeObserver(observer)
                exoPlayer.removeListener(listener)
                exoPlayer.release()
            }
        }

        AndroidView(
            modifier = Modifier.clickable {
                shouldShowControls = shouldShowControls.not()
            }, factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams =
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                }
            }
        )

        PlayerControls(
            modifier = Modifier.fillMaxSize(),
            isVisible = { shouldShowControls },
            isPlaying = { isPlaying },
            playbackState = { playbackState },
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onPauseToggle = {
                when {
                    exoPlayer.isPlaying -> {
                        exoPlayer.pause()
                    }

                    exoPlayer.isPlaying.not() &&
                            playbackState == Player.STATE_ENDED -> {
                        exoPlayer.seekTo(0)
                        exoPlayer.playWhenReady = false
                    }

                    else -> {
                        exoPlayer.play()
                    }
                }
                isPlaying = isPlaying.not()
            }
        )
    }
}

@Composable
private fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPauseToggle: () -> Unit,
    playbackState: () -> Int
) {

    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box {
            CenterControls(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                isPlaying = isPlaying,
                onPreviousClick = onPreviousClick,
                onNextClick = onNextClick,
                onPauseToggle = onPauseToggle,
                playbackState = playbackState
            )
        }
    }
}

@Composable
private fun CenterControls(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    playbackState: () -> Int,
    onPreviousClick: () -> Unit,
    onPauseToggle: () -> Unit,
    onNextClick: () -> Unit
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }

    val playerState = remember(playbackState()) { playbackState() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
                .background(color = MaterialTheme.colors.primary), onClick = onPreviousClick
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_previous),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "Previous"
            )
        }

        IconButton(
            modifier = Modifier
                .size(80.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
                .background(color = MaterialTheme.colors.primary), onClick = onPauseToggle
        ) {
            Image(
                modifier = Modifier.size(35.dp),
                contentScale = ContentScale.Crop,
                painter =
                when {
                    isVideoPlaying -> {
                        painterResource(id = R.drawable.ic_pause)
                    }

                    isVideoPlaying.not() && playerState == Player.STATE_ENDED -> {
                        painterResource(id = R.drawable.ic_play)
                    }

                    else -> {
                        painterResource(id = R.drawable.ic_play)
                    }
                },
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "Play/Pause"
            )
        }

        IconButton(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                )
                .background(color = MaterialTheme.colors.primary), onClick = onNextClick
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_next),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "Next"
            )
        }
    }
}

@Preview
@Composable
fun PreviewMainActivityView() {
    MainActivityView(emptyList())
}