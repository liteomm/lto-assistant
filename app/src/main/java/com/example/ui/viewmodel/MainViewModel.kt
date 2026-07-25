package com.example.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.RouterDecision
import com.example.data.api.SmartRouterEngine
import com.example.data.db.AppDatabase
import com.example.data.db.ChatEntity
import com.example.data.db.MessageEntity
import com.example.util.AppPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH, CHAT, HISTORY, SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.appDao()
    val appPreferences = AppPreferences(application)

    val chats: StateFlow<List<ChatEntity>> = dao.getAllChats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _activeChatId = MutableStateFlow<Long?>(null)
    val activeChatId: StateFlow<Long?> = _activeChatId.asStateFlow()

    private val _currentMessages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val currentMessages: StateFlow<List<MessageEntity>> = _currentMessages.asStateFlow()

    private val _isRouting = MutableStateFlow(false)
    val isRouting: StateFlow<Boolean> = _isRouting.asStateFlow()

    private val _currentDecision = MutableStateFlow<RouterDecision?>(null)
    val currentDecision: StateFlow<RouterDecision?> = _currentDecision.asStateFlow()

    private val _streamingText = MutableStateFlow("")
    val streamingText: StateFlow<String> = _streamingText.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private var streamJob: Job? = null

    // Active Model Override
    private val _selectedModel = MutableStateFlow("Smart Router (Auto)")
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    // Attachments State
    private val _attachmentName = MutableStateFlow<String?>(null)
    val attachmentName: StateFlow<String?> = _attachmentName.asStateFlow()

    private val _attachmentType = MutableStateFlow<String?>(null)
    val attachmentType: StateFlow<String?> = _attachmentType.asStateFlow()

    private val _attachmentUri = MutableStateFlow<String?>(null)
    val attachmentUri: StateFlow<String?> = _attachmentUri.asStateFlow()

    // Persistent Settings States
    val themeMode = MutableStateFlow(appPreferences.themeMode)
    val fontSize = MutableStateFlow(appPreferences.fontSize)
    val appLanguage = MutableStateFlow(appPreferences.appLanguage)
    val performanceMode = MutableStateFlow(appPreferences.performanceMode)

    val conversationMemory = MutableStateFlow(appPreferences.conversationMemory)
    val streamingResponse = MutableStateFlow(appPreferences.streamingResponse)
    val markdownFormatting = MutableStateFlow(appPreferences.markdownFormatting)
    val autoScroll = MutableStateFlow(appPreferences.autoScroll)
    val typingAnimation = MutableStateFlow(appPreferences.typingAnimation)
    val hapticFeedback = MutableStateFlow(appPreferences.hapticFeedback)
    val soundEffects = MutableStateFlow(appPreferences.soundEffects)
    val notifications = MutableStateFlow(appPreferences.notifications)
    val aiReplyStyle = MutableStateFlow(appPreferences.aiReplyStyle)

    // New Premium Customization StateFlows
    val bgAnimation = MutableStateFlow(appPreferences.bgAnimation)
    val bgSpeed = MutableStateFlow(appPreferences.bgSpeed)
    val bgIntensity = MutableStateFlow(appPreferences.bgIntensity)
    val accentColor = MutableStateFlow(appPreferences.accentColor)
    val cardTheme = MutableStateFlow(appPreferences.cardTheme)
    val bubbleStyle = MutableStateFlow(appPreferences.bubbleStyle)
    val fontOption = MutableStateFlow(appPreferences.fontOption)
    val appIcon = MutableStateFlow(appPreferences.appIcon)
    val homeLayout = MutableStateFlow(appPreferences.homeLayout)
    val borderRadius = MutableStateFlow(appPreferences.borderRadius)
    val glassBlur = MutableStateFlow(appPreferences.glassBlur)
    val glassTransparency = MutableStateFlow(appPreferences.glassTransparency)
    val glassGlow = MutableStateFlow(appPreferences.glassGlow)
    val glassShadow = MutableStateFlow(appPreferences.glassShadow)
    val glassReflection = MutableStateFlow(appPreferences.glassReflection)
    val animOpening = MutableStateFlow(appPreferences.animOpening)
    val animPage = MutableStateFlow(appPreferences.animPage)
    val animCard = MutableStateFlow(appPreferences.animCard)
    val animButton = MutableStateFlow(appPreferences.animButton)
    val animChat = MutableStateFlow(appPreferences.animChat)
    val animTyping = MutableStateFlow(appPreferences.animTyping)
    val animSend = MutableStateFlow(appPreferences.animSend)
    val soundTyping = MutableStateFlow(appPreferences.soundTyping)
    val soundSend = MutableStateFlow(appPreferences.soundSend)
    val soundNotification = MutableStateFlow(appPreferences.soundNotification)
    val hapticLevel = MutableStateFlow(appPreferences.hapticLevel)
    val lastTranslationLanguage = MutableStateFlow(appPreferences.lastTranslationLanguage)
    val autoTranslateAI = MutableStateFlow(appPreferences.autoTranslateAI)

    // User profile stats
    val aiCredits = MutableStateFlow(9850)
    val streakDays = MutableStateFlow(14)
    val tokensUsed = MutableStateFlow("1.24M Tokens")

    init {
        viewModelScope.launch {
            chats.collectLatest { list ->
                if (list.isEmpty()) {
                    populateInitialData()
                }
            }
        }
    }

    private suspend fun populateInitialData() {
        val chatId = dao.insertChat(
            ChatEntity(
                title = "LTO Assistant Architecture & Smart AI Routing",
                lastMessage = "LTO Assistant automatically selects Gemini 3.1 Pro or Gemini 3.5 Flash for your studies.",
                modelName = "LTO Assistant",
                isPinned = true
            )
        )
        dao.insertMessage(
            MessageEntity(
                chatId = chatId,
                sender = "user",
                text = "Explain how LTO Assistant helps students solve complex homework and exam questions."
            )
        )
        dao.insertMessage(
            MessageEntity(
                chatId = chatId,
                sender = "ai",
                text = """
### LTO Assistant 🎓

LTO Assistant (Powered by LiteOMM) analyzes every question in 4 distinct high-speed steps:

1. **Intent & Subject Detection**: Classifies whether your query is Mathematics, Science, Literature, History, Computer Science, or local languages.
2. **Difficulty Assessment**: Evaluates problem depth, step-by-step logic, and formula derivation requirements.
3. **Model Selection**: Routes quick factual questions to `gemini-3.5-flash` for instant speed, and complex deep proofs/code to `gemini-3.1-pro-preview`.
4. **Structured Explanation**: Outputs clean markdown formatted with step-by-step solutions, key takeaways, and follow-up study suggestions!
                """.trimIndent(),
                routedModel = "gemini-3.1-pro-preview"
            )
        )
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun openChat(chatId: Long) {
        streamJob?.cancel()
        _isStreaming.value = false
        _isRouting.value = false
        _streamingText.value = ""
        _currentDecision.value = null
        clearAttachment()

        _activeChatId.value = chatId
        loadMessagesForChat(chatId)
        _currentScreen.value = Screen.CHAT
    }

    fun setSelectedModel(model: String) {
        _selectedModel.value = model
    }

    fun setAttachment(name: String, type: String, uri: String) {
        _attachmentName.value = name
        _attachmentType.value = type
        _attachmentUri.value = uri
    }

    fun clearAttachment() {
        _attachmentName.value = null
        _attachmentType.value = null
        _attachmentUri.value = null
    }

    // --- SETTINGS SETTERS (INSTANT PERSISTENCE & APPLIED) ---
    fun setThemeMode(mode: String) {
        appPreferences.themeMode = mode
        themeMode.value = mode
        showToast("Theme changed to $mode")
    }

    fun setFontSize(size: String) {
        appPreferences.fontSize = size
        fontSize.value = size
        showToast("Font size set to $size")
    }

    fun setAppLanguage(lang: String) {
        appPreferences.appLanguage = lang
        appLanguage.value = lang
        showToast("App language updated to $lang")
    }

    fun setPerformanceMode(mode: String) {
        appPreferences.performanceMode = mode
        performanceMode.value = mode
        showToast("Performance mode: $mode")
    }

    fun setConversationMemory(enabled: Boolean) {
        appPreferences.conversationMemory = enabled
        conversationMemory.value = enabled
        showToast(if (enabled) "Conversation memory enabled" else "Conversation memory disabled")
    }

    fun setStreamingResponse(enabled: Boolean) {
        appPreferences.streamingResponse = enabled
        streamingResponse.value = enabled
        showToast(if (enabled) "Word streaming enabled" else "Instant response enabled")
    }

    fun setMarkdownFormatting(enabled: Boolean) {
        appPreferences.markdownFormatting = enabled
        markdownFormatting.value = enabled
        showToast(if (enabled) "Markdown formatting ON" else "Markdown formatting OFF")
    }

    fun setAutoScroll(enabled: Boolean) {
        appPreferences.autoScroll = enabled
        autoScroll.value = enabled
        showToast(if (enabled) "Auto scroll enabled" else "Auto scroll disabled")
    }

    fun setTypingAnimation(enabled: Boolean) {
        appPreferences.typingAnimation = enabled
        typingAnimation.value = enabled
        showToast(if (enabled) "Typing animation ON" else "Typing animation OFF")
    }

    fun setHapticFeedback(enabled: Boolean) {
        appPreferences.hapticFeedback = enabled
        hapticFeedback.value = enabled
        showToast(if (enabled) "Haptic feedback ON" else "Haptic feedback OFF")
    }

    fun setSoundEffects(enabled: Boolean) {
        appPreferences.soundEffects = enabled
        soundEffects.value = enabled
        showToast(if (enabled) "Sound effects ON" else "Sound effects OFF")
    }

    fun setNotifications(enabled: Boolean) {
        appPreferences.notifications = enabled
        notifications.value = enabled
        showToast(if (enabled) "Notifications ON" else "Notifications OFF")
    }

    fun setAiReplyStyle(style: String) {
        appPreferences.aiReplyStyle = style
        aiReplyStyle.value = style
        showToast("AI Reply Style set to $style")
    }

    fun setLastTranslationLanguage(lang: String) {
        appPreferences.lastTranslationLanguage = lang
        lastTranslationLanguage.value = lang
    }

    fun setAutoTranslateAI(enabled: Boolean) {
        appPreferences.autoTranslateAI = enabled
        autoTranslateAI.value = enabled
        showToast(if (enabled) "Auto Translate AI Responses ON" else "Auto Translate AI Responses OFF")
    }

    fun translateMessage(messageId: Long, targetLanguage: String) {
        setLastTranslationLanguage(targetLanguage)
        viewModelScope.launch {
            val msg = _currentMessages.value.find { it.id == messageId } ?: return@launch
            showToast("Translating AI response into $targetLanguage...")
            val translatedText = SmartRouterEngine.translateText(
                originalText = msg.text,
                targetLanguage = targetLanguage,
                replyStyle = aiReplyStyle.value
            )
            if (translatedText.isNotBlank()) {
                val updated = msg.copy(text = translatedText)
                dao.insertMessage(updated)
            }
        }
    }

    fun setBgAnimation(bg: String) {
        appPreferences.bgAnimation = bg
        bgAnimation.value = bg
    }

    fun setBgSpeed(speed: String) {
        appPreferences.bgSpeed = speed
        bgSpeed.value = speed
    }

    fun setBgIntensity(intensity: String) {
        appPreferences.bgIntensity = intensity
        bgIntensity.value = intensity
    }

    fun setAccentColor(colorName: String) {
        appPreferences.accentColor = colorName
        accentColor.value = colorName
        showToast("Accent Color set to $colorName")
    }

    fun setCardTheme(theme: String) {
        appPreferences.cardTheme = theme
        cardTheme.value = theme
        showToast("Card Theme set to $theme")
    }

    fun setBubbleStyle(style: String) {
        appPreferences.bubbleStyle = style
        bubbleStyle.value = style
        showToast("Bubble Style set to $style")
    }

    fun setFontOption(font: String) {
        appPreferences.fontOption = font
        fontOption.value = font
        showToast("Font changed to $font")
    }

    fun setAppIcon(iconName: String) {
        appPreferences.appIcon = iconName
        appIcon.value = iconName
        showToast("App Icon set to $iconName")
    }

    fun setHomeLayout(layout: String) {
        appPreferences.homeLayout = layout
        homeLayout.value = layout
        showToast("Home Layout set to $layout")
    }

    fun setBorderRadius(radius: String) {
        appPreferences.borderRadius = radius
        borderRadius.value = radius
    }

    fun setGlassBlur(blur: String) {
        appPreferences.glassBlur = blur
        glassBlur.value = blur
    }

    fun setGlassTransparency(transparency: String) {
        appPreferences.glassTransparency = transparency
        glassTransparency.value = transparency
    }

    fun setGlassGlow(glow: String) {
        appPreferences.glassGlow = glow
        glassGlow.value = glow
    }

    fun setGlassShadow(shadow: String) {
        appPreferences.glassShadow = shadow
        glassShadow.value = shadow
    }

    fun setGlassReflection(reflection: String) {
        appPreferences.glassReflection = reflection
        glassReflection.value = reflection
    }

    fun setAnimOpening(anim: String) { appPreferences.animOpening = anim; animOpening.value = anim }
    fun setAnimPage(anim: String) { appPreferences.animPage = anim; animPage.value = anim }
    fun setAnimCard(anim: String) { appPreferences.animCard = anim; animCard.value = anim }
    fun setAnimButton(anim: String) { appPreferences.animButton = anim; animButton.value = anim }
    fun setAnimChat(anim: String) { appPreferences.animChat = anim; animChat.value = anim }
    fun setAnimTyping(anim: String) { appPreferences.animTyping = anim; animTyping.value = anim }
    fun setAnimSend(anim: String) { appPreferences.animSend = anim; animSend.value = anim }

    fun setSoundTyping(enabled: Boolean) { appPreferences.soundTyping = enabled; soundTyping.value = enabled }
    fun setSoundSend(enabled: Boolean) { appPreferences.soundSend = enabled; soundSend.value = enabled }
    fun setSoundNotification(enabled: Boolean) { appPreferences.soundNotification = enabled; soundNotification.value = enabled }
    fun setHapticLevel(level: String) { appPreferences.hapticLevel = level; hapticLevel.value = level; showToast("Haptic level: $level") }

    fun resetAllSettings() {
        appPreferences.resetToDefaults()
        themeMode.value = appPreferences.themeMode
        fontSize.value = appPreferences.fontSize
        appLanguage.value = appPreferences.appLanguage
        performanceMode.value = appPreferences.performanceMode
        conversationMemory.value = appPreferences.conversationMemory
        streamingResponse.value = appPreferences.streamingResponse
        markdownFormatting.value = appPreferences.markdownFormatting
        autoScroll.value = appPreferences.autoScroll
        typingAnimation.value = appPreferences.typingAnimation
        hapticFeedback.value = appPreferences.hapticFeedback
        soundEffects.value = appPreferences.soundEffects
        notifications.value = appPreferences.notifications
        aiReplyStyle.value = appPreferences.aiReplyStyle

        bgAnimation.value = appPreferences.bgAnimation
        bgSpeed.value = appPreferences.bgSpeed
        bgIntensity.value = appPreferences.bgIntensity
        accentColor.value = appPreferences.accentColor
        cardTheme.value = appPreferences.cardTheme
        bubbleStyle.value = appPreferences.bubbleStyle
        fontOption.value = appPreferences.fontOption
        appIcon.value = appPreferences.appIcon
        homeLayout.value = appPreferences.homeLayout
        borderRadius.value = appPreferences.borderRadius
        glassBlur.value = appPreferences.glassBlur
        glassTransparency.value = appPreferences.glassTransparency
        glassGlow.value = appPreferences.glassGlow
        glassShadow.value = appPreferences.glassShadow
        glassReflection.value = appPreferences.glassReflection
        animOpening.value = appPreferences.animOpening
        animPage.value = appPreferences.animPage
        animCard.value = appPreferences.animCard
        animButton.value = appPreferences.animButton
        animChat.value = appPreferences.animChat
        animTyping.value = appPreferences.animTyping
        animSend.value = appPreferences.animSend
        soundTyping.value = appPreferences.soundTyping
        soundSend.value = appPreferences.soundSend
        soundNotification.value = appPreferences.soundNotification
        hapticLevel.value = appPreferences.hapticLevel
        showToast("All settings reset to defaults!")
    }

    private fun showToast(msg: String) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
    }

    private var messagesJob: Job? = null

    private fun loadMessagesForChat(chatId: Long) {
        messagesJob?.cancel()
        _currentMessages.value = emptyList()
        messagesJob = viewModelScope.launch {
            dao.getMessagesForChat(chatId).collect { msgs ->
                _currentMessages.value = msgs
            }
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank() && _attachmentName.value == null) return

        val attName = _attachmentName.value
        val attType = _attachmentType.value
        val attUri = _attachmentUri.value

        clearAttachment()
        streamJob?.cancel()

        streamJob = viewModelScope.launch {
            try {
                var chatId = _activeChatId.value
                if (chatId == null) {
                    chatId = dao.insertChat(
                        ChatEntity(
                            title = if (userText.length > 30) userText.take(30) + "..." else if (!attName.isNullOrEmpty()) attName else "New Study Chat",
                            lastMessage = if (userText.isNotBlank()) userText else "Attachment: $attName",
                            modelName = _selectedModel.value,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    _activeChatId.value = chatId
                    loadMessagesForChat(chatId)
                } else {
                    val currentChat = chats.value.find { it.id == chatId }
                    val titleToUse = if (currentChat == null || currentChat.title == "New Study Session" || currentChat.title == "New Study Chat") {
                        if (userText.length > 30) userText.take(30) + "..." else if (!attName.isNullOrEmpty()) attName else "New Study Chat"
                    } else {
                        currentChat.title
                    }

                    dao.insertChat(
                        ChatEntity(
                            id = chatId,
                            title = titleToUse,
                            lastMessage = if (userText.isNotBlank()) userText else "Attachment: $attName",
                            modelName = currentChat?.modelName ?: _selectedModel.value,
                            isPinned = currentChat?.isPinned ?: false,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }

                val fullUserMsgText = if (!attName.isNullOrEmpty()) {
                    "📎 [Attached $attType: $attName]\n$userText"
                } else {
                    userText
                }

                // Insert User Message
                dao.insertMessage(
                    MessageEntity(
                        chatId = chatId,
                        sender = "user",
                        text = fullUserMsgText
                    )
                )

                // Prepare prompt context based on Conversation Memory & App Language
                var fullPrompt = userText
                if (conversationMemory.value && _currentMessages.value.isNotEmpty()) {
                    val history = _currentMessages.value.takeLast(4).joinToString("\n") { "${it.sender}: ${it.text}" }
                    fullPrompt = "Context History:\n$history\n\nUser Question:\n$userText"
                }

                val lang = appLanguage.value
                if (lang != "English") {
                    fullPrompt += "\n\n(Note: Please provide the response in $lang language.)"
                }

                // Instant Smart Router Analysis
                _isRouting.value = false
                _currentDecision.value = SmartRouterEngine.analyzePrompt(userText)

                val decision = _currentDecision.value ?: RouterDecision(
                    intent = "STUDENT_QA",
                    difficulty = "MEDIUM",
                    selectedModel = "gemini-3.5-flash",
                    requiresWebSearch = false,
                    requiresVision = false,
                    steps = emptyList()
                )
                _isStreaming.value = true

                val forcedModel = if (_selectedModel.value != "Smart Router (Auto)") _selectedModel.value else null

                // Stream response with AI Reply Style System
                _streamingText.value = ""
                val currentReplyStyle = aiReplyStyle.value
                if (streamingResponse.value) {
                    SmartRouterEngine.streamResponse(fullPrompt, decision, forcedModel, currentReplyStyle).collect { textChunk ->
                        _streamingText.value = textChunk
                    }
                } else {
                    // Non-streaming instant mode
                    var finalOutput = ""
                    SmartRouterEngine.streamResponse(fullPrompt, decision, forcedModel, currentReplyStyle).collect { textChunk ->
                        finalOutput = textChunk
                    }
                    _streamingText.value = finalOutput
                }

                // Save final AI Message
                var finalAiText = _streamingText.value
                if (finalAiText.isNotBlank()) {
                    if (autoTranslateAI.value && lastTranslationLanguage.value.isNotBlank() && !lastTranslationLanguage.value.equals("English", ignoreCase = true)) {
                        val translated = SmartRouterEngine.translateText(
                            originalText = finalAiText,
                            targetLanguage = lastTranslationLanguage.value,
                            replyStyle = aiReplyStyle.value
                        )
                        if (translated.isNotBlank()) {
                            finalAiText = translated
                        }
                    }

                    dao.insertMessage(
                        MessageEntity(
                            chatId = chatId,
                            sender = "ai",
                            text = finalAiText,
                            routedModel = forcedModel ?: decision.selectedModel
                        )
                    )
                }

                // Deduct credits slightly
                aiCredits.value = (aiCredits.value - 2).coerceAtLeast(0)
            } catch (e: Exception) {
                e.printStackTrace()
                val chatId = _activeChatId.value
                if (chatId != null && _streamingText.value.isBlank()) {
                    val fallback = SmartRouterEngine.generateIntelligentFallbackResponse(
                        userText,
                        _currentDecision.value ?: RouterDecision("STUDENT_QA", "MEDIUM", "gemini-3.5-flash", false, false, emptyList()),
                        _selectedModel.value,
                        aiReplyStyle.value
                    )
                    dao.insertMessage(
                        MessageEntity(
                            chatId = chatId,
                            sender = "ai",
                            text = fallback,
                            routedModel = _selectedModel.value
                        )
                    )
                }
            } finally {
                _isStreaming.value = false
                _isRouting.value = false
                _streamingText.value = ""
                _currentDecision.value = null
            }
        }
    }

    fun stopGenerating() {
        streamJob?.cancel()
        _isStreaming.value = false
        _isRouting.value = false
        val currentText = _streamingText.value
        val chatId = _activeChatId.value
        if (chatId != null && currentText.isNotBlank()) {
            viewModelScope.launch {
                dao.insertMessage(
                    MessageEntity(
                        chatId = chatId,
                        sender = "ai",
                        text = "$currentText\n\n*(Generation stopped by user)*",
                        routedModel = _currentDecision.value?.selectedModel ?: "Smart Router"
                    )
                )
                _streamingText.value = ""
                _currentDecision.value = null
            }
        }
    }

    fun startNewChat() {
        streamJob?.cancel()
        _isStreaming.value = false
        _isRouting.value = false
        _streamingText.value = ""
        _currentDecision.value = null
        clearAttachment()

        messagesJob?.cancel()
        _activeChatId.value = null
        _currentMessages.value = emptyList()
        _currentScreen.value = Screen.CHAT
    }

    fun deleteChat(chatId: Long) {
        viewModelScope.launch {
            dao.deleteChat(chatId)
            dao.deleteMessagesForChat(chatId)
            val list = chats.value.filter { it.id != chatId }
            if (_activeChatId.value == chatId) {
                if (list.isNotEmpty()) {
                    openChat(list.first().id)
                } else {
                    startNewChat()
                }
            }
        }
    }

    fun clearAllChatHistory() {
        viewModelScope.launch {
            chats.value.forEach {
                dao.deleteChat(it.id)
                dao.deleteMessagesForChat(it.id)
            }
            startNewChat()
        }
    }
}
