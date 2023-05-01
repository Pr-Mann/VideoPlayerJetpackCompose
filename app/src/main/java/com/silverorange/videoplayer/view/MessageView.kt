package com.silverorange.videoplayer.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MessageView(message: String) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBarView()
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = message, modifier = Modifier.align(alignment = Alignment.Center))
        }
    }
}