package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

private val CODE_FENCE_REGEX = Regex("```(\\w*)\\n?([\\s\\S]*?)```")
private val BOLD_REGEX = Regex("\\*\\*(.*?)\\*\\*")

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val blocks = remember(text) { parseMarkdownBlocks(text) }

    Column(modifier = modifier) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.Code -> {
                    CodeBlockView(
                        code = block.code,
                        language = block.language,
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Code", block.code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                is MarkdownBlock.Paragraph -> {
                    val annotatedString = remember(block.content) { buildAnnotatedMarkdown(block.content) }
                    Text(
                        text = annotatedString,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CodeBlockView(
    code: String,
    language: String,
    onCopy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AmoledBlack)
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GlassBorder.copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = CyberCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = language.ifBlank { "code" },
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy code",
                        tint = TextPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Code Body
            Text(
                text = code,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = CyberCyan,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

sealed class MarkdownBlock {
    data class Paragraph(val content: String) : MarkdownBlock()
    data class Code(val code: String, val language: String) : MarkdownBlock()
}

private fun parseMarkdownBlocks(text: String): List<MarkdownBlock> {
    if (text.isBlank()) return emptyList()
    val list = mutableListOf<MarkdownBlock>()

    var lastIndex = 0
    CODE_FENCE_REGEX.findAll(text).forEach { matchResult ->
        if (matchResult.range.first > lastIndex) {
            val normalText = text.substring(lastIndex, matchResult.range.first).trim()
            if (normalText.isNotEmpty()) {
                list.add(MarkdownBlock.Paragraph(normalText))
            }
        }
        val lang = matchResult.groupValues[1]
        val code = matchResult.groupValues[2].trim()
        list.add(MarkdownBlock.Code(code = code, language = lang))
        lastIndex = matchResult.range.last + 1
    }

    if (lastIndex < text.length) {
        val remaining = text.substring(lastIndex).trim()
        if (remaining.isNotEmpty()) {
            list.add(MarkdownBlock.Paragraph(remaining))
        }
    }

    if (list.isEmpty() && text.isNotEmpty()) {
        list.add(MarkdownBlock.Paragraph(text))
    }

    return list
}

private fun buildAnnotatedMarkdown(text: String) = buildAnnotatedString {
    var lastIndex = 0
    BOLD_REGEX.findAll(text).forEach { matchResult ->
        if (matchResult.range.first > lastIndex) {
            append(text.substring(lastIndex, matchResult.range.first))
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = CyberCyan)) {
            append(matchResult.groupValues[1])
        }
        lastIndex = matchResult.range.last + 1
    }
    if (lastIndex < text.length) {
        append(text.substring(lastIndex))
    }
}
