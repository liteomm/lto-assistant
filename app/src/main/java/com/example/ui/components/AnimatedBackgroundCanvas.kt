package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnimatedBackgroundCanvas(
    bgType: String,
    speed: String = "Normal",
    intensity: String = "Medium",
    modifier: Modifier = Modifier
) {
    AnimatedBackgroundView(
        bgId = bgType,
        speed = speed,
        intensity = intensity,
        modifier = modifier
    )
}
