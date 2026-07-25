package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.MessageEntity
import com.example.ui.components.AnimatedBackgroundCanvas
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.util.AIReplyStyleManager
import com.example.util.AppCustomizationManager
import com.example.util.AppLanguageManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileSettingsScreen(
    credits: Int,
    streak: Int,
    tokens: String,
    themeMode: String,
    fontSize: String,
    appLanguage: String,
    performanceMode: String,
    aiReplyStyle: String,
    conversationMemory: Boolean,
    streamingResponse: Boolean,
    markdownFormatting: Boolean,
    autoScroll: Boolean,
    typingAnimation: Boolean,
    hapticFeedback: Boolean,
    soundEffects: Boolean,
    notifications: Boolean,
    currentMessages: List<MessageEntity>,
    // Customization states
    bgAnimation: String,
    bgSpeed: String,
    bgIntensity: String,
    accentColor: String,
    cardTheme: String,
    bubbleStyle: String,
    fontOption: String,
    appIcon: String,
    homeLayout: String,
    borderRadius: String,
    glassBlur: String,
    glassTransparency: String,
    glassGlow: String,
    glassShadow: String,
    glassReflection: String,
    animOpening: String,
    animPage: String,
    animCard: String,
    animButton: String,
    animChat: String,
    animTyping: String,
    animSend: String,
    soundTyping: Boolean,
    soundSend: Boolean,
    soundNotification: Boolean,
    hapticLevel: String,
    lastTranslationLanguage: String = "Hindi",
    autoTranslateAI: Boolean = false,
    // Callbacks
    onThemeChange: (String) -> Unit,
    onFontSizeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onPerformanceChange: (String) -> Unit,
    onAiReplyStyleChange: (String) -> Unit,
    onConversationMemoryChange: (Boolean) -> Unit,
    onStreamingResponseChange: (Boolean) -> Unit,
    onMarkdownFormattingChange: (Boolean) -> Unit,
    onAutoScrollChange: (Boolean) -> Unit,
    onTypingAnimationChange: (Boolean) -> Unit,
    onHapticFeedbackChange: (Boolean) -> Unit,
    onSoundEffectsChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onLastTranslationLanguageChange: (String) -> Unit = {},
    onAutoTranslateAIChange: (Boolean) -> Unit = {},
    onClearHistory: () -> Unit,
    onResetSettings: () -> Unit,
    // Customization callbacks
    onBgAnimationChange: (String) -> Unit,
    onBgSpeedChange: (String) -> Unit,
    onBgIntensityChange: (String) -> Unit,
    onAccentColorChange: (String) -> Unit,
    onCardThemeChange: (String) -> Unit,
    onBubbleStyleChange: (String) -> Unit,
    onFontOptionChange: (String) -> Unit,
    onAppIconChange: (String) -> Unit,
    onHomeLayoutChange: (String) -> Unit,
    onBorderRadiusChange: (String) -> Unit,
    onGlassBlurChange: (String) -> Unit,
    onGlassTransparencyChange: (String) -> Unit,
    onGlassGlowChange: (String) -> Unit,
    onGlassShadowChange: (String) -> Unit,
    onGlassReflectionChange: (String) -> Unit,
    onAnimOpeningChange: (String) -> Unit,
    onAnimPageChange: (String) -> Unit,
    onAnimCardChange: (String) -> Unit,
    onAnimButtonChange: (String) -> Unit,
    onAnimChatChange: (String) -> Unit,
    onAnimTypingChange: (String) -> Unit,
    onAnimSendChange: (String) -> Unit,
    onSoundTypingChange: (Boolean) -> Unit,
    onSoundSendChange: (Boolean) -> Unit,
    onSoundNotificationChange: (Boolean) -> Unit,
    onHapticLevelChange: (String) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showClearDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val strings = AppLanguageManager.getStrings(appLanguage)
    val activeAccent = AppCustomizationManager.getAccentOption(accentColor)

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- STUDENT USER PROFILE CARD ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 28.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.5f),
                cardTheme = cardTheme
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .border(2.dp, Brush.linearGradient(listOf(activeAccent.primaryColor, activeAccent.secondaryColor)), CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1784784425408),
                            contentDescription = "Profile Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = strings.profileTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "LTO Assistant • Pro Academic Tier",
                        fontSize = 12.sp,
                        color = activeAccent.primaryColor
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem(label = strings.aiCredits, value = "$credits Credits", activeAccent.primaryColor)
                        ProfileStatItem(label = strings.studyStreak, value = "$streak Days", WarmGold)
                        ProfileStatItem(label = strings.tokensUsed, value = tokens, NeonPink)
                    }
                }
            }
        }

        // --- THEME MODE SETTING ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DarkMode, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "THEME MODE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Select application visual theme display mode", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val themeModes = listOf("Light Mode", "Dark Mode", "AMOLED Mode", "System Default")
                    val activeThemeMode = when {
                        themeMode.contains("Light", ignoreCase = true) -> "Light Mode"
                        themeMode.contains("Dark", ignoreCase = true) && !themeMode.contains("AMOLED", ignoreCase = true) -> "Dark Mode"
                        themeMode.contains("System", ignoreCase = true) || themeMode.contains("Auto", ignoreCase = true) -> "System Default"
                        else -> "AMOLED Mode"
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        themeModes.forEach { mode ->
                            OptionChip(
                                label = mode,
                                isSelected = mode == activeThemeMode,
                                onClick = { onThemeChange(mode) },
                                activeColor = activeAccent.primaryColor,
                                hapticLevel = hapticLevel
                            )
                        }
                    }
                }
            }
        }

        // --- 1. ANIMATED BACKGROUND GALLERY (20 MODES + LIVE PREVIEW) ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "ANIMATED BACKGROUND GALLERY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "20 60FPS GPU accelerated live canvas backgrounds", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Live Background Preview Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, activeAccent.primaryColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .background(AmoledBlack)
                    ) {
                        AnimatedBackgroundCanvas(bgType = bgAnimation, speed = bgSpeed, intensity = bgIntensity)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(
                                text = "Preview: $bgAnimation ($bgSpeed / $bgIntensity)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier
                                    .background(GlassSurface.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 20 Background Selector Chips
                    val bgList = AppCustomizationManager.backgrounds
                    val matchedBg = bgList.find { it.name.equals(bgAnimation, ignoreCase = true) || it.id.equals(bgAnimation, ignoreCase = true) } ?: bgList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        bgList.forEach { bg ->
                            OptionChip(
                                label = bg.name,
                                isSelected = bg == matchedBg,
                                onClick = { onBgAnimationChange(bg.name) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Speed & Intensity Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Animation Speed", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            val speedList = listOf("Slow", "Normal", "Fast")
                            val matchedSpeed = speedList.find { it.equals(bgSpeed, ignoreCase = true) } ?: "Normal"
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                speedList.forEach { sp ->
                                    OptionChip(
                                        label = sp,
                                        isSelected = sp == matchedSpeed,
                                        onClick = { onBgSpeedChange(sp) },
                                        activeColor = activeAccent.primaryColor
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Intensity", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            val intensityList = listOf("Low", "Medium", "High")
                            val matchedIntensity = intensityList.find { it.equals(bgIntensity, ignoreCase = true) } ?: "Medium"
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                intensityList.forEach { it ->
                                    OptionChip(
                                        label = it,
                                        isSelected = it == matchedIntensity,
                                        onClick = { onBgIntensityChange(it) },
                                        activeColor = activeAccent.primaryColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 2. CUSTOM ACCENT COLORS (10 COLORS) ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ColorLens, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "CUSTOM ACCENT COLOR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Changes app highlights, glowing borders, and buttons", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val accentList = AppCustomizationManager.accentColors
                    val matchedAccentOption = accentList.find { it.name.equals(accentColor, ignoreCase = true) || it.id.equals(accentColor, ignoreCase = true) } ?: accentList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        accentList.forEach { acc ->
                            val isSelected = acc == matchedAccentOption
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) acc.primaryColor.copy(alpha = 0.3f) else GlassSurface)
                                    .border(if (isSelected) 2.dp else 1.dp, if (isSelected) acc.primaryColor else GlassBorder, RoundedCornerShape(12.dp))
                                    .clickable { onAccentColorChange(acc.name) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(acc.primaryColor)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = acc.name,
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

        // --- 3. SUGGESTION CARD THEMES (12 STYLES) ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Dashboard, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "SUGGESTION CARD THEME", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Select 1 of 12 custom card visual styles", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val cardList = AppCustomizationManager.cardThemes
                    val matchedCard = cardList.find { it.name.equals(cardTheme, ignoreCase = true) || it.id.equals(cardTheme, ignoreCase = true) } ?: cardList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        cardList.forEach { card ->
                            OptionChip(
                                label = card.name,
                                isSelected = card == matchedCard,
                                onClick = { onCardThemeChange(card.name) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // --- 4. CHAT BUBBLE STYLES (10 STYLES) ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.QuestionAnswer, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "CHAT BUBBLE STYLES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Custom message bubble shapes and borders", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val bubbleList = AppCustomizationManager.bubbleStyles
                    val matchedBubble = bubbleList.find { it.name.equals(bubbleStyle, ignoreCase = true) || it.id.equals(bubbleStyle, ignoreCase = true) } ?: bubbleList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        bubbleList.forEach { bubble ->
                            OptionChip(
                                label = bubble.name,
                                isSelected = bubble == matchedBubble,
                                onClick = { onBubbleStyleChange(bubble.name) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // --- 5. FONT OPTIONS (10 TYPOGRAPHY OPTIONS) ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TextFormat, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "FONT TYPOGRAPHY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "10 fonts supporting English, Hindi, and Odia", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val fontList = AppCustomizationManager.fontOptions
                    val matchedFont = fontList.find { it.name.equals(fontOption, ignoreCase = true) || it.id.equals(fontOption, ignoreCase = true) } ?: fontList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fontList.forEach { font ->
                            OptionChip(
                                label = font.name,
                                isSelected = font == matchedFont,
                                onClick = { onFontOptionChange(font.name) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // --- APP LANGUAGE SETTING ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Translate, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "APP LANGUAGE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Select application interface language", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val appLanguages = com.example.util.AppLanguageManager.supportedLanguages
                    val matchedAppLang = appLanguages.find { it.equals(appLanguage, ignoreCase = true) || it.contains(appLanguage, ignoreCase = true) } ?: appLanguages.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        appLanguages.forEach { lang ->
                            OptionChip(
                                label = lang,
                                isSelected = lang == matchedAppLang,
                                onClick = { onLanguageChange(lang) },
                                activeColor = activeAccent.primaryColor,
                                hapticLevel = hapticLevel
                            )
                        }
                    }
                }
            }
        }

        // --- FONT SIZE SETTING ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.FormatSize, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "FONT SIZE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Adjust text scale across the application", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val fontSizes = listOf("Small", "Normal", "Large", "Extra Large")
                    val matchedFontSize = fontSizes.find { it.equals(fontSize, ignoreCase = true) } ?: "Normal"

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fontSizes.forEach { size ->
                            OptionChip(
                                label = size,
                                isSelected = size == matchedFontSize,
                                onClick = { onFontSizeChange(size) },
                                activeColor = activeAccent.primaryColor,
                                hapticLevel = hapticLevel
                            )
                        }
                    }
                }
            }
        }

        // --- PERFORMANCE MODE SETTING ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "PERFORMANCE MODE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Optimize graphics, frame rate, and battery usage", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val perfModes = listOf("Performance", "Balanced", "Battery Saver")
                    val matchedPerfMode = perfModes.find { it.equals(performanceMode, ignoreCase = true) } ?: "Performance"

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        perfModes.forEach { mode ->
                            OptionChip(
                                label = mode,
                                isSelected = mode == matchedPerfMode,
                                onClick = { onPerformanceChange(mode) },
                                activeColor = activeAccent.primaryColor,
                                hapticLevel = hapticLevel
                            )
                        }
                    }
                }
            }
        }

        // --- 6. APP ICONS (10 BRAND ICONS) ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Category, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "APP ICON BRANDING", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Switch between 10 premium app emblem styles", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val iconList = AppCustomizationManager.appIcons
                    val matchedIcon = iconList.find { it.name.equals(appIcon, ignoreCase = true) || it.id.equals(appIcon, ignoreCase = true) } ?: iconList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        iconList.forEach { icon ->
                            OptionChip(
                                label = icon.name,
                                isSelected = icon == matchedIcon,
                                onClick = { onAppIconChange(icon.name) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // --- 7. HOME SCREEN LAYOUTS (5 LAYOUTS) ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ViewQuilt, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "HOME SCREEN LAYOUT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Choose your preferred study dashboard arrangement", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val layoutList = AppCustomizationManager.homeLayouts
                    val matchedLayout = layoutList.find { it.name.equals(homeLayout, ignoreCase = true) || it.id.equals(homeLayout, ignoreCase = true) } ?: layoutList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        layoutList.forEach { layout ->
                            OptionChip(
                                label = layout.name,
                                isSelected = layout == matchedLayout,
                                onClick = { onHomeLayoutChange(layout.name) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // --- 8. GLASS & BORDER TUNING ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "GLASS & BORDER CORNER TUNING", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Fine-tune corner radius, glow, and transparency", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Border Radius", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    val radList = AppCustomizationManager.borderRadii
                    val matchedRad = radList.find { it.equals(borderRadius, ignoreCase = true) } ?: "Medium"
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        radList.forEach { rad ->
                            OptionChip(
                                label = rad,
                                isSelected = rad == matchedRad,
                                onClick = { onBorderRadiusChange(rad) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Glass Glow Intensity", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    val glowList = listOf("Off", "Soft", "Vibrant", "Intense")
                    val matchedGlow = glowList.find { it.equals(glassGlow, ignoreCase = true) } ?: "Soft"
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        glowList.forEach { glow ->
                            OptionChip(
                                label = glow,
                                isSelected = glow == matchedGlow,
                                onClick = { onGlassGlowChange(glow) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // --- 9. ANIMATIONS CUSTOMIZATION ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Animation, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "ANIMATION & MOTION CUSTOMIZER", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Customize transition modes across screens and buttons", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Opening Animation
                    Column {
                        Text("Opening Animation", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                        val openList = listOf("Fade", "Slide", "Zoom", "Scale")
                        val matchedOpen = openList.find { it.equals(animOpening, ignoreCase = true) } ?: "Fade"
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 4.dp)) {
                            openList.forEach { anim ->
                                OptionChip(label = anim, isSelected = anim == matchedOpen, onClick = { onAnimOpeningChange(anim) }, activeColor = activeAccent.primaryColor)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chat Animation
                    Column {
                        Text("Chat Message Entry Animation", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                        val chatAnimList = listOf("Slide Up Fade", "Spring Pop", "Staggered")
                        val matchedChatAnim = chatAnimList.find { it.equals(animChat, ignoreCase = true) } ?: "Slide Up Fade"
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 4.dp)) {
                            chatAnimList.forEach { anim ->
                                OptionChip(label = anim, isSelected = anim == matchedChatAnim, onClick = { onAnimChatChange(anim) }, activeColor = activeAccent.primaryColor)
                            }
                        }
                    }
                }
            }
        }

        // --- 10. SOUND & HAPTIC FEEDBACK ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "SOUND & HAPTICS CONTROL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Real-time sound synthesis & tactile feedback", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingToggleRow("Typing Sound Effect", "Play soft click sound while AI responds", soundTyping, activeAccent.primaryColor) { onSoundTypingChange(it) }
                    SettingToggleRow("Send Message Sound", "Play audio chime upon sending queries", soundSend, activeAccent.primaryColor) { onSoundSendChange(it) }
                    SettingToggleRow("Notification Alert Sound", "Play sound on key AI response completion", soundNotification, activeAccent.primaryColor) { onSoundNotificationChange(it) }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Haptic Feedback Intensity", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    val hapticList = listOf("OFF", "Light", "Medium", "Strong")
                    val matchedHaptic = hapticList.find { it.equals(hapticLevel, ignoreCase = true) } ?: "Medium"
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        hapticList.forEach { lvl ->
                            OptionChip(
                                label = lvl,
                                isSelected = lvl == matchedHaptic,
                                onClick = { onHapticLevelChange(lvl) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // --- 11. AI REPLY STYLE SETTING (10 HUMAN-LIKE MODES) ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.4f),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "AI REPLY STYLE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Changes AI tone, depth, and response structure", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val stylesList = AIReplyStyleManager.styles
                    val cleanStyleQuery = aiReplyStyle.replace(Regex("[^a-zA-Z0-9 -]"), "").trim()
                    val matchedStyle = stylesList.find {
                        it.name.equals(aiReplyStyle, ignoreCase = true) ||
                        it.id.equals(aiReplyStyle, ignoreCase = true) ||
                        it.name.equals(cleanStyleQuery, ignoreCase = true)
                    } ?: stylesList.find { it.name.equals("Teacher", ignoreCase = true) } ?: stylesList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        stylesList.forEach { style ->
                            OptionChip(
                                label = style.name,
                                isSelected = style == matchedStyle,
                                onClick = { onAiReplyStyleChange(style.name) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val activeStyle = AIReplyStyleManager.getStyleByName(aiReplyStyle)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(GlassSurface)
                            .border(1.dp, activeAccent.primaryColor.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = activeStyle.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = activeStyle.shortDesc, fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }

        // --- TRANSLATION SETTINGS ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "TRANSLATION SETTINGS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                            Text(text = "Auto-translate responses and default Indian language preference", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingToggleRow(
                        title = "Auto Translate AI Responses",
                        subtitle = "When enabled, AI answers automatically appear in selected language",
                        checked = autoTranslateAI,
                        activeColor = activeAccent.primaryColor,
                        onCheckedChange = onAutoTranslateAIChange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Default Translation Language", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))

                    val langList = com.example.ui.components.quickAccessLanguages
                    val matchedLang = langList.find { it.name.equals(lastTranslationLanguage, ignoreCase = true) || it.nativeScript.equals(lastTranslationLanguage, ignoreCase = true) } ?: langList.first()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        langList.forEach { lang ->
                            OptionChip(
                                label = lang.nativeScript,
                                isSelected = lang == matchedLang,
                                onClick = { onLastTranslationLanguageChange(lang.name) },
                                activeColor = activeAccent.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // --- 12. SYSTEM PREFERENCES & DATA ACTIONS ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cardTheme = cardTheme
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "SYSTEM PREFERENCES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingToggleRow(strings.conversationMemoryLabel, strings.conversationMemorySub, conversationMemory, activeAccent.primaryColor) { onConversationMemoryChange(it) }
                    SettingToggleRow(strings.streamingResponseLabel, strings.streamingResponseSub, streamingResponse, activeAccent.primaryColor) { onStreamingResponseChange(it) }
                    SettingToggleRow(strings.markdownFormattingLabel, strings.markdownFormattingSub, markdownFormatting, activeAccent.primaryColor) { onMarkdownFormattingChange(it) }
                    SettingToggleRow(strings.autoScrollLabel, strings.autoScrollSub, autoScroll, activeAccent.primaryColor) { onAutoScrollChange(it) }
                    SettingToggleRow(strings.hapticFeedbackLabel, strings.hapticFeedbackSub, hapticFeedback, activeAccent.primaryColor) { onHapticFeedbackChange(it) }
                    SettingToggleRow(strings.notificationsLabel, strings.notificationsSub, notifications, activeAccent.primaryColor) { onNotificationsChange(it) }
                }
            }
        }

        // --- 13. DATA ACTIONS ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cardTheme = cardTheme
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showClearDialog = true }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, tint = NeonPink)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = strings.clearHistoryLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                            Text(text = strings.clearHistorySub, fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showResetDialog = true }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Restore, contentDescription = null, tint = WarmGold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = strings.resetSettingsLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WarmGold)
                            Text(text = strings.resetSettingsSub, fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            }
        }

        // --- 14. ABOUT PAGE SECTION ---
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                borderColor = activeAccent.primaryColor.copy(alpha = 0.5f),
                glowColor = NeonViolet.copy(alpha = 0.2f),
                cardTheme = cardTheme
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = activeAccent.primaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "ABOUT LTO ASSISTANT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor, letterSpacing = 1.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Brush.linearGradient(listOf(activeAccent.primaryColor.copy(alpha = 0.15f), NeonViolet.copy(alpha = 0.2f))))
                            .border(1.dp, Brush.linearGradient(listOf(activeAccent.primaryColor, NeonViolet)), RoundedCornerShape(18.dp))
                            .padding(vertical = 18.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "LTO Assistant", fontSize = 22.sp, fontWeight = FontWeight.Black, color = TextPrimary, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Your Smart AI Study Companion", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = activeAccent.primaryColor)
                            Text(text = "Powered by LiteOMM", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "App Name", fontSize = 12.sp, color = TextMuted)
                            Text(text = "LTO Assistant", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Developer", fontSize = 12.sp, color = TextMuted)
                            Text(text = "LiteOMM", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = activeAccent.primaryColor)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Category", fontSize = 12.sp, color = TextMuted)
                            Text(text = "AI Student Assistant", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(90.dp)) }
    }

    // Clear History Dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(strings.clearDialogTitle, color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text(strings.clearDialogText, color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearHistory()
                        showClearDialog = false
                    }
                ) {
                    Text(strings.confirm, color = NeonPink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(strings.cancel, color = TextMuted)
                }
            },
            containerColor = AmoledBlack,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Reset Settings Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(strings.resetDialogTitle, color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text(strings.resetDialogText, color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onResetSettings()
                        showResetDialog = false
                    }
                ) {
                    Text(strings.confirm, color = WarmGold, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(strings.cancel, color = TextMuted)
                }
            },
            containerColor = AmoledBlack,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Black, color = valueColor)
        Text(text = label, fontSize = 11.sp, color = TextMuted)
    }
}

@Composable
private fun OptionChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    activeColor: Color = CyberCyan,
    hapticLevel: String = "Medium"
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) activeColor.copy(alpha = 0.25f) else GlassSurface)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) activeColor else GlassBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                com.example.util.HapticFeedbackManager.performHaptic(context, hapticLevel)
                onClick()
            }
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
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    activeColor: Color = CyberCyan,
    hapticLevel: String = "Medium",
    onCheckedChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
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
            onCheckedChange = { newValue ->
                com.example.util.HapticFeedbackManager.performHaptic(context, hapticLevel)
                onCheckedChange(newValue)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = activeColor,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = GlassSurface
            )
        )
    }
}
