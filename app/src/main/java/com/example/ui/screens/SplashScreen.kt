package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logoScale = remember { Animatable(0.8f) }
    val progressAnim = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingGlow"
    )

    LaunchedEffect(Unit) {
        logoScale.animateTo(1.0f, animationSpec = tween(1200))
    }

    LaunchedEffect(Unit) {
        progressAnim.animateTo(1.0f, animationSpec = tween(1500, easing = LinearEasing))
        onSplashComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .clickable { onSplashComplete() },
        contentAlignment = Alignment.Center
    ) {
        // Floating Neon Particle Background Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = CyberCyan.copy(alpha = breathingAlpha * 0.12f),
                radius = size.width * 0.6f,
                center = Offset(size.width * 0.5f, size.height * 0.45f)
            )
            drawCircle(
                color = NeonViolet.copy(alpha = breathingAlpha * 0.15f),
                radius = size.width * 0.45f,
                center = Offset(size.width * 0.5f, size.height * 0.45f)
            )
        }

        // Center Logo & Branding
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale.value)
                    .shadow(
                        elevation = 32.dp,
                        shape = CircleShape,
                        ambientColor = CyberCyan.copy(alpha = breathingAlpha),
                        spotColor = CyberCyan.copy(alpha = breathingAlpha)
                    )
                    .clip(CircleShape)
                    .border(2.dp, Brush.linearGradient(listOf(CyberCyan, NeonViolet)), CircleShape)
                    .background(Brush.radialGradient(listOf(AmoledBlack, CyberCyan.copy(alpha = 0.25f)))),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon_1784784425408),
                    contentDescription = "LTO Assistant Logo",
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "LTO Assistant",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                ),
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Powered by LiteOMM",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                ),
                color = CyberCyan,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Your Smart AI Study Companion",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(44.dp))

            // Liquid Progress Line
            Column(
                modifier = Modifier.fillMaxWidth(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(CyberCyan.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressAnim.value)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(CyberCyan, NeonViolet, EmeraldGlow)
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = when {
                        progressAnim.value < 0.25f -> "Initializing LTO Assistant..."
                        progressAnim.value < 0.50f -> "Connecting LiteOMM Engine..."
                        progressAnim.value < 0.75f -> "Optimizing High-Speed Pipelines..."
                        else -> "Ready..."
                    },
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
