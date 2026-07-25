package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonViolet
import kotlin.math.*

@Composable
fun AnimatedBackgroundView(
    bgId: String,
    speed: String = "Normal",
    intensity: String = "Medium",
    modifier: Modifier = Modifier
) {
    val speedMultiplier = when (speed.lowercase()) {
        "slow" -> 0.4f
        "fast" -> 2.2f
        else -> 1.0f
    }

    val alphaMultiplier = when (intensity.lowercase()) {
        "low" -> 0.25f
        "high" -> 0.85f
        else -> 0.50f
    }

    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundAnim")
    val timeState by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (30000 / speedMultiplier).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "timeFloat"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val t = timeState

            if (width <= 0 || height <= 0) return@Canvas

            val cleanId = bgId.lowercase().replace(" ", "_").trim()

            when {
                cleanId.contains("aurora_lights") || cleanId.contains("aurora") -> drawAuroraLights(width, height, t, alphaMultiplier)
                cleanId.contains("blue_nebula") || cleanId.contains("blue") -> drawBlueNebula(width, height, t, alphaMultiplier)
                cleanId.contains("purple_galaxy") || cleanId.contains("galaxy") || cleanId.contains("purple") -> drawPurpleGalaxy(width, height, t, alphaMultiplier)
                cleanId.contains("cyber_grid") || cleanId.contains("grid") -> drawCyberGrid(width, height, t, alphaMultiplier)
                cleanId.contains("floating_particles") || cleanId.contains("particle") -> drawFloatingParticles(width, height, t, alphaMultiplier)
                cleanId.contains("glass_waves") || cleanId.contains("wave") -> drawGlassWaves(width, height, t, alphaMultiplier)
                cleanId.contains("liquid_gradient") || cleanId.contains("liquid") -> drawLiquidGradient(width, height, t, alphaMultiplier)
                cleanId.contains("neon_rings") || cleanId.contains("ring") -> drawNeonRings(width, height, t, alphaMultiplier)
                cleanId.contains("matrix_rain") || cleanId.contains("matrix") -> drawMatrixRain(width, height, t, alphaMultiplier)
                cleanId.contains("space_stars") || cleanId.contains("star") || cleanId.contains("space") -> drawSpaceStars(width, height, t, alphaMultiplier)
                cleanId.contains("northern_lights") || cleanId.contains("northern") -> drawNorthernLights(width, height, t, alphaMultiplier)
                cleanId.contains("fireflies") || cleanId.contains("firefly") -> drawFireflies(width, height, t, alphaMultiplier)
                cleanId.contains("ocean_glow") || cleanId.contains("ocean") -> drawOceanGlow(width, height, t, alphaMultiplier)
                cleanId.contains("crystal_blur") || cleanId.contains("crystal") -> drawCrystalBlur(width, height, t, alphaMultiplier)
                cleanId.contains("geometric_motion") || cleanId.contains("geometric") -> drawGeometricMotion(width, height, t, alphaMultiplier)
                cleanId.contains("abstract_mesh") || cleanId.contains("mesh") -> drawAbstractMesh(width, height, t, alphaMultiplier)
                cleanId.contains("neural_network") || cleanId.contains("neural") || cleanId.contains("ai_neural") -> drawNeuralNetwork(width, height, t, alphaMultiplier)
                cleanId.contains("floating_bubbles") || cleanId.contains("bubble") -> drawFloatingBubbles(width, height, t, alphaMultiplier)
                cleanId.contains("soft_smoke") || cleanId.contains("smoke") -> drawSoftSmoke(width, height, t, alphaMultiplier)
                cleanId.contains("dynamic_gradient") || cleanId.contains("dynamic") -> drawDynamicGradient(width, height, t, alphaMultiplier)
                else -> drawAuroraLights(width, height, t, alphaMultiplier)
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawAuroraLights(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val phase1 = (t * 0.05f) % (2 * PI.toFloat())
    val phase2 = (t * 0.08f) % (2 * PI.toFloat())

    val path1 = Path().apply {
        moveTo(0f, h * 0.3f)
        cubicTo(
            w * 0.35f, h * (0.2f + 0.15f * sin(phase1)),
            w * 0.65f, h * (0.45f + 0.15f * cos(phase2)),
            w, h * 0.25f
        )
        lineTo(w, h * 0.7f)
        cubicTo(
            w * 0.6f, h * (0.8f + 0.1f * sin(phase2)),
            w * 0.3f, h * (0.6f + 0.1f * cos(phase1)),
            0f, h * 0.75f
        )
        close()
    }

    drawPath(
        path = path1,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF00F2FE).copy(alpha = alpha * 0.45f),
                Color(0xFFA855F7).copy(alpha = alpha * 0.30f),
                Color.Transparent
            )
        )
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawBlueNebula(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val cx = w * (0.5f + 0.15f * sin(t * 0.03f))
    val cy = h * (0.4f + 0.12f * cos(t * 0.02f))
    val radius = max(w, h) * 0.65f

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF00A8FF).copy(alpha = alpha * 0.5f),
                Color(0xFF0044AA).copy(alpha = alpha * 0.25f),
                Color.Transparent
            ),
            center = Offset(cx, cy),
            radius = radius
        ),
        center = Offset(cx, cy),
        radius = radius
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPurpleGalaxy(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val cx = w * 0.5f
    val cy = h * 0.45f

    rotate(degrees = (t * 2f) % 360f, pivot = Offset(cx, cy)) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFA855F7).copy(alpha = alpha * 0.6f),
                    Color(0xFF6366F1).copy(alpha = alpha * 0.25f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = max(w, h) * 0.55f
            ),
            center = Offset(cx, cy),
            radius = max(w, h) * 0.55f
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCyberGrid(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val offsety = (t * 15f) % 50f
    val gridColor = Color(0xFF00F2FE).copy(alpha = alpha * 0.25f)

    // Horizontal grid lines
    var y = offsety
    while (y < h) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 1.2f
        )
        y += 50f
    }

    // Vertical grid lines
    var x = 0f
    while (x < w) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 1.2f
        )
        x += 50f
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFloatingParticles(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val count = 35
    for (i in 0 until count) {
        val speed = 0.8f + (i % 5) * 0.3f
        val x = (w * (0.05f + (i * 0.028f) % 0.9f) + sin(t * 0.05f + i) * 20f)
        val y = (h - ((t * 40f * speed + i * 85f) % h))
        val size = 3f + (i % 4) * 2.5f

        drawCircle(
            color = Color(0xFF00F2FE).copy(alpha = alpha * (0.3f + (i % 3) * 0.2f)),
            center = Offset(x, y),
            radius = size
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGlassWaves(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val wavePath = Path().apply {
        moveTo(0f, h * 0.5f)
        for (x in 0..w.toInt() step 20) {
            val y = h * 0.5f + sin(x * 0.008f + t * 0.05f) * 60f
            lineTo(x.toFloat(), y)
        }
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }

    drawPath(
        path = wavePath,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0x33FFFFFF),
                Color(0x05FFFFFF)
            )
        )
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLiquidGradient(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val c1 = Offset(w * (0.3f + 0.2f * sin(t * 0.04f)), h * (0.3f + 0.2f * cos(t * 0.03f)))
    val c2 = Offset(w * (0.7f - 0.2f * cos(t * 0.03f)), h * (0.7f - 0.2f * sin(t * 0.04f)))

    drawCircle(
        brush = Brush.radialGradient(colors = listOf(Color(0xFF00F2FE).copy(alpha = alpha * 0.4f), Color.Transparent), center = c1, radius = w * 0.5f),
        center = c1,
        radius = w * 0.5f
    )
    drawCircle(
        brush = Brush.radialGradient(colors = listOf(Color(0xFFA855F7).copy(alpha = alpha * 0.4f), Color.Transparent), center = c2, radius = w * 0.5f),
        center = c2,
        radius = w * 0.5f
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawNeonRings(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val center = Offset(w * 0.5f, h * 0.45f)
    for (i in 1..4) {
        val radius = ((t * 30f + i * 120f) % 500f)
        val ringAlpha = (1f - (radius / 500f)) * alpha * 0.6f
        drawCircle(
            color = Color(0xFF00F2FE).copy(alpha = max(0f, ringAlpha)),
            center = center,
            radius = radius,
            style = Stroke(width = 3.5f)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawMatrixRain(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val columns = 16
    val colWidth = w / columns
    for (i in 0 until columns) {
        val speed = 1f + (i % 4) * 0.5f
        val y = (t * 60f * speed + i * 130f) % (h + 200f) - 100f
        val x = i * colWidth + colWidth / 2f

        drawLine(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF10B981).copy(alpha = alpha * 0.8f),
                    Color(0xFF059669).copy(alpha = alpha * 0.2f),
                    Color.Transparent
                ),
                startY = y,
                endY = y + 140f
            ),
            start = Offset(x, y),
            end = Offset(x, y + 140f),
            strokeWidth = 3f
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSpaceStars(
    w: Float, h: Float, t: Float, alpha: Float
) {
    for (i in 0 until 50) {
        val x = (w * ((i * 0.021f) % 1f))
        val y = (h * ((i * 0.037f) % 1f))
        val pulse = (sin(t * 0.1f + i) + 1f) / 2f
        val starAlpha = (0.2f + 0.7f * pulse) * alpha

        drawCircle(
            color = Color.White.copy(alpha = starAlpha),
            center = Offset(x, y),
            radius = 1.5f + (i % 3) * 1.2f
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawNorthernLights(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val path = Path().apply {
        moveTo(0f, h * 0.15f)
        cubicTo(w * 0.3f, h * (0.05f + 0.1f * sin(t * 0.05f)), w * 0.7f, h * (0.25f + 0.1f * cos(t * 0.04f)), w, h * 0.1f)
        lineTo(w, h * 0.6f)
        cubicTo(w * 0.6f, h * 0.7f, w * 0.2f, h * 0.5f, 0f, h * 0.65f)
        close()
    }

    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF10B981).copy(alpha = alpha * 0.5f),
                Color(0xFF00F2FE).copy(alpha = alpha * 0.2f),
                Color.Transparent
            )
        )
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFireflies(
    w: Float, h: Float, t: Float, alpha: Float
) {
    for (i in 0 until 25) {
        val x = w * (0.05f + (i * 0.039f) % 0.9f) + sin(t * 0.04f + i) * 35f
        val y = h * (0.1f + (i * 0.033f) % 0.8f) + cos(t * 0.03f + i) * 35f
        val glow = (sin(t * 0.08f + i * 2) + 1f) / 2f

        drawCircle(
            color = Color(0xFFEAB308).copy(alpha = alpha * (0.3f + 0.6f * glow)),
            center = Offset(x, y),
            radius = 3.5f + glow * 3f
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawOceanGlow(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val c = Offset(w * 0.5f, h * 0.2f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF00F2FE).copy(alpha = alpha * 0.5f), Color(0xFF0284C7).copy(alpha = alpha * 0.2f), Color.Transparent),
            center = c,
            radius = h * 0.7f
        ),
        center = c,
        radius = h * 0.7f
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCrystalBlur(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val center = Offset(w * 0.5f, h * 0.35f)
    rotate(degrees = (t * 1.2f) % 360f, pivot = center) {
        val path = Path().apply {
            moveTo(center.x, center.y - 120f)
            lineTo(center.x + 90f, center.y + 60f)
            lineTo(center.x - 90f, center.y + 60f)
            close()
        }
        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF38BDF8).copy(alpha = alpha * 0.35f), Color(0xFFA855F7).copy(alpha = alpha * 0.2f))
            )
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawGeometricMotion(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val center = Offset(w * 0.5f, h * 0.4f)
    rotate(degrees = (t * 2.5f) % 360f, pivot = center) {
        drawRect(
            color = Color(0xFF00F2FE).copy(alpha = alpha * 0.25f),
            topLeft = Offset(center.x - 80f, center.y - 80f),
            size = Size(160f, 160f),
            style = Stroke(width = 2.5f)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawAbstractMesh(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val points = listOf(
        Offset(w * 0.2f + sin(t * 0.03f) * 30f, h * 0.25f),
        Offset(w * 0.8f + cos(t * 0.04f) * 30f, h * 0.3f),
        Offset(w * 0.5f + sin(t * 0.05f) * 40f, h * 0.6f),
        Offset(w * 0.15f, h * 0.7f),
        Offset(w * 0.85f, h * 0.75f)
    )

    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            drawLine(
                color = Color(0xFF38BDF8).copy(alpha = alpha * 0.2f),
                start = points[i],
                end = points[j],
                strokeWidth = 1.5f
            )
        }
        drawCircle(color = Color(0xFF00F2FE).copy(alpha = alpha * 0.5f), center = points[i], radius = 4f)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawNeuralNetwork(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val nodes = 12
    val coords = List(nodes) { i ->
        Offset(
            w * (0.1f + (i * 0.08f) % 0.85f) + sin(t * 0.04f + i) * 20f,
            h * (0.15f + (i * 0.07f) % 0.7f) + cos(t * 0.03f + i) * 20f
        )
    }

    for (i in 0 until nodes) {
        val n1 = coords[i]
        val n2 = coords[(i + 1) % nodes]
        val signalPos = (t * 0.08f + i) % 1f
        val signalPoint = Offset(n1.x + (n2.x - n1.x) * signalPos, n1.y + (n2.y - n1.y) * signalPos)

        drawLine(color = Color(0xFFA855F7).copy(alpha = alpha * 0.2f), start = n1, end = n2, strokeWidth = 1.2f)
        drawCircle(color = Color(0xFF00F2FE).copy(alpha = alpha * 0.8f), center = signalPoint, radius = 3.5f)
        drawCircle(color = Color(0xFFA855F7).copy(alpha = alpha * 0.5f), center = n1, radius = 4f)
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFloatingBubbles(
    w: Float, h: Float, t: Float, alpha: Float
) {
    for (i in 0 until 18) {
        val speed = 0.6f + (i % 3) * 0.4f
        val x = w * (0.1f + (i * 0.052f) % 0.8f) + sin(t * 0.03f + i) * 25f
        val y = (h - ((t * 30f * speed + i * 110f) % (h + 100f)))
        val radius = 12f + (i % 4) * 8f

        drawCircle(
            color = Color(0xFF00F2FE).copy(alpha = alpha * 0.15f),
            center = Offset(x, y),
            radius = radius
        )
        drawCircle(
            color = Color.White.copy(alpha = alpha * 0.3f),
            center = Offset(x - radius * 0.3f, y - radius * 0.3f),
            radius = radius * 0.25f
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSoftSmoke(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val c1 = Offset(w * (0.4f + 0.15f * sin(t * 0.02f)), h * (0.6f + 0.1f * cos(t * 0.02f)))
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF334155).copy(alpha = alpha * 0.4f), Color.Transparent),
            center = c1,
            radius = max(w, h) * 0.5f
        ),
        center = c1,
        radius = max(w, h) * 0.5f
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDynamicGradient(
    w: Float, h: Float, t: Float, alpha: Float
) {
    val angle = (t * 1.5f) % 360f
    rotate(degrees = angle, pivot = Offset(w * 0.5f, h * 0.5f)) {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF00F2FE).copy(alpha = alpha * 0.3f),
                    Color(0xFFA855F7).copy(alpha = alpha * 0.3f),
                    Color(0xFFEC4899).copy(alpha = alpha * 0.2f)
                )
            ),
            size = Size(w * 1.5f, h * 1.5f),
            topLeft = Offset(-w * 0.25f, -h * 0.25f)
        )
    }
}
