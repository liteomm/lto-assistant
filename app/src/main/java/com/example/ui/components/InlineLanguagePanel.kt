package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
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
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.GlassSurfaceVariant
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextMuted

data class LanguageOption(
    val name: String,
    val nativeScript: String
)

val quickAccessLanguages = listOf(
    LanguageOption("English", "English"),
    LanguageOption("Hindi", "हिन्दी"),
    LanguageOption("Odia", "ଓଡ଼ିଆ"),
    LanguageOption("Bengali", "বাংলা"),
    LanguageOption("Telugu", "తెలుగు"),
    LanguageOption("Tamil", "தமிழ்"),
    LanguageOption("Kannada", "ಕನ್ನಡ"),
    LanguageOption("Malayalam", "മലയാളം"),
    LanguageOption("Marathi", "मराठी"),
    LanguageOption("Gujarati", "ગુજરાતી"),
    LanguageOption("Punjabi", "ਪੰਜਾਬੀ"),
    LanguageOption("Urdu", "اردو"),
    LanguageOption("Assamese", "অসমীয়া")
)

val remainingLanguages = listOf(
    LanguageOption("Konkani", "कोंकणी"),
    LanguageOption("Sanskrit", "संस्कृतम्"),
    LanguageOption("Maithili", "मैथिली"),
    LanguageOption("Santali", "ସାନ୍ତାଳୀ"),
    LanguageOption("Nepali", "नेपाली")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InlineLanguagePanel(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMore by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GlassSurfaceVariant.copy(alpha = 0.95f))
            .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = CyberCyan,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "TRANSLATE RESPONSE TO",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = CyberCyan,
                letterSpacing = 0.5.sp
            )
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val visibleLanguages = if (showMore) (quickAccessLanguages + remainingLanguages) else quickAccessLanguages

            visibleLanguages.forEach { lang ->
                val isSelected = lang.name.equals(selectedLanguage, ignoreCase = true)
                LanguageChip(
                    language = lang,
                    isSelected = isSelected,
                    onClick = { onLanguageSelected(lang.name) }
                )
            }

            // More Languages Toggle Chip
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (showMore) CyberCyan.copy(alpha = 0.25f) else GlassSurface)
                    .border(1.dp, CyberCyan.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                    .clickable { showMore = !showMore }
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (showMore) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = CyberCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (showMore) "Less" else "+ More",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageChip(
    language: LanguageOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        Brush.horizontalGradient(listOf(CyberCyan.copy(alpha = 0.4f), NeonViolet.copy(alpha = 0.4f)))
    } else {
        Brush.horizontalGradient(listOf(GlassSurface, GlassSurface))
    }

    val borderColor = if (isSelected) CyberCyan else GlassBorder
    val textColor = if (isSelected) TextPrimary else TextMuted
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium

    Box(
        modifier = Modifier
            .height(30.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor)
            .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 11.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(CyberCyan)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = language.nativeScript,
                fontSize = 12.sp,
                fontWeight = fontWeight,
                color = textColor
            )
        }
    }
}
