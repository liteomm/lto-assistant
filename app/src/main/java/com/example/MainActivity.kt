package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AnimatedBackgroundCanvas
import com.example.ui.components.GlassBottomNavBar
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.LTOAssistantTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LTOAssistantApp()
        }
    }
}

@Composable
fun LTOAssistantApp(viewModel: MainViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val chats by viewModel.chats.collectAsStateWithLifecycle()
    val activeChatId by viewModel.activeChatId.collectAsStateWithLifecycle()
    val currentMessages by viewModel.currentMessages.collectAsStateWithLifecycle()
    val isRouting by viewModel.isRouting.collectAsStateWithLifecycle()
    val isStreaming by viewModel.isStreaming.collectAsStateWithLifecycle()
    val currentDecision by viewModel.currentDecision.collectAsStateWithLifecycle()
    val streamingText by viewModel.streamingText.collectAsStateWithLifecycle()
    val selectedModel by viewModel.selectedModel.collectAsStateWithLifecycle()

    val attachmentName by viewModel.attachmentName.collectAsStateWithLifecycle()
    val attachmentType by viewModel.attachmentType.collectAsStateWithLifecycle()
    val attachmentUri by viewModel.attachmentUri.collectAsStateWithLifecycle()

    // Persistent Settings
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val fontSize by viewModel.fontSize.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val performanceMode by viewModel.performanceMode.collectAsStateWithLifecycle()

    val conversationMemory by viewModel.conversationMemory.collectAsStateWithLifecycle()
    val streamingResponse by viewModel.streamingResponse.collectAsStateWithLifecycle()
    val markdownFormatting by viewModel.markdownFormatting.collectAsStateWithLifecycle()
    val autoScroll by viewModel.autoScroll.collectAsStateWithLifecycle()
    val typingAnimation by viewModel.typingAnimation.collectAsStateWithLifecycle()
    val hapticFeedback by viewModel.hapticFeedback.collectAsStateWithLifecycle()
    val soundEffects by viewModel.soundEffects.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val aiReplyStyle by viewModel.aiReplyStyle.collectAsStateWithLifecycle()

    // Customization states
    val bgAnimation by viewModel.bgAnimation.collectAsStateWithLifecycle()
    val bgSpeed by viewModel.bgSpeed.collectAsStateWithLifecycle()
    val bgIntensity by viewModel.bgIntensity.collectAsStateWithLifecycle()
    val accentColor by viewModel.accentColor.collectAsStateWithLifecycle()
    val cardTheme by viewModel.cardTheme.collectAsStateWithLifecycle()
    val bubbleStyle by viewModel.bubbleStyle.collectAsStateWithLifecycle()
    val fontOption by viewModel.fontOption.collectAsStateWithLifecycle()
    val appIcon by viewModel.appIcon.collectAsStateWithLifecycle()
    val homeLayout by viewModel.homeLayout.collectAsStateWithLifecycle()
    val borderRadius by viewModel.borderRadius.collectAsStateWithLifecycle()
    val glassBlur by viewModel.glassBlur.collectAsStateWithLifecycle()
    val glassTransparency by viewModel.glassTransparency.collectAsStateWithLifecycle()
    val glassGlow by viewModel.glassGlow.collectAsStateWithLifecycle()
    val glassShadow by viewModel.glassShadow.collectAsStateWithLifecycle()
    val glassReflection by viewModel.glassReflection.collectAsStateWithLifecycle()
    val animOpening by viewModel.animOpening.collectAsStateWithLifecycle()
    val animPage by viewModel.animPage.collectAsStateWithLifecycle()
    val animCard by viewModel.animCard.collectAsStateWithLifecycle()
    val animButton by viewModel.animButton.collectAsStateWithLifecycle()
    val animChat by viewModel.animChat.collectAsStateWithLifecycle()
    val animTyping by viewModel.animTyping.collectAsStateWithLifecycle()
    val animSend by viewModel.animSend.collectAsStateWithLifecycle()
    val soundTyping by viewModel.soundTyping.collectAsStateWithLifecycle()
    val soundSend by viewModel.soundSend.collectAsStateWithLifecycle()
    val soundNotification by viewModel.soundNotification.collectAsStateWithLifecycle()
    val hapticLevel by viewModel.hapticLevel.collectAsStateWithLifecycle()
    val lastTranslationLanguage by viewModel.lastTranslationLanguage.collectAsStateWithLifecycle()
    val autoTranslateAI by viewModel.autoTranslateAI.collectAsStateWithLifecycle()

    val credits by viewModel.aiCredits.collectAsStateWithLifecycle()
    val streak by viewModel.streakDays.collectAsStateWithLifecycle()
    val tokens by viewModel.tokensUsed.collectAsStateWithLifecycle()

    val settingsListState = rememberLazyListState()

    LTOAssistantTheme(
        themeMode = themeMode,
        accentColorName = accentColor,
        fontOption = fontOption,
        fontSizeOption = fontSize
    ) {
        val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
        val dynamicBgColor = when {
            themeMode.contains("Light", ignoreCase = true) -> Color(0xFFF1F5F9)
            themeMode.contains("Dark", ignoreCase = true) -> Color(0xFF0F172A)
            themeMode.contains("AMOLED", ignoreCase = true) -> AmoledBlack
            themeMode.contains("Auto", ignoreCase = true) || themeMode.contains("System", ignoreCase = true) -> {
                if (isSystemDark) Color(0xFF0F172A) else Color(0xFFF1F5F9)
            }
            else -> AmoledBlack
        }

    if (currentScreen == Screen.SPLASH) {
        SplashScreen(
            onSplashComplete = {
                viewModel.navigateTo(Screen.CHAT)
            }
        )
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(dynamicBgColor),
            bottomBar = {
                GlassBottomNavBar(
                    currentScreen = currentScreen,
                    appLanguage = appLanguage,
                    hapticLevel = hapticLevel,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(dynamicBgColor)
            ) {
                // Global 60FPS Animated Background Canvas
                AnimatedBackgroundCanvas(
                    bgType = bgAnimation,
                    speed = bgSpeed,
                    intensity = bgIntensity,
                    modifier = Modifier.fillMaxSize()
                )

                Crossfade(targetState = currentScreen, label = "screenTransition") { screen ->
                    when (screen) {
                        Screen.CHAT -> ChatScreen(
                            messages = currentMessages,
                            isRouting = isRouting,
                            isStreaming = isStreaming,
                            currentDecision = currentDecision,
                            streamingText = streamingText,
                            selectedModel = selectedModel,
                            appLanguage = appLanguage,
                            fontSize = fontSize,
                            autoScroll = autoScroll,
                            attachmentName = attachmentName,
                            attachmentType = attachmentType,
                            attachmentUri = attachmentUri,
                            lastTranslationLanguage = lastTranslationLanguage,
                            cardTheme = cardTheme,
                            hapticLevel = hapticLevel,
                            onSendMessage = { text -> viewModel.sendMessage(text) },
                            onStartNewChat = { viewModel.startNewChat() },
                            onStopGenerating = { viewModel.stopGenerating() },
                            onSetModel = { model -> viewModel.setSelectedModel(model) },
                            onSetAttachment = { name, type, uri -> viewModel.setAttachment(name, type, uri) },
                            onClearAttachment = { viewModel.clearAttachment() },
                            onTranslateMessage = { id, lang -> viewModel.translateMessage(id, lang) }
                        )

                        Screen.HISTORY -> HistoryScreen(
                            chats = chats,
                            activeChatId = activeChatId,
                            appLanguage = appLanguage,
                            onOpenChat = { id -> viewModel.openChat(id) },
                            onStartNewChat = { viewModel.startNewChat() },
                            onDeleteChat = { id -> viewModel.deleteChat(id) }
                        )

                        Screen.SETTINGS -> ProfileSettingsScreen(
                            credits = credits,
                            streak = streak,
                            tokens = tokens,
                            themeMode = themeMode,
                            fontSize = fontSize,
                            appLanguage = appLanguage,
                            performanceMode = performanceMode,
                            aiReplyStyle = aiReplyStyle,
                            conversationMemory = conversationMemory,
                            streamingResponse = streamingResponse,
                            markdownFormatting = markdownFormatting,
                            autoScroll = autoScroll,
                            typingAnimation = typingAnimation,
                            hapticFeedback = hapticFeedback,
                            soundEffects = soundEffects,
                            notifications = notifications,
                            currentMessages = currentMessages,
                            // Customization states
                            bgAnimation = bgAnimation,
                            bgSpeed = bgSpeed,
                            bgIntensity = bgIntensity,
                            accentColor = accentColor,
                            cardTheme = cardTheme,
                            bubbleStyle = bubbleStyle,
                            fontOption = fontOption,
                            appIcon = appIcon,
                            homeLayout = homeLayout,
                            borderRadius = borderRadius,
                            glassBlur = glassBlur,
                            glassTransparency = glassTransparency,
                            glassGlow = glassGlow,
                            glassShadow = glassShadow,
                            glassReflection = glassReflection,
                            animOpening = animOpening,
                            animPage = animPage,
                            animCard = animCard,
                            animButton = animButton,
                            animChat = animChat,
                            animTyping = animTyping,
                            animSend = animSend,
                            soundTyping = soundTyping,
                            soundSend = soundSend,
                            soundNotification = soundNotification,
                            hapticLevel = hapticLevel,
                            lastTranslationLanguage = lastTranslationLanguage,
                            autoTranslateAI = autoTranslateAI,
                            listState = settingsListState,
                            // Callbacks
                            onThemeChange = { mode -> viewModel.setThemeMode(mode) },
                            onFontSizeChange = { size -> viewModel.setFontSize(size) },
                            onLanguageChange = { lang -> viewModel.setAppLanguage(lang) },
                            onPerformanceChange = { perf -> viewModel.setPerformanceMode(perf) },
                            onAiReplyStyleChange = { style -> viewModel.setAiReplyStyle(style) },
                            onConversationMemoryChange = { enabled -> viewModel.setConversationMemory(enabled) },
                            onStreamingResponseChange = { enabled -> viewModel.setStreamingResponse(enabled) },
                            onMarkdownFormattingChange = { enabled -> viewModel.setMarkdownFormatting(enabled) },
                            onAutoScrollChange = { enabled -> viewModel.setAutoScroll(enabled) },
                            onTypingAnimationChange = { enabled -> viewModel.setTypingAnimation(enabled) },
                            onHapticFeedbackChange = { enabled -> viewModel.setHapticFeedback(enabled) },
                            onSoundEffectsChange = { enabled -> viewModel.setSoundEffects(enabled) },
                            onNotificationsChange = { enabled -> viewModel.setNotifications(enabled) },
                            onLastTranslationLanguageChange = { lang -> viewModel.setLastTranslationLanguage(lang) },
                            onAutoTranslateAIChange = { enabled -> viewModel.setAutoTranslateAI(enabled) },
                            onClearHistory = { viewModel.clearAllChatHistory() },
                            onResetSettings = { viewModel.resetAllSettings() },
                            // Customization Callbacks
                            onBgAnimationChange = { bg -> viewModel.setBgAnimation(bg) },
                            onBgSpeedChange = { sp -> viewModel.setBgSpeed(sp) },
                            onBgIntensityChange = { it -> viewModel.setBgIntensity(it) },
                            onAccentColorChange = { col -> viewModel.setAccentColor(col) },
                            onCardThemeChange = { card -> viewModel.setCardTheme(card) },
                            onBubbleStyleChange = { bub -> viewModel.setBubbleStyle(bub) },
                            onFontOptionChange = { font -> viewModel.setFontOption(font) },
                            onAppIconChange = { icon -> viewModel.setAppIcon(icon) },
                            onHomeLayoutChange = { layout -> viewModel.setHomeLayout(layout) },
                            onBorderRadiusChange = { rad -> viewModel.setBorderRadius(rad) },
                            onGlassBlurChange = { blur -> viewModel.setGlassBlur(blur) },
                            onGlassTransparencyChange = { trans -> viewModel.setGlassTransparency(trans) },
                            onGlassGlowChange = { glow -> viewModel.setGlassGlow(glow) },
                            onGlassShadowChange = { shd -> viewModel.setGlassShadow(shd) },
                            onGlassReflectionChange = { ref -> viewModel.setGlassReflection(ref) },
                            onAnimOpeningChange = { a -> viewModel.setAnimOpening(a) },
                            onAnimPageChange = { a -> viewModel.setAnimPage(a) },
                            onAnimCardChange = { a -> viewModel.setAnimCard(a) },
                            onAnimButtonChange = { a -> viewModel.setAnimButton(a) },
                            onAnimChatChange = { a -> viewModel.setAnimChat(a) },
                            onAnimTypingChange = { a -> viewModel.setAnimTyping(a) },
                            onAnimSendChange = { a -> viewModel.setAnimSend(a) },
                            onSoundTypingChange = { en -> viewModel.setSoundTyping(en) },
                            onSoundSendChange = { en -> viewModel.setSoundSend(en) },
                            onSoundNotificationChange = { en -> viewModel.setSoundNotification(en) },
                            onHapticLevelChange = { lvl -> viewModel.setHapticLevel(lvl) }
                        )

                        else -> {}
                    }
                }
            }
        }
    }
}
}
