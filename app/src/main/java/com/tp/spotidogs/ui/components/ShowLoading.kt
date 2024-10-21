package com.tp.spotidogs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tp.spotidogs.ui.theme.Black
import com.tp.spotidogs.ui.theme.Green

@Composable
fun ShowLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Green)
    }
}