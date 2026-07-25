package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ChatEntity
import com.example.ui.components.GlassCard
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.AppLanguageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    chats: List<ChatEntity>,
    activeChatId: Long?,
    appLanguage: String,
    onOpenChat: (Long) -> Unit,
    onStartNewChat: () -> Unit,
    onDeleteChat: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val strings = AppLanguageManager.getStrings(appLanguage)

    val filteredChats = chats.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.lastMessage.contains(searchQuery, ignoreCase = true)
    }

    val pinnedChats = filteredChats.filter { it.isPinned }
    val recentChats = filteredChats.filter { !it.isPinned }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Transparent)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = strings.chatHistoryTitle,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(CyberCyan, NeonViolet)))
                    .clickable { onStartNewChat() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Chat",
                        tint = AmoledBlack,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = strings.newChat,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmoledBlack
                    )
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(strings.searchChats, color = TextMuted, fontSize = 14.sp) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(GlassSurface),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberCyan.copy(alpha = 0.5f),
                unfocusedBorderColor = GlassBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (pinnedChats.isNotEmpty()) {
                item {
                    Text(
                        text = strings.pinnedChats.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(
                    items = pinnedChats,
                    key = { chat -> "pinned_${chat.id}" },
                    contentType = { "chat_item" }
                ) { chat ->
                    HistoryChatItem(
                        chat = chat,
                        isActive = chat.id == activeChatId,
                        onOpenChat = { onOpenChat(chat.id) },
                        onDeleteChat = { onDeleteChat(chat.id) }
                    )
                }
            }

            item(key = "header_recent") {
                Text(
                    text = strings.recentChats.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            }

            if (recentChats.isEmpty() && pinnedChats.isEmpty()) {
                item(key = "empty_history") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = strings.noHistory,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = strings.noHistorySub,
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else {
                items(
                    items = recentChats,
                    key = { chat -> "recent_${chat.id}" },
                    contentType = { "chat_item" }
                ) { chat ->
                    HistoryChatItem(
                        chat = chat,
                        isActive = chat.id == activeChatId,
                        onOpenChat = { onOpenChat(chat.id) },
                        onDeleteChat = { onDeleteChat(chat.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryChatItem(
    chat: ChatEntity,
    isActive: Boolean,
    onOpenChat: () -> Unit,
    onDeleteChat: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenChat() },
        cornerRadius = 20.dp,
        borderColor = if (isActive) CyberCyan.copy(alpha = 0.6f) else GlassBorder,
        glowColor = if (isActive) CyberCyan.copy(alpha = 0.2f) else GlassSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isActive) CyberCyan.copy(alpha = 0.2f) else GlassSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = if (isActive) CyberCyan else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = chat.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            maxLines = 1
                        )
                        if (chat.isPinned) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Pin,
                                contentDescription = "Pinned",
                                tint = CyberCyan,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = chat.lastMessage,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = dateFormat.format(Date(chat.timestamp)),
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            IconButton(onClick = onDeleteChat) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
