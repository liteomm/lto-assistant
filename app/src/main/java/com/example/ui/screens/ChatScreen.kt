package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.api.RouterDecision
import com.example.data.api.SmartRouterEngine
import com.example.data.db.MessageEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.MarkdownText
import com.example.ui.components.SmartRouterAnimationView
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.GlassSurfaceVariant
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.AppLanguageManager
import com.example.util.StudentSuggestion
import com.example.util.StudentSuggestionsManager
import java.util.Calendar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    messages: List<MessageEntity>,
    isRouting: Boolean,
    isStreaming: Boolean,
    currentDecision: RouterDecision?,
    streamingText: String,
    selectedModel: String,
    appLanguage: String,
    fontSize: String,
    autoScroll: Boolean,
    attachmentName: String?,
    attachmentType: String?,
    attachmentUri: String?,
    lastTranslationLanguage: String = "Hindi",
    cardTheme: String = "Glass Card",
    hapticLevel: String = "Medium",
    onSendMessage: (String) -> Unit,
    onStartNewChat: () -> Unit,
    onStopGenerating: () -> Unit,
    onSetModel: (String) -> Unit,
    onSetAttachment: (String, String, String) -> Unit,
    onClearAttachment: () -> Unit,
    onTranslateMessage: (Long, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val strings = AppLanguageManager.getStrings(appLanguage)

    // Load 8 smart student suggestions (Left 4 & Right 4)
    val suggestionGroup = remember { StudentSuggestionsManager.get8SmartSuggestions(context) }

    // Dynamic local time hour state for real-time greeting updates
    var currentHour by remember { mutableStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(5000L)
            val newHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            if (newHour != currentHour) {
                currentHour = newHour
            }
        }
    }

    // Dynamic greeting calculation using device local time rules:
    // 05:00–11:59 Good Morning | 12:00–16:59 Good Afternoon | 17:00–20:59 Good Evening | 21:00–04:59 Good Night
    val timeGreeting = remember(appLanguage, currentHour) {
        when {
            currentHour in 5..11 -> strings.greetingMorning
            currentHour in 12..16 -> strings.greetingAfternoon
            currentHour in 17..20 -> strings.greetingEvening
            else -> strings.greetingNight
        }
    }

    // Auto-scroll on new messages or when streaming / routing starts/updates/stops
    LaunchedEffect(messages.size, isStreaming, isRouting, streamingText.length) {
        if (autoScroll) {
            val totalCount = messages.size + (if (streamingText.isNotEmpty() || isStreaming || isRouting) 1 else 0)
            if (totalCount > 0 && !listState.isScrollInProgress) {
                listState.scrollToItem(totalCount - 1)
            }
        }
    }

    // File picker launcher for images or documents
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = it.lastPathSegment?.substringAfterLast("/") ?: "Attached_File"
            val mimeType = context.contentResolver.getType(it) ?: ""
            val type = if (mimeType.contains("pdf")) "PDF Document" else "Image File"
            onSetAttachment(fileName, type, it.toString())
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // --- TOP BAR: Greeting, Smart Router Status, Model Selector & Profile ---
        TopStudentHeader(
            greeting = timeGreeting,
            onlineText = strings.smartRouterOnline,
            selectedModel = selectedModel,
            onSetModel = onSetModel,
            onStartNewChat = onStartNewChat
        )

        // --- MAIN AREA: Empty State or Messages List ---
        if (messages.isEmpty()) {
            // Student Empty Welcome Screen with Compact 2-Column Grid Layout
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    CyberCyan.copy(alpha = 0.25f),
                                    NeonViolet.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        )
                        .border(1.5.dp, Brush.linearGradient(listOf(CyberCyan, NeonViolet)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_ai_hero_orb_1784784436462),
                        contentDescription = "AI Orb",
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = strings.howCanIHelp,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextPrimary
                )

                Text(
                    text = strings.studentSuggestionsHeader,
                    fontSize = 11.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                // Compact 2-Column Grid (4 items left, 4 items right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Left Column: Odia, Mathematics, History, Hindi
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestionGroup.left.forEach { suggestion ->
                            CompactStudentSuggestionCard(
                                suggestion = suggestion,
                                cardTheme = cardTheme,
                                onClick = {
                                    com.example.util.HapticFeedbackManager.performHaptic(context, hapticLevel)
                                    onSendMessage(suggestion.question)
                                }
                            )
                        }
                    }

                    // Right Column: English, Science, Geography, Physics
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestionGroup.right.forEach { suggestion ->
                            CompactStudentSuggestionCard(
                                suggestion = suggestion,
                                cardTheme = cardTheme,
                                onClick = {
                                    com.example.util.HapticFeedbackManager.performHaptic(context, hapticLevel)
                                    onSendMessage(suggestion.question)
                                }
                            )
                        }
                    }
                }
            }
        } else {
            // Conversation Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = messages,
                    key = { msg -> if (msg.id != 0L) "msg_${msg.id}" else "msg_${msg.timestamp}_${msg.text.hashCode()}" },
                    contentType = { msg -> msg.sender }
                ) { msg ->
                    MessageBubble(
                        message = msg,
                        strings = strings,
                        lastSelectedLanguage = lastTranslationLanguage,
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("LTO Assistant Note", msg.text)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, strings.copiedToast, Toast.LENGTH_SHORT).show()
                        },
                        onRegenerate = { onSendMessage(msg.text) },
                        onShare = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, msg.text)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share AI Response"))
                        },
                        onTranslate = { targetLang ->
                            onTranslateMessage(msg.id, targetLang)
                        },
                        onFollowUpClick = { followUpPrompt ->
                            onSendMessage(followUpPrompt)
                        }
                    )
                }

                // Live Streaming AI Response or Immediate Typing Indicator
                if ((isStreaming || isRouting) && streamingText.isEmpty()) {
                    item(key = "typing_indicator", contentType = "typing_indicator") {
                        TypingIndicatorBubble()
                    }
                } else if (streamingText.isNotEmpty()) {
                    item(key = "streaming_bubble", contentType = "streaming_bubble") {
                        MessageBubble(
                            message = MessageEntity(
                                chatId = 0,
                                sender = "ai",
                                text = streamingText + " ▌",
                                routedModel = currentDecision?.selectedModel ?: selectedModel
                            ),
                            strings = strings,
                            lastSelectedLanguage = lastTranslationLanguage,
                            onCopy = {},
                            onRegenerate = {},
                            onShare = {},
                            onTranslate = {},
                            onFollowUpClick = {}
                        )
                    }
                }
            }
        }

        // --- ATTACHMENT PREVIEW BAR (IF ATTACHED) ---
        if (attachmentName != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                GlassCard(
                    cornerRadius = 16.dp,
                    borderColor = CyberCyan,
                    glowColor = CyberCyan.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (attachmentType?.contains("PDF") == true) Icons.Default.Description else Icons.Default.Image,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = attachmentName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = attachmentType ?: "Attachment Ready",
                                    fontSize = 10.sp,
                                    color = EmeraldGlow
                                )
                            }
                        }

                        IconButton(onClick = onClearAttachment, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- BOTTOM INPUT BAR & ACTION BUTTONS ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Attachment Button
                IconButton(
                    onClick = { filePickerLauncher.launch("*/*") },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach PDF or Image",
                        tint = CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Input Box Container
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(strings.askPlaceholder, color = TextMuted, fontSize = 14.sp)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(26.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = GlassSurface,
                        unfocusedContainerColor = GlassSurface,
                        focusedBorderColor = CyberCyan.copy(alpha = 0.8f),
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (inputText.isNotBlank() || attachmentName != null) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    }),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send or Stop Generating Button
                if (isStreaming) {
                    IconButton(
                        onClick = onStopGenerating,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(NeonPink, Color.Red)))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    val canSend = inputText.isNotBlank() || attachmentName != null
                    IconButton(
                        onClick = {
                            if (canSend) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                if (canSend)
                                    Brush.linearGradient(listOf(CyberCyan, NeonViolet))
                                else
                                    Brush.linearGradient(listOf(GlassBorder, GlassSurfaceVariant))
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = if (canSend) AmoledBlack else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopStudentHeader(
    greeting: String,
    onlineText: String,
    selectedModel: String,
    onSetModel: (String) -> Unit,
    onStartNewChat: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    // Pulsating indicator for Smart Router Online status
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                color = TextPrimary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(EmeraldGlow.copy(alpha = alphaAnim))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = onlineText,
                    fontSize = 11.sp,
                    color = EmeraldGlow,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Model Selection Pill Dropdown
            Box {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                        .clickable { isMenuExpanded = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (selectedModel.contains("pro")) "Gemini 3.1 Pro" else if (selectedModel.contains("flash")) "Gemini 3.5 Flash" else "Auto Router",
                            fontSize = 11.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier
                        .background(AmoledBlack)
                        .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("Smart Router (Auto)", color = TextPrimary, fontSize = 12.sp) },
                        onClick = { onSetModel("Smart Router (Auto)"); isMenuExpanded = false },
                        leadingIcon = { Icon(Icons.Default.AutoAwesome, null, tint = CyberCyan, modifier = Modifier.size(16.dp)) }
                    )
                    DropdownMenuItem(
                        text = { Text("Gemini 3.1 Pro (Deep Reasoning)", color = TextPrimary, fontSize = 12.sp) },
                        onClick = { onSetModel("gemini-3.1-pro-preview"); isMenuExpanded = false },
                        leadingIcon = { Icon(Icons.Default.Check, null, tint = NeonViolet, modifier = Modifier.size(16.dp)) }
                    )
                    DropdownMenuItem(
                        text = { Text("Gemini 3.5 Flash (Instant Speed)", color = TextPrimary, fontSize = 12.sp) },
                        onClick = { onSetModel("gemini-3.5-flash"); isMenuExpanded = false },
                        leadingIcon = { Icon(Icons.Default.Check, null, tint = EmeraldGlow, modifier = Modifier.size(16.dp)) }
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // New Chat Button
            IconButton(
                onClick = onStartNewChat,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GlassSurface)
                    .border(1.dp, CyberCyan.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Chat",
                    tint = CyberCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun CompactStudentSuggestionCard(
    suggestion: StudentSuggestion,
    cardTheme: String = "Glass Card",
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = 8.dp,
        cornerRadius = 16.dp,
        borderColor = GlassBorder,
        glowColor = GlassSurface,
        cardTheme = cardTheme,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = suggestion.icon,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = suggestion.subject,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = suggestion.question,
                fontSize = 11.sp,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MessageBubble(
    message: MessageEntity,
    strings: com.example.util.TranslationStrings,
    lastSelectedLanguage: String = "Hindi",
    onCopy: () -> Unit,
    onRegenerate: () -> Unit,
    onShare: () -> Unit,
    onTranslate: (String) -> Unit = {},
    onFollowUpClick: (String) -> Unit
) {
    val isUser = message.sender == "user"
    var isTranslateExpanded by remember { mutableStateOf(false) }
    val followUpQuestions = remember(message.text) {
        if (!isUser) SmartRouterEngine.generateFollowUpQuestions(message.text) else emptyList()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.98f),
            cornerRadius = 22.dp,
            borderColor = if (isUser) NeonViolet.copy(alpha = 0.4f) else CyberCyan.copy(alpha = 0.25f),
            glowColor = if (isUser) NeonViolet.copy(alpha = 0.12f) else CyberCyan.copy(alpha = 0.1f)
        ) {
            Column {
                MarkdownText(text = message.text)

                if (!isUser) {
                    Spacer(modifier = Modifier.height(10.dp))
                    // Premium Response Action Bar: 📋 Copy  🔄 Regenerate  🌐 Translate  📤 Share
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ResponseActionButton(
                            icon = "📋",
                            label = "Copy",
                            onClick = onCopy
                        )
                        ResponseActionButton(
                            icon = "🔄",
                            label = "Regenerate",
                            onClick = onRegenerate
                        )
                        ResponseActionButton(
                            icon = "🌐",
                            label = "Translate",
                            isSelected = isTranslateExpanded,
                            onClick = { isTranslateExpanded = !isTranslateExpanded }
                        )
                        ResponseActionButton(
                            icon = "📤",
                            label = "Share",
                            onClick = onShare
                        )
                    }

                    // Inline Language Panel (Slide down + Fade transition, no popup)
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isTranslateExpanded,
                        enter = androidx.compose.animation.fadeIn(tween(200)) + androidx.compose.animation.expandVertically(tween(200)),
                        exit = androidx.compose.animation.fadeOut(tween(200)) + androidx.compose.animation.shrinkVertically(tween(200))
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            com.example.ui.components.InlineLanguagePanel(
                                selectedLanguage = lastSelectedLanguage,
                                onLanguageSelected = { selectedLang ->
                                    isTranslateExpanded = false
                                    onTranslate(selectedLang)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Suggested Follow-up Questions Chips below completed AI responses
        if (!isUser && followUpQuestions.isNotEmpty() && !message.text.contains("▌")) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp)
            ) {
                Text(
                    text = strings.suggestedFollowUps,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                followUpQuestions.forEach { followUp ->
                    Box(
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(GlassSurface)
                            .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .clickable { onFollowUpClick(followUp) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = followUp,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypingIndicatorBubble() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing_dots")
    val dot1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val dot2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val dot3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        GlassCard(
            cornerRadius = 18.dp,
            borderColor = CyberCyan.copy(alpha = 0.3f),
            glowColor = CyberCyan.copy(alpha = 0.1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = dot1Alpha))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = dot2Alpha))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = dot3Alpha))
                )
            }
        }
    }
}

@Composable
private fun ResponseActionButton(
    icon: String,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) CyberCyan.copy(alpha = 0.25f) else GlassSurface)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) CyberCyan else GlassBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 11.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) CyberCyan else TextPrimary
            )
        }
    }
}

