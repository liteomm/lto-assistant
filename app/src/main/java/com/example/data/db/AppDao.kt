package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM chat_sessions ORDER BY isPinned DESC, timestamp DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity): Long

    @Query("DELETE FROM chat_sessions WHERE id = :chatId")
    suspend fun deleteChat(chatId: Long)

    @Query("DELETE FROM chat_messages WHERE chatId = :chatId")
    suspend fun deleteMessagesForChat(chatId: Long)

    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: Long): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Query("SELECT * FROM generated_images ORDER BY timestamp DESC")
    fun getAllGeneratedImages(): Flow<List<GeneratedImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneratedImage(image: GeneratedImageEntity): Long

    @Query("SELECT * FROM document_insights ORDER BY timestamp DESC")
    fun getAllDocuments(): Flow<List<DocumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentEntity): Long
}
