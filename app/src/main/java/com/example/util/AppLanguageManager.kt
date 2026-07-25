package com.example.util

data class TranslationStrings(
    val greetingMorning: String,
    val greetingAfternoon: String,
    val greetingEvening: String,
    val greetingNight: String = "Good Night",
    val smartRouterOnline: String,
    val newChat: String,
    val howCanIHelp: String,
    val studentSuggestionsHeader: String,
    val askPlaceholder: String,
    val chatTab: String,
    val historyTab: String,
    val settingsTab: String,
    val chatHistoryTitle: String,
    val searchChats: String,
    val pinnedChats: String,
    val recentChats: String,
    val noHistory: String,
    val noHistorySub: String,
    val profileTitle: String,
    val aiCredits: String,
    val studyStreak: String,
    val tokensUsed: String,
    val themeSettingTitle: String,
    val fontSizeSettingTitle: String,
    val appLanguageSettingTitle: String,
    val performanceModeTitle: String,
    val chatPreferencesTitle: String,
    val notificationsTitle: String,
    val conversationMemoryLabel: String,
    val conversationMemorySub: String = "Remember past study context across turns",
    val streamingResponseLabel: String,
    val streamingResponseSub: String = "Stream response tokens in real-time",
    val markdownLabel: String,
    val markdownFormattingLabel: String = "Markdown Formatting",
    val markdownFormattingSub: String = "Render math, code snippets & tables",
    val autoScrollLabel: String,
    val autoScrollSub: String = "Auto scroll down on incoming AI replies",
    val typingAnimationLabel: String,
    val hapticFeedbackLabel: String,
    val hapticFeedbackSub: String = "Tactile vibration feedback on interactions",
    val notificationsLabel: String = "Notifications",
    val notificationsSub: String = "Receive alerts when long AI replies complete",
    val soundEffectsLabel: String,
    val pushNotificationsLabel: String,
    val exportChatLabel: String,
    val exportChatSub: String,
    val clearAllHistoryLabel: String,
    val clearAllHistorySub: String,
    val clearHistoryLabel: String = "Clear All Chat History",
    val clearHistorySub: String = "Delete all past study conversations permanently",
    val resetSettingsLabel: String,
    val resetSettingsSub: String,
    val clearDialogTitle: String,
    val clearDialogText: String,
    val resetDialogTitle: String,
    val resetDialogText: String,
    val confirm: String,
    val cancel: String,
    val settingSavedToast: String,
    val settingsResetToast: String,
    val copiedToast: String,
    val suggestedFollowUps: String,
    val routedBy: String
)

object AppLanguageManager {

    val supportedLanguages = listOf(
        "English",
        "Hindi",
        "Odia (ଓଡ଼ିଆ)",
        "Bengali",
        "Telugu",
        "Tamil",
        "Kannada",
        "Malayalam",
        "Marathi",
        "Gujarati",
        "Punjabi",
        "Assamese",
        "Urdu"
    )

    fun getStrings(language: String): TranslationStrings {
        val baseStrings = when {
            language.contains("Hindi", ignoreCase = true) || language.contains("हिंदी") -> TranslationStrings(
                greetingMorning = "शुभ प्रभात",
                greetingAfternoon = "शुभ दोपहर",
                greetingEvening = "शुभ संध्या",
                greetingNight = "शुभ रात्रि",
                smartRouterOnline = "स्मार्ट राउटर ऑनलाइन",
                newChat = "नई चैट",
                howCanIHelp = "आज पढ़ाई में मैं आपकी क्या मदद कर सकता हूँ?",
                studentSuggestionsHeader = "छात्र स्मार्ट सुझाव • टैप करते ही तुरंत उत्तर पाएं",
                askPlaceholder = "स्टूडेंट एआई से कुछ भी पूछें...",
                chatTab = "चैट",
                historyTab = "हिस्ट्री",
                settingsTab = "सेटिंग्स",
                chatHistoryTitle = "चैट इतिहास",
                searchChats = "पढ़ाई चैट खोजें...",
                pinnedChats = "पिन की गई बातचीत",
                recentChats = "हाल की बातचीत",
                noHistory = "कोई पुराना चैट इतिहास नहीं मिला",
                noHistorySub = "एआई के साथ पढ़ाई शुरू करने के लिए नई चैट शुरू करें!",
                profileTitle = "स्टूडेंट एआई प्रो",
                aiCredits = "एआई क्रेडिट्स",
                studyStreak = "स्टडी स्ट्रिक",
                tokensUsed = "टोकन उपयोग",
                themeSettingTitle = "थीम मोड",
                fontSizeSettingTitle = "फ़ॉन्ट का आकार",
                appLanguageSettingTitle = "ऐप की भाषा",
                performanceModeTitle = "परफॉर्मेंस मोड",
                chatPreferencesTitle = "चैट प्राथमिकताएं",
                notificationsTitle = "सूचनाएं",
                conversationMemoryLabel = "बातचीत की याददाश्त (मेमोरी)",
                streamingResponseLabel = "स्ट्रीमिंग उत्तर",
                markdownLabel = "मार्कडाउन फॉर्मेटिंग",
                autoScrollLabel = "ऑटो स्क्रॉल",
                typingAnimationLabel = "टाइपिंग एनिमेशन",
                hapticFeedbackLabel = "हाइप्टिक फीडबैक",
                soundEffectsLabel = "ध्वनि प्रभाव",
                pushNotificationsLabel = "पुश नोटिफिकेशन",
                exportChatLabel = "चैट निर्यात करें (TXT / PDF)",
                exportChatSub = "बातचीत के रिकॉर्ड सहेजें या साझा करें",
                clearAllHistoryLabel = "सभी चैट इतिहास साफ़ करें",
                clearAllHistorySub = "सभी पुरानी बातचीत स्थायी रूप से हटाएं",
                resetSettingsLabel = "सभी सेटिंग्स रीसेट करें",
                resetSettingsSub = "मूल डिफ़ॉल्ट सेटिंग्स पुनर्स्थापित करें",
                clearDialogTitle = "चैट इतिहास साफ़ करें?",
                clearDialogText = "क्या आप सभी छात्र चैट इतिहास को स्थायी रूप से हटाना चाहते हैं?",
                resetDialogTitle = "सभी सेटिंग्स रीसेट करें?",
                resetDialogText = "क्या आप सभी सेटिंग्स को डिफ़ॉल्ट मानों पर पुनर्स्थापित करना चाहते हैं?",
                confirm = "पुष्टि करें",
                cancel = "रद्द करें",
                settingSavedToast = "सेटिंग सहेजी गई और लागू हुई!",
                settingsResetToast = "सभी सेटिंग्स डिफ़ॉल्ट पर रीसेट कर दी गईं!",
                copiedToast = "क्लिपबोर्ड पर कॉपी किया गया!",
                suggestedFollowUps = "सुझाए गए अनुवर्ती प्रश्न:",
                routedBy = "रूट किया गया:"
            )

            else -> TranslationStrings(
                greetingMorning = "Good Morning",
                greetingAfternoon = "Good Afternoon",
                greetingEvening = "Good Evening",
                greetingNight = "Good Night",
                smartRouterOnline = "LTO Assistant Online",
                newChat = "New Chat",
                howCanIHelp = "How can I help you in your studies today?",
                studentSuggestionsHeader = "Student Smart Suggestions • Tapping sends immediately",
                askPlaceholder = "Ask LTO Assistant anything...",
                chatTab = "Chat",
                historyTab = "History",
                settingsTab = "Settings",
                chatHistoryTitle = "Chat History",
                searchChats = "Search study chats...",
                pinnedChats = "Pinned Conversations",
                recentChats = "Recent Conversations",
                noHistory = "No past study chats found",
                noHistorySub = "Start a new chat to begin studying with LTO Assistant!",
                profileTitle = "LTO ASSISTANT PRO",
                aiCredits = "AI Credits",
                studyStreak = "Study Streak",
                tokensUsed = "Tokens Used",
                themeSettingTitle = "THEME MODE",
                fontSizeSettingTitle = "FONT SIZE",
                appLanguageSettingTitle = "APP LANGUAGE",
                performanceModeTitle = "PERFORMANCE MODE",
                chatPreferencesTitle = "CHAT PREFERENCES",
                notificationsTitle = "NOTIFICATIONS",
                conversationMemoryLabel = "Conversation Memory",
                streamingResponseLabel = "Streaming Response",
                markdownLabel = "Markdown Formatting",
                autoScrollLabel = "Auto Scroll",
                typingAnimationLabel = "Typing Animation",
                hapticFeedbackLabel = "Haptic Feedback",
                soundEffectsLabel = "Sound Effects",
                pushNotificationsLabel = "Push Notifications",
                exportChatLabel = "Export Chat (TXT / PDF)",
                exportChatSub = "Share or save conversation records",
                clearAllHistoryLabel = "Clear All Chat History",
                clearAllHistorySub = "Delete all past conversations permanently",
                resetSettingsLabel = "Reset All Settings",
                resetSettingsSub = "Restore default app preferences",
                clearDialogTitle = "Clear Chat History?",
                clearDialogText = "Are you sure you want to delete all LTO Assistant chat history and sessions permanently?",
                resetDialogTitle = "Reset All Settings?",
                resetDialogText = "Are you sure you want to restore all settings to default values?",
                confirm = "Confirm",
                cancel = "Cancel",
                settingSavedToast = "Setting saved & applied!",
                settingsResetToast = "All settings reset to default!",
                copiedToast = "Copied to clipboard!",
                suggestedFollowUps = "Suggested Follow-up Questions:",
                routedBy = "Routed:"
            )
        }

        // Apply language-specific greetings for all supported languages
        return when {
            language.contains("Odia", ignoreCase = true) || language.contains("ଓଡ଼ିଆ") -> baseStrings.copy(
                greetingMorning = "ଶୁଭ ସକାଳ",
                greetingAfternoon = "ଶୁଭ ଅପରାହ୍ଣ",
                greetingEvening = "ଶୁଭ ସନ୍ଧ୍ୟା",
                greetingNight = "ଶୁଭ ରାତ୍ରି"
            )
            language.contains("Bengali", ignoreCase = true) || language.contains("বাংলা") -> baseStrings.copy(
                greetingMorning = "শুভ সকাল",
                greetingAfternoon = "শুভ অপরাহ্ন",
                greetingEvening = "শুভ সন্ধ্যা",
                greetingNight = "শুভ রাত্রি"
            )
            language.contains("Telugu", ignoreCase = true) || language.contains("తెలుగు") -> baseStrings.copy(
                greetingMorning = "శుభోదయం",
                greetingAfternoon = "శుభ మధ్యాహ్నం",
                greetingEvening = "శుభ సాయంత్రం",
                greetingNight = "శుభ రాత్రి"
            )
            language.contains("Tamil", ignoreCase = true) || language.contains("தமிழ்") -> baseStrings.copy(
                greetingMorning = "காலை வணக்கம்",
                greetingAfternoon = "மதிய வணக்கம்",
                greetingEvening = "மாலை வணக்கம்",
                greetingNight = "இரவு வணக்கம்"
            )
            language.contains("Kannada", ignoreCase = true) || language.contains("ಕನ್ನಡ") -> baseStrings.copy(
                greetingMorning = "ಶುಭೋದಯ",
                greetingAfternoon = "ಶುಭ ಮಧ್ಯಾಹ್ನ",
                greetingEvening = "ಶುಭ ಸಂಜೆ",
                greetingNight = "ಶುಭ ರಾತ್ರಿ"
            )
            language.contains("Malayalam", ignoreCase = true) || language.contains("മലയാളം") -> baseStrings.copy(
                greetingMorning = "സുപ്രഭാതം",
                greetingAfternoon = "ഉച്ചവന്ദനം",
                greetingEvening = "സന്ധ്യാവന്ദനം",
                greetingNight = "ശുഭ രാത്രി"
            )
            language.contains("Marathi", ignoreCase = true) || language.contains("मराठी") -> baseStrings.copy(
                greetingMorning = "शुभ प्रभात",
                greetingAfternoon = "शुभ दुपार",
                greetingEvening = "शुभ संध्या",
                greetingNight = "शुभ रात्री"
            )
            language.contains("Gujarati", ignoreCase = true) || language.contains("ગુજરાતી") -> baseStrings.copy(
                greetingMorning = "શુભ પ્રભાત",
                greetingAfternoon = "શુભ બપોર",
                greetingEvening = "શુભ સંધ્યા",
                greetingNight = "શુભ રાત્રિ"
            )
            language.contains("Punjabi", ignoreCase = true) || language.contains("ਪੰਜਾਬੀ") -> baseStrings.copy(
                greetingMorning = "ਸ਼ੁਭ ਸਵੇਰ",
                greetingAfternoon = "ਸ਼ੁਭ ਦੁਪਹਿਰ",
                greetingEvening = "ਸ਼ੁਭ ਸ਼ਾਮ",
                greetingNight = "ਸ਼ੁਭ ਰਾਤ"
            )
            language.contains("Assamese", ignoreCase = true) || language.contains("অসমীয়া") -> baseStrings.copy(
                greetingMorning = "শুভ প্ৰভাত",
                greetingAfternoon = "শুভ অপৰাহ্ন",
                greetingEvening = "শুভ সন্ধিয়া",
                greetingNight = "শুভ ৰাত্ৰি"
            )
            language.contains("Urdu", ignoreCase = true) || language.contains("اردو") -> baseStrings.copy(
                greetingMorning = "صبح بخیر",
                greetingAfternoon = "سہ پہر بخیر",
                greetingEvening = "شام بخیر",
                greetingNight = "شب بخیر"
            )
            else -> baseStrings
        }
    }
}
