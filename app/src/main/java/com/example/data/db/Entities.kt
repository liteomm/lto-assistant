package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_sessions")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val lastMessage: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelName: String = "Smart Router (Gemini 3.1 Pro)",
    val isPinned: Boolean = false
)

@Entity(tableName = "chat_messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chatId: Long,
    val sender: String, // "user" or "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val routedModel: String? = null,
    val reasoningStepsJson: String? = null,
    val sourcesJson: String? = null
)

@Entity(tableName = "generated_images")
data class GeneratedImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val prompt: String,
    val style: String,
    val imageResName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val aspectRatio: String = "1:1"
)

@Entity(tableName = "document_insights")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val fileType: String,
    val summary: String,
    val keyPointsJson: String,
    val timestamp: Long = System.currentTimeMillis()
)
