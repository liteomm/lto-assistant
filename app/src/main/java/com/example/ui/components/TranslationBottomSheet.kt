package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class IndianLanguage(
    val name: String,
    val nativeScript: String,
    val flagEmoji: String = "🇮🇳"
)

val majorIndianLanguages = listOf(
    IndianLanguage("English", "English", "🌐"),
    IndianLanguage("Hindi", "हिंदी", "🇮🇳"),
    IndianLanguage("Odia", "ଓଡ଼ିଆ", "🇮🇳"),
    IndianLanguage("Bengali", "বাংলা", "🇮🇳"),
    IndianLanguage("Tamil", "தமிழ்", "🇮🇳"),
    IndianLanguage("Telugu", "తెలుగు", "🇮🇳"),
    IndianLanguage("Kannada", "ಕನ್ನಡ", "🇮🇳"),
    IndianLanguage("Malayalam", "മലയാളം", "🇮🇳"),
    IndianLanguage("Gujarati", "ગુજરાતી", "🇮🇳"),
    IndianLanguage("Punjabi", "ਪੰਜਾਬੀ", "🇮🇳"),
    IndianLanguage("Marathi", "मराठी", "🇮🇳"),
    IndianLanguage("Urdu", "اردو", "🇮🇳"),
    IndianLanguage("Assamese", "অসমীয়া", "🇮🇳"),
    IndianLanguage("Konkani", "कोंकणी", "🇮🇳"),
    IndianLanguage("Sanskrit", "संस्कृतम्", "🇮🇳"),
    IndianLanguage("Maithili", "मैथिली", "🇮🇳"),
    IndianLanguage("Santali", "ସାନ୍ତାଳୀ", "🇮🇳"),
    IndianLanguage("Nepali", "नेपाली", "🇳🇵")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationBottomSheet(
    onDismiss: () -> Unit,
    onSelectLanguage: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedLang by remember { mutableStateOf<String?>(null) }

    val filteredLanguages = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            majorIndianLanguages
        } else {
            majorIndianLanguages.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.nativeScript.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AmoledBlack,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.2f))
                            .border(1.dp, CyberCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Translate",
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "INSTANT TRANSLATION",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Translate response into 18+ Indian & Global languages",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(GlassSurface)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search language (e.g. Odia, Hindi, Bengali...)", fontSize = 12.sp, color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = CyberCyan, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = GlassSurface,
                    unfocusedContainerColor = GlassSurface,
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = GlassBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Languages Grid / List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredLanguages) { lang ->
                    val isSelected = selectedLang == lang.name
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) CyberCyan.copy(alpha = 0.25f) else GlassSurface)
                            .border(1.dp, if (isSelected) CyberCyan else GlassBorder, RoundedCornerShape(14.dp))
                            .clickable {
                                selectedLang = lang.name
                                onSelectLanguage(lang.name)
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = lang.flagEmoji, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = lang.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = lang.nativeScript,
                                    fontSize = 12.sp,
                                    color = CyberCyan
                                )
                            }
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
