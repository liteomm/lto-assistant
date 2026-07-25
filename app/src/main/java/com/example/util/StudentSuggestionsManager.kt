package com.example.util

import android.content.Context

data class StudentSuggestion(
    val subject: String,
    val icon: String,
    val question: String
)

data class SuggestionsGroup(
    val left: List<StudentSuggestion>,
    val right: List<StudentSuggestion>
)

object StudentSuggestionsManager {

    private val leftSubjects = listOf("Odia", "Mathematics", "History", "Hindi")
    private val rightSubjects = listOf("English", "Science", "Geography", "Physics")

    private val subjectData = mapOf(
        "Odia" to Pair("📘", listOf(
            "ବିଶେଷ୍ୟ କାହାକୁ କୁହାଯାଏ?",
            "ସମାସ କେତେ ପ୍ରକାର?",
            "କାରକ ଓ ବିଭକ୍ତି କ’ଣ?",
            "ବିଶେଷଣ ପଦର ସଂଜ୍ଞା ଦିଅ।",
            "ସନ୍ଧି କାହାକୁ କୁହାଯାଏ?"
        )),
        "Mathematics" to Pair("➗", listOf(
            "What is LCM?",
            "What is quadratic equation?",
            "State Pythagoras theorem.",
            "What is probability formula?",
            "Difference: Prime vs Composite?"
        )),
        "History" to Pair("📜", listOf(
            "Who founded Maurya Empire?",
            "What was the Kalinga War?",
            "Who was Emperor Ashoka?",
            "Significance of Dandi March?",
            "What is Harappan civilization?"
        )),
        "Hindi" to Pair("📙", listOf(
            "संज्ञा किसे कहते हैं?",
            "सर्वनाम के कितने भेद हैं?",
            "विशेषण क्या होता है?",
            "क्रिया किसे कहते हैं?",
            "संधि की परिभाषा क्या है?"
        )),
        "English" to Pair("📗", listOf(
            "What is a noun?",
            "Difference: Adjective vs Adverb?",
            "What is passive voice?",
            "What is a conjunction?",
            "Explain subject-verb agreement."
        )),
        "Science" to Pair("🧪", listOf(
            "Why is the sky blue?",
            "What is photosynthesis?",
            "Explain Newton's first law.",
            "Why does ice float on water?",
            "What is reflection of light?"
        )),
        "Geography" to Pair("🌍", listOf(
            "Why do earthquakes occur?",
            "Name 5 layers of atmosphere.",
            "What causes seasons on Earth?",
            "What are tectonic plates?",
            "What is water cycle?"
        )),
        "Physics" to Pair("⚛️", listOf(
            "What is Ohm's law?",
            "Difference: Mass vs Weight?",
            "What is speed vs velocity?",
            "Define kinetic energy.",
            "What is gravitational force?"
        ))
    )

    fun get8SmartSuggestions(context: Context): SuggestionsGroup {
        val prefs = context.getSharedPreferences("student_suggestions_prefs_v2", Context.MODE_PRIVATE)

        fun pickSuggestion(subject: String): StudentSuggestion {
            val data = subjectData[subject] ?: return StudentSuggestion(subject, "💡", "Ask a question")
            val icon = data.first
            val questions = data.second

            val key = "last_idx_$subject"
            val lastIdx = prefs.getInt(key, -1)

            val nextIdx = if (questions.size <= 1) 0 else {
                var candidate = (0 until questions.size).random()
                if (candidate == lastIdx) {
                    candidate = (candidate + 1) % questions.size
                }
                candidate
            }

            prefs.edit().putInt(key, nextIdx).apply()
            return StudentSuggestion(subject, icon, questions[nextIdx])
        }

        val left = leftSubjects.map { pickSuggestion(it) }
        val right = rightSubjects.map { pickSuggestion(it) }

        return SuggestionsGroup(left = left, right = right)
    }
}

