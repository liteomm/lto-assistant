package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.NeonViolet

private data class CardStyleSpec(
    val bgBrush: Brush,
    val borderBrush: Brush,
    val shadowElevation: Dp,
    val borderWidth: Dp
)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cardTheme: String = "Glass Card",
    cardStyle: String = "Glass Card",
    cornerRadius: Dp = 24.dp,
    borderColor: Color = GlassBorder,
    glowColor: Color = CyberCyan.copy(alpha = 0.15f),
    contentPadding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val activeStyle = if (cardStyle != "Glass Card") cardStyle else cardTheme
    val shape = RoundedCornerShape(cornerRadius)

    val spec = when (activeStyle.lowercase().trim()) {
        "neon card", "neon", "neon_card" -> CardStyleSpec(
            Brush.verticalGradient(listOf(Color(0x2800F2FE), Color(0x18A855F7))),
            Brush.linearGradient(listOf(CyberCyan, NeonViolet)),
            12.dp,
            2.dp
        )
        "gradient card", "gradient", "gradient_card" -> CardStyleSpec(
            Brush.linearGradient(listOf(Color(0x4000F2FE), Color(0x40A855F7))),
            Brush.linearGradient(listOf(CyberCyan, NeonViolet)),
            10.dp,
            1.5.dp
        )
        "material 3 card", "material3", "material_3", "material_3_card", "material" -> CardStyleSpec(
            Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B))),
            Brush.linearGradient(listOf(Color(0xFF475569), Color(0xFF334155))),
            4.dp,
            1.dp
        )
        "liquid glass", "liquid_glass", "liquid_glass_card" -> CardStyleSpec(
            Brush.verticalGradient(listOf(Color(0x35FFFFFF), Color(0x10FFFFFF))),
            Brush.linearGradient(listOf(Color.White.copy(alpha = 0.8f), CyberCyan.copy(alpha = 0.5f))),
            14.dp,
            1.5.dp
        )
        "floating card", "floating", "floating_card" -> CardStyleSpec(
            Brush.verticalGradient(listOf(Color(0x2A1E293B), Color(0x1E0F172A))),
            Brush.linearGradient(listOf(Color(0x44FFFFFF), Color(0x22FFFFFF))),
            18.dp,
            1.dp
        )
        "soft shadow card", "soft_shadow", "soft_shadow_card" -> CardStyleSpec(
            Brush.verticalGradient(listOf(Color(0x18FFFFFF), Color(0x0EFFFFFF))),
            Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
            14.dp,
            0.dp
        )
        "minimal card", "minimal", "minimal_card" -> CardStyleSpec(
            Brush.verticalGradient(listOf(Color(0x10FFFFFF), Color(0x10FFFFFF))),
            Brush.linearGradient(listOf(Color(0x33FFFFFF), Color(0x22FFFFFF))),
            2.dp,
            1.dp
        )
        "premium dark", "premium_dark", "premium_dark_card", "premium" -> CardStyleSpec(
            Brush.verticalGradient(listOf(AmoledBlack, Color(0xFF12121A))),
            Brush.linearGradient(listOf(Color(0xFFEAB308), CyberCyan)),
            10.dp,
            1.5.dp
        )
        "colorful study", "colorful_study", "colorful_study_card" -> CardStyleSpec(
            Brush.linearGradient(listOf(Color(0x4010B981), Color(0x4000F2FE))),
            Brush.linearGradient(listOf(Color(0xFF10B981), CyberCyan)),
            8.dp,
            1.5.dp
        )
        "cyber card", "cyber", "cyber_card" -> CardStyleSpec(
            Brush.verticalGradient(listOf(Color(0x3000F2FE), Color(0x1500F2FE))),
            Brush.linearGradient(listOf(CyberCyan, CyberCyan.copy(alpha = 0.4f))),
            10.dp,
            2.dp
        )
        "aurora card", "aurora", "aurora_card" -> CardStyleSpec(
            Brush.linearGradient(listOf(Color(0x3510B981), Color(0x35A855F7))),
            Brush.linearGradient(listOf(Color(0xFF10B981), NeonViolet)),
            12.dp,
            1.5.dp
        )
        else -> CardStyleSpec( // Standard Glass Card
            Brush.verticalGradient(listOf(Color(0x22FFFFFF), Color(0x0EFFFFFF))),
            Brush.linearGradient(listOf(borderColor.copy(alpha = 0.7f), borderColor.copy(alpha = 0.3f))),
            8.dp,
            1.dp
        )
    }

    val baseModifier = modifier
        .shadow(
            elevation = spec.shadowElevation,
            shape = shape,
            ambientColor = glowColor,
            spotColor = glowColor
        )
        .clip(shape)
        .background(brush = spec.bgBrush)
        .border(
            width = spec.borderWidth,
            brush = spec.borderBrush,
            shape = shape
        )

    val finalModifier = if (onClick != null) {
        baseModifier.clickable { onClick() }
    } else {
        baseModifier
    }

    Box(
        modifier = finalModifier.padding(contentPadding),
        content = content
    )
}
