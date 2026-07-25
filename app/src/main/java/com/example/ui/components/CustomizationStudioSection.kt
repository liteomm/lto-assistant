package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.util.AppCustomizationManager
import com.example.util.HapticFeedbackManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomizationStudioSection(
    backgroundAnim: String,
    animSpeed: String,
    animIntensity: String,
    cardTheme: String,
    accentColor: String,
    bubbleStyle: String,
    fontOption: String,
    appIcon: String,
    homeLayout: String,
    borderRadius: String,
    glassBlur: String,
    glassTransparency: String,
    glassGlow: String,
    glassReflection: String,
    soundTyping: Boolean,
    soundSend: Boolean,
    soundNotification: Boolean,
    hapticLevel: String,
    animOpening: String,
    animPage: String,
    animCard: String,
    animSend: String,
    onBgAnimChange: (String) -> Unit,
    onAnimSpeedChange: (String) -> Unit,
    onAnimIntensityChange: (String) -> Unit,
    onCardThemeChange: (String) -> Unit,
    onAccentColorChange: (String) -> Unit,
    onBubbleStyleChange: (String) -> Unit,
    onFontOptionChange: (String) -> Unit,
    onAppIconChange: (String) -> Unit,
    onHomeLayoutChange: (String) -> Unit,
    onBorderRadiusChange: (String) -> Unit,
    onGlassBlurChange: (String) -> Unit,
    onGlassTransparencyChange: (String) -> Unit,
    onGlassGlowChange: (String) -> Unit,
    onGlassReflectionChange: (String) -> Unit,
    onSoundTypingChange: (Boolean) -> Unit,
    onSoundSendChange: (Boolean) -> Unit,
    onSoundNotifChange: (Boolean) -> Unit,
    onHapticLevelChange: (String) -> Unit,
    onAnimOpeningChange: (String) -> Unit,
    onAnimPageChange: (String) -> Unit,
    onAnimCardChange: (String) -> Unit,
    onAnimSendChange: (String) -> Unit
) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf("Backgrounds") }

    val activeAccentOption = AppCustomizationManager.getAccentOption(accentColor)
    val currentAccentColor = activeAccentOption.primaryColor

    Column(modifier = Modifier.fillMaxWidth()) {
        // --- STUDIO HEADER BANNER ---
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cardTheme = cardTheme,
            borderColor = currentAccentColor.copy(alpha = 0.5f),
            glowColor = currentAccentColor.copy(alpha = 0.2f)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = null,
                        tint = currentAccentColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "PREMIUM CUSTOMIZATION STUDIO",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = currentAccentColor,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Personalize every visual, motion, sound & touch detail",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Studio SubTabs Carousel
                val studioTabs = listOf(
                    "Backgrounds" to Icons.Default.AutoAwesome,
                    "Card Themes" to Icons.Default.Layers,
                    "Accent Colors" to Icons.Default.ColorLens,
                    "Chat Bubbles" to Icons.Default.ChatBubble,
                    "Fonts & Icons" to Icons.Default.TextFields,
                    "Layout & Glass" to Icons.Default.GridView,
                    "Audio & Touch" to Icons.Default.VolumeUp
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(studioTabs) { (title, icon) ->
                        val isSelected = activeSubTab == title
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) currentAccentColor.copy(alpha = 0.25f) else Color(0x12FFFFFF)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) currentAccentColor else Color(0x22FFFFFF),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    HapticFeedbackManager.performHaptic(context, hapticLevel)
                                    activeSubTab = title
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) currentAccentColor else TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) TextPrimary else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SUBTAB CONTENT PANELS ---
        when (activeSubTab) {
            "Backgrounds" -> {
                GlassCard(modifier = Modifier.fillMaxWidth(), cardTheme = cardTheme) {
                    Column {
                        Text(
                            text = "ANIMATED BACKGROUND GALLERY (20 STYLES)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentAccentColor
                        )
                        Text(
                            text = "Select any background to preview and apply it instantly in 60FPS",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Clean Background Options Flow Grid (NO EMOJIS)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppCustomizationManager.backgroundOptions.forEach { bg ->
                                val isSelected = backgroundAnim.equals(bg.name, ignoreCase = true) || backgroundAnim.contains(bg.id)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) currentAccentColor.copy(alpha = 0.25f) else GlassSurface)
                                        .border(1.dp, if (isSelected) currentAccentColor else GlassBorder, RoundedCornerShape(12.dp))
                                        .clickable {
                                            HapticFeedbackManager.performHaptic(context, hapticLevel)
                                            onBgAnimChange(bg.name)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = bg.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = GlassBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Animation Speed Controls
                        Text(
                            text = "ANIMATION SPEED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Slow", "Normal", "Fast").forEach { speed ->
                                StudioChip(
                                    label = speed,
                                    isSelected = animSpeed.equals(speed, ignoreCase = true),
                                    activeColor = currentAccentColor,
                                    onClick = { onAnimSpeedChange(speed) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Animation Intensity Controls
                        Text(
                            text = "ANIMATION INTENSITY & PARTICLE DENSITY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Low", "Medium", "High").forEach { intensity ->
                                StudioChip(
                                    label = intensity,
                                    isSelected = animIntensity.equals(intensity, ignoreCase = true),
                                    activeColor = currentAccentColor,
                                    onClick = { onAnimIntensityChange(intensity) }
                                )
                            }
                        }
                    }
                }
            }

            "Card Themes" -> {
                GlassCard(modifier = Modifier.fillMaxWidth(), cardTheme = cardTheme) {
                    Column {
                        Text(
                            text = "SUGGESTION & CARD STYLES (12 THEMES)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentAccentColor
                        )
                        Text(
                            text = "Updates all container cards, dialogs, and suggestion panels instantly",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppCustomizationManager.cardStyles.forEach { card ->
                                val isSelected = cardTheme.equals(card.name, ignoreCase = true) || cardTheme.contains(card.id)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) currentAccentColor.copy(alpha = 0.25f) else GlassSurface)
                                        .border(1.dp, if (isSelected) currentAccentColor else GlassBorder, RoundedCornerShape(12.dp))
                                        .clickable {
                                            HapticFeedbackManager.performHaptic(context, hapticLevel)
                                            onCardThemeChange(card.name)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = card.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            "Accent Colors" -> {
                GlassCard(modifier = Modifier.fillMaxWidth(), cardTheme = cardTheme) {
                    Column {
                        Text(
                            text = "CUSTOM ACCENT COLOR PALETTES",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentAccentColor
                        )
                        Text(
                            text = "Changes primary brand color, icons, highlights and active toggles",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppCustomizationManager.accentColors.forEach { accent ->
                                val isSelected = accentColor.equals(accent.name, ignoreCase = true) || accentColor.equals(accent.id, ignoreCase = true)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSelected) accent.primaryColor.copy(alpha = 0.25f) else GlassSurface)
                                        .border(1.dp, if (isSelected) accent.primaryColor else GlassBorder, RoundedCornerShape(14.dp))
                                        .clickable {
                                            HapticFeedbackManager.performHaptic(context, hapticLevel)
                                            onAccentColorChange(accent.name)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(accent.primaryColor)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = accent.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }

            "Chat Bubbles" -> {
                GlassCard(modifier = Modifier.fillMaxWidth(), cardTheme = cardTheme) {
                    Column {
                        Text(
                            text = "CHAT BUBBLE STYLES & RADII",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentAccentColor
                        )
                        Text(
                            text = "Customize message bubble contours, borders, and corner radii",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppCustomizationManager.chatBubbleStyles.forEach { bubble ->
                                val isSelected = bubbleStyle.equals(bubble.name, ignoreCase = true) || bubbleStyle.contains(bubble.id)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) currentAccentColor.copy(alpha = 0.25f) else GlassSurface)
                                        .border(1.dp, if (isSelected) currentAccentColor else GlassBorder, RoundedCornerShape(12.dp))
                                        .clickable {
                                            HapticFeedbackManager.performHaptic(context, hapticLevel)
                                            onBubbleStyleChange(bubble.name)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = bubble.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = GlassBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "BUBBLE CORNER RADIUS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Small", "Medium", "Large", "Extra Large").forEach { radius ->
                                StudioChip(
                                    label = radius,
                                    isSelected = borderRadius.equals(radius, ignoreCase = true),
                                    activeColor = currentAccentColor,
                                    onClick = { onBorderRadiusChange(radius) }
                                )
                            }
                        }
                    }
                }
            }

            "Fonts & Icons" -> {
                GlassCard(modifier = Modifier.fillMaxWidth(), cardTheme = cardTheme) {
                    Column {
                        Text(
                            text = "TYPOGRAPHY & APP ICONS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentAccentColor
                        )
                        Text(
                            text = "Select custom font family & active launcher emblem",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Text(
                            text = "FONT TYPOGRAPHY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppCustomizationManager.fontOptions.forEach { font ->
                                val isSelected = fontOption.equals(font.name, ignoreCase = true) || fontOption.contains(font.id)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) currentAccentColor.copy(alpha = 0.25f) else GlassSurface)
                                        .border(1.dp, if (isSelected) currentAccentColor else GlassBorder, RoundedCornerShape(12.dp))
                                        .clickable {
                                            HapticFeedbackManager.performHaptic(context, hapticLevel)
                                            onFontOptionChange(font.name)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = font.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = GlassBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "APP ICON EMBLEM",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppCustomizationManager.appIcons.forEach { icon ->
                                val isSelected = appIcon.equals(icon.name, ignoreCase = true) || appIcon.contains(icon.id)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) currentAccentColor.copy(alpha = 0.25f) else GlassSurface)
                                        .border(1.dp, if (isSelected) currentAccentColor else GlassBorder, RoundedCornerShape(12.dp))
                                        .clickable {
                                            HapticFeedbackManager.performHaptic(context, hapticLevel)
                                            onAppIconChange(icon.name)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = icon.emoji, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = icon.name,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) TextPrimary else TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "Layout & Glass" -> {
                GlassCard(modifier = Modifier.fillMaxWidth(), cardTheme = cardTheme) {
                    Column {
                        Text(
                            text = "HOME LAYOUT & FROSTED GLASS EFFECTS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentAccentColor
                        )
                        Text(
                            text = "Fine-tune UI density, blur, glow, transparency, and glass reflections",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Text(
                            text = "HOME SCREEN LAYOUT DENSITY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppCustomizationManager.homeLayouts.forEach { layout ->
                                val isSelected = homeLayout.equals(layout.name, ignoreCase = true) || homeLayout.contains(layout.id)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) currentAccentColor.copy(alpha = 0.25f) else GlassSurface)
                                        .border(1.dp, if (isSelected) currentAccentColor else GlassBorder, RoundedCornerShape(12.dp))
                                        .clickable {
                                            HapticFeedbackManager.performHaptic(context, hapticLevel)
                                            onHomeLayoutChange(layout.name)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = layout.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextPrimary else TextMuted
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Glass Blur Level
                        Text(
                            text = "GLASS BLUR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Off", "Low", "Medium", "High").forEach { b ->
                                StudioChip(
                                    label = b,
                                    isSelected = glassBlur.equals(b, ignoreCase = true),
                                    activeColor = currentAccentColor,
                                    onClick = { onGlassBlurChange(b) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Glass Glow Intensity
                        Text(
                            text = "GLOW INTENSITY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Off", "Soft", "Vibrant", "Intense").forEach { g ->
                                StudioChip(
                                    label = g,
                                    isSelected = glassGlow.equals(g, ignoreCase = true),
                                    activeColor = currentAccentColor,
                                    onClick = { onGlassGlowChange(g) }
                                )
                            }
                        }
                    }
                }
            }

            "Audio & Touch" -> {
                GlassCard(modifier = Modifier.fillMaxWidth(), cardTheme = cardTheme) {
                    Column {
                        Text(
                            text = "AUDIO FEEDBACK & HAPTIC TOUCH",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentAccentColor
                        )
                        Text(
                            text = "Configure typing chimes, send sounds, and tactile vibration intensity",
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        StudioToggleRow(
                            title = "Typing Sound",
                            subtitle = "Soft audio feedback on word generation",
                            checked = soundTyping,
                            activeColor = currentAccentColor,
                            onCheckedChange = onSoundTypingChange
                        )

                        StudioToggleRow(
                            title = "Send Message Sound",
                            subtitle = "Audio effect when sending prompt",
                            checked = soundSend,
                            activeColor = currentAccentColor,
                            onCheckedChange = onSoundSendChange
                        )

                        StudioToggleRow(
                            title = "Notification Chime",
                            subtitle = "Sound when AI finishes streaming answer",
                            checked = soundNotification,
                            activeColor = currentAccentColor,
                            onCheckedChange = onSoundNotifChange
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "HAPTIC VIBRATION INTENSITY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Off", "Light", "Medium", "Strong").forEach { lvl ->
                                StudioChip(
                                    label = lvl,
                                    isSelected = hapticLevel.equals(lvl, ignoreCase = true),
                                    activeColor = currentAccentColor,
                                    onClick = {
                                        HapticFeedbackManager.performHaptic(context, lvl)
                                        onHapticLevelChange(lvl)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StudioChip(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) activeColor.copy(alpha = 0.25f) else GlassSurface)
            .border(1.dp, if (isSelected) activeColor else GlassBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) TextPrimary else TextMuted
        )
    }
}

@Composable
private fun StudioToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    activeColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            Text(text = subtitle, fontSize = 10.sp, color = TextMuted)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = activeColor,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = GlassSurface
            )
        )
    }
}
