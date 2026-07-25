package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.util.AppCustomizationManager

@Composable
fun LTOAssistantTheme(
    themeMode: String = "AMOLED Black",
    accentColorName: String = "Cyber Cyan",
    fontOption: String = "Default Sans",
    fontSizeOption: String = "Normal",
    content: @Composable () -> Unit
) {
    val accent = AppCustomizationManager.getAccentOption(accentColorName)
    val primaryColor = accent.primaryColor
    val secondaryColor = accent.secondaryColor

    val isLight = themeMode.equals("Light Mode", ignoreCase = true)

    val colorScheme = if (isLight) {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = Color.White,
            primaryContainer = Color(0xFFE2E8F0),
            onPrimaryContainer = primaryColor,
            secondary = secondaryColor,
            onSecondary = Color.White,
            background = Color(0xFFF8FAFC),
            onBackground = Color(0xFF0F172A),
            surface = Color.White,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFF1F5F9),
            onSurfaceVariant = Color(0xFF475569),
            outline = Color(0xFFCBD5E1)
        )
    } else {
        val bg = if (themeMode.equals("Dark Mode", ignoreCase = true)) Color(0xFF0F172A) else AmoledBlack
        darkColorScheme(
            primary = primaryColor,
            onPrimary = Color.Black,
            primaryContainer = GlassSurfaceVariant,
            onPrimaryContainer = primaryColor,
            secondary = secondaryColor,
            onSecondary = Color.White,
            secondaryContainer = GlassSurface,
            onSecondaryContainer = secondaryColor,
            tertiary = ElectricBlue,
            background = bg,
            onBackground = TextPrimary,
            surface = AmoledDarkBackground,
            onSurface = TextPrimary,
            surfaceVariant = GlassSurface,
            onSurfaceVariant = TextSecondary,
            outline = GlassBorder,
            outlineVariant = CardGlowBorder
        )
    }

    val selectedFontFamily = when {
        fontOption.contains("Playfair", ignoreCase = true) || fontOption.contains("Cinzel", ignoreCase = true) -> FontFamily.Serif
        fontOption.contains("Fira", ignoreCase = true) || fontOption.contains("Code", ignoreCase = true) -> FontFamily.Monospace
        fontOption.contains("Space", ignoreCase = true) -> FontFamily.Cursive
        else -> FontFamily.SansSerif
    }

    val scaleFactor = when (fontSizeOption.lowercase()) {
        "small" -> 0.88f
        "large" -> 1.15f
        "extra large" -> 1.28f
        else -> 1.0f
    }

    val customTypography = Typography(
        bodyLarge = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * scaleFactor).sp,
            lineHeight = (24 * scaleFactor).sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * scaleFactor).sp,
            lineHeight = (20 * scaleFactor).sp
        ),
        bodySmall = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (12 * scaleFactor).sp,
            lineHeight = (16 * scaleFactor).sp
        ),
        titleLarge = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (22 * scaleFactor).sp,
            lineHeight = (28 * scaleFactor).sp
        ),
        titleMedium = TextStyle(
            fontFamily = selectedFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (18 * scaleFactor).sp,
            lineHeight = (24 * scaleFactor).sp
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = customTypography,
        content = content
    )
}

@Composable
fun SmartRouterTheme(
    content: @Composable () -> Unit
) {
    LTOAssistantTheme(content = content)
}
