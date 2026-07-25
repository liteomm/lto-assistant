package com.example.util

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_student_settings", Context.MODE_PRIVATE)

    var themeMode: String
        get() = prefs.getString("theme_mode", "AMOLED Black") ?: "AMOLED Black"
        set(value) = prefs.edit().putString("theme_mode", value).apply()

    var fontSize: String
        get() = prefs.getString("font_size", "Normal") ?: "Normal"
        set(value) = prefs.edit().putString("font_size", value).apply()

    var appLanguage: String
        get() = prefs.getString("app_language", "English") ?: "English"
        set(value) = prefs.edit().putString("app_language", value).apply()

    var performanceMode: String
        get() = prefs.getString("performance_mode", "Performance") ?: "Performance"
        set(value) = prefs.edit().putString("performance_mode", value).apply()

    var conversationMemory: Boolean
        get() = prefs.getBoolean("conversation_memory", true)
        set(value) = prefs.edit().putBoolean("conversation_memory", value).apply()

    var streamingResponse: Boolean
        get() = prefs.getBoolean("streaming_response", true)
        set(value) = prefs.edit().putBoolean("streaming_response", value).apply()

    var markdownFormatting: Boolean
        get() = prefs.getBoolean("markdown_formatting", true)
        set(value) = prefs.edit().putBoolean("markdown_formatting", value).apply()

    var autoScroll: Boolean
        get() = prefs.getBoolean("auto_scroll", true)
        set(value) = prefs.edit().putBoolean("auto_scroll", value).apply()

    var typingAnimation: Boolean
        get() = prefs.getBoolean("typing_animation", true)
        set(value) = prefs.edit().putBoolean("typing_animation", value).apply()

    var hapticFeedback: Boolean
        get() = prefs.getBoolean("haptic_feedback", true)
        set(value) = prefs.edit().putBoolean("haptic_feedback", value).apply()

    var soundEffects: Boolean
        get() = prefs.getBoolean("sound_effects", true)
        set(value) = prefs.edit().putBoolean("sound_effects", value).apply()

    var notifications: Boolean
        get() = prefs.getBoolean("notifications", true)
        set(value) = prefs.edit().putBoolean("notifications", value).apply()

    var aiReplyStyle: String
        get() = prefs.getString("ai_reply_style", AIReplyStyleManager.defaultStyleName) ?: AIReplyStyleManager.defaultStyleName
        set(value) = prefs.edit().putString("ai_reply_style", value).apply()

    // --- NEW PREMIUM CUSTOMIZATION PREFERENCES ---

    var bgAnimation: String
        get() = prefs.getString("bg_animation", "Aurora Lights") ?: "Aurora Lights"
        set(value) = prefs.edit().putString("bg_animation", value).apply()

    var bgSpeed: String
        get() = prefs.getString("bg_speed", "Normal") ?: "Normal"
        set(value) = prefs.edit().putString("bg_speed", value).apply()

    var bgIntensity: String
        get() = prefs.getString("bg_intensity", "Medium") ?: "Medium"
        set(value) = prefs.edit().putString("bg_intensity", value).apply()

    var accentColor: String
        get() = prefs.getString("accent_color", "Cyan") ?: "Cyan"
        set(value) = prefs.edit().putString("accent_color", value).apply()

    var cardTheme: String
        get() = prefs.getString("card_theme", "Glass Card") ?: "Glass Card"
        set(value) = prefs.edit().putString("card_theme", value).apply()

    var bubbleStyle: String
        get() = prefs.getString("bubble_style", "Modern") ?: "Modern"
        set(value) = prefs.edit().putString("bubble_style", value).apply()

    var fontOption: String
        get() = prefs.getString("font_option", "Default Sans") ?: "Default Sans"
        set(value) = prefs.edit().putString("font_option", value).apply()

    var appIcon: String
        get() = prefs.getString("app_icon", "Default Cyan Orb") ?: "Default Cyan Orb"
        set(value) = prefs.edit().putString("app_icon", value).apply()

    var homeLayout: String
        get() = prefs.getString("home_layout", "Classic") ?: "Classic"
        set(value) = prefs.edit().putString("home_layout", value).apply()

    var borderRadius: String
        get() = prefs.getString("border_radius", "Large") ?: "Large"
        set(value) = prefs.edit().putString("border_radius", value).apply()

    var glassBlur: String
        get() = prefs.getString("glass_blur", "Medium") ?: "Medium"
        set(value) = prefs.edit().putString("glass_blur", value).apply()

    var glassTransparency: String
        get() = prefs.getString("glass_transparency", "Medium") ?: "Medium"
        set(value) = prefs.edit().putString("glass_transparency", value).apply()

    var glassGlow: String
        get() = prefs.getString("glass_glow", "Soft") ?: "Soft"
        set(value) = prefs.edit().putString("glass_glow", value).apply()

    var glassShadow: String
        get() = prefs.getString("glass_shadow", "Soft") ?: "Soft"
        set(value) = prefs.edit().putString("glass_shadow", value).apply()

    var glassReflection: String
        get() = prefs.getString("glass_reflection", "Glossy") ?: "Glossy"
        set(value) = prefs.edit().putString("glass_reflection", value).apply()

    var animOpening: String
        get() = prefs.getString("anim_opening", "Fade") ?: "Fade"
        set(value) = prefs.edit().putString("anim_opening", value).apply()

    var animPage: String
        get() = prefs.getString("anim_page", "Slide") ?: "Slide"
        set(value) = prefs.edit().putString("anim_page", value).apply()

    var animCard: String
        get() = prefs.getString("anim_card", "Scale Hover") ?: "Scale Hover"
        set(value) = prefs.edit().putString("anim_card", value).apply()

    var animButton: String
        get() = prefs.getString("anim_button", "Ripple") ?: "Ripple"
        set(value) = prefs.edit().putString("anim_button", value).apply()

    var animChat: String
        get() = prefs.getString("anim_chat", "Slide Up Fade") ?: "Slide Up Fade"
        set(value) = prefs.edit().putString("anim_chat", value).apply()

    var animTyping: String
        get() = prefs.getString("anim_typing", "Blinking Dot") ?: "Blinking Dot"
        set(value) = prefs.edit().putString("anim_typing", value).apply()

    var animSend: String
        get() = prefs.getString("anim_send", "Fly Off Up") ?: "Fly Off Up"
        set(value) = prefs.edit().putString("anim_send", value).apply()

    var soundTyping: Boolean
        get() = prefs.getBoolean("sound_typing", true)
        set(value) = prefs.edit().putBoolean("sound_typing", value).apply()

    var soundSend: Boolean
        get() = prefs.getBoolean("sound_send", true)
        set(value) = prefs.edit().putBoolean("sound_send", value).apply()

    var soundNotification: Boolean
        get() = prefs.getBoolean("sound_notification", true)
        set(value) = prefs.edit().putBoolean("sound_notification", value).apply()

    var hapticLevel: String
        get() = prefs.getString("haptic_level", "Medium") ?: "Medium"
        set(value) = prefs.edit().putString("haptic_level", value).apply()

    var lastTranslationLanguage: String
        get() = prefs.getString("last_translation_language", "Hindi") ?: "Hindi"
        set(value) = prefs.edit().putString("last_translation_language", value).apply()

    var autoTranslateAI: Boolean
        get() = prefs.getBoolean("auto_translate_ai", false)
        set(value) = prefs.edit().putBoolean("auto_translate_ai", value).apply()

    fun resetToDefaults() {
        prefs.edit().clear().apply()
    }
}
