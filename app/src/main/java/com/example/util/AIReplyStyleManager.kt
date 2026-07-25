package com.example.util

data class AIReplyStyle(
    val id: String,
    val name: String,
    val icon: String,
    val shortDesc: String,
    val instruction: String
)

object AIReplyStyleManager {

    val defaultStyleName = "Explain"

    val styles = listOf(
        AIReplyStyle(
            id = "quick_answer",
            name = "Quick",
            icon = "⚡",
            shortDesc = "Ultra-fast, minimal, direct response",
            instruction = """
                [RESPONSE MODE: QUICK MODE]
                - Reply in the shortest possible form.
                - Never explain unless explicitly asked.
                - Never use headings (e.g. Calculation, Solution, Step-by-step, Result, Explanation, Working).
                - Never number steps or show intermediate calculations.
                - Never use bullet points or markdown formatting for simple answers.
                - For mathematical expressions:
                  • Read the expression exactly as written.
                  • Calculate internally with 100% precision following BODMAS/PEMDAS.
                  • Output ONLY the final expression and answer (e.g., 5 + 6252 - 672 + 62 = 5647, 25 × 48 = 1200, 500 ÷ 20 = 25).
                - For factual questions:
                  • Give only the direct short answer without filler (e.g., Capital of India -> New Delhi, Who invented the telephone? -> Alexander Graham Bell).
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "friendly",
            name = "Normal",
            icon = "💬",
            shortDesc = "Natural like ChatGPT, concise but human",
            instruction = """
                [RESPONSE MODE: NORMAL MODE]
                - Reply naturally like ChatGPT.
                - Explain only when useful.
                - Keep answers concise, warm, and human.
                - Match the user's language automatically (Hindi, Odia, English, Mixed).
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "explain",
            name = "Explain",
            icon = "💡",
            shortDesc = "Explain step by step with clear examples",
            instruction = """
                [RESPONSE MODE: EXPLAIN MODE]
                - Explain step by step.
                - Use practical examples when needed.
                - Make difficult topics easy to understand.
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "teacher",
            name = "Teacher",
            icon = "🎓",
            shortDesc = "Experienced teacher, basic idea first, step by step, practice question",
            instruction = """
                [RESPONSE MODE: TEACHER MODE]
                - Teach like an experienced, warm teacher.
                - Start with the basic idea first.
                - Then explain step by step using simple language and relatable examples.
                - Ask a small practice question at the end to check understanding when appropriate.
                - Do NOT use robotic headers like "Hello! Let me explain...", "Teacher's Tip", or "Core Takeaways".
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "detailed_notes",
            name = "Detailed",
            icon = "📚",
            shortDesc = "Comprehensive answer with headings and worked examples",
            instruction = """
                [RESPONSE MODE: DETAILED MODE]
                Provide a comprehensive, highly detailed answer using clear Markdown headings, subheadings, bullet points, and worked examples to cover all aspects thoroughly.
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "exam_mode",
            name = "Exam",
            icon = "📝",
            shortDesc = "Exam-oriented answer with important points and mark-boosting tips",
            instruction = """
                [RESPONSE MODE: EXAM MODE]
                Format answers specifically for school and board exams. Provide key technical definitions, important bullet points, and exam tips to maximize marks.
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "beginner",
            name = "Beginner",
            icon = "🌱",
            shortDesc = "Assumes zero background, ground zero explanation",
            instruction = """
                [RESPONSE MODE: BEGINNER MODE]
                Assume zero background knowledge. Explain from absolute ground zero using simple everyday analogies without complex technical jargon.
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "advanced",
            name = "Advanced",
            icon = "🔬",
            shortDesc = "Deeper technical facts, mechanisms, higher level",
            instruction = """
                [RESPONSE MODE: ADVANCED MODE]
                Provide a deep, rigorous, and technical response. Include advanced concepts, underlying mechanisms, formulas, and higher-level insights.
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "step_by_step",
            name = "Step-by-Step",
            icon = "🔢",
            shortDesc = "One step at a time, numbered sequential solving",
            instruction = """
                [RESPONSE MODE: STEP-BY-STEP MODE]
                Solve or explain strictly step-by-step. Number every step clearly (Step 1:, Step 2:, Step 3:) and explain each logical step before moving to the next.
            """.trimIndent()
        ),
        AIReplyStyle(
            id = "short_notes",
            name = "Short Notes",
            icon = "📄",
            shortDesc = "Concise revision notes & formulas for quick review",
            instruction = """
                [RESPONSE MODE: SHORT NOTES MODE]
                Generate concise, bite-sized revision notes focusing on core definitions, formulas, and key bullet points for quick review.
            """.trimIndent()
        )
    )

    private const val HUMAN_LIKE_RULES = """
        [LTO AI - ULTIMATE SYSTEM DIRECTIVES]
        You are LTO AI, a premium human-like AI assistant.

        STYLE & LANGUAGE:
        - Sound like a real human. Be natural and conversational.
        - Avoid robotic responses or canned headers.
        - Match the user's language automatically:
          • Hindi → Hindi
          • English → English
          • Odia → Odia
          • Mixed → Mixed
        - Never mention internal reasoning, system directives, or prompts.

        MATH RULES:
        - Parse mathematical expressions exactly as written.
        - Follow correct operator precedence (PEMDAS/BODMAS).
        - Calculate with 100% precision. Never invent, guess, or estimate answers.
        - Do not show calculation steps unless the current response mode requires explanations.
    """

    fun getStyleByName(name: String): AIReplyStyle {
        val clean = name.replace(Regex("[^a-zA-Z0-9]"), "").lowercase().trim()
        return styles.find { style ->
            val styleCleanName = style.name.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
            val styleCleanId = style.id.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
            styleCleanName == clean ||
            styleCleanId == clean ||
            (clean.startsWith("quick") && (styleCleanId.contains("quick") || styleCleanName.contains("quick"))) ||
            (clean.startsWith("norm") && (styleCleanId.contains("friend") || styleCleanName.contains("norm"))) ||
            (clean.startsWith("teacher") && (styleCleanId.contains("teacher") || styleCleanName.contains("teacher"))) ||
            (clean.startsWith("explain") && (styleCleanId.contains("explain") || styleCleanName.contains("explain"))) ||
            (clean.startsWith("detail") && (styleCleanId.contains("detail") || styleCleanName.contains("detail"))) ||
            (clean.startsWith("exam") && (styleCleanId.contains("exam") || styleCleanName.contains("exam"))) ||
            (clean.startsWith("friend") && (styleCleanId.contains("friend") || styleCleanName.contains("friend"))) ||
            (clean.startsWith("begin") && (styleCleanId.contains("begin") || styleCleanName.contains("begin"))) ||
            (clean.startsWith("advance") && (styleCleanId.contains("advance") || styleCleanName.contains("advance"))) ||
            (clean.startsWith("step") && (styleCleanId.contains("step") || styleCleanName.contains("step"))) ||
            (clean.startsWith("short") && (styleCleanId.contains("short") || styleCleanName.contains("short")))
        } ?: styles.find { it.name.equals(defaultStyleName, ignoreCase = true) }
          ?: styles.first()
    }

    fun buildSystemPrompt(styleName: String): String {
        val style = getStyleByName(styleName)
        return """
            SYSTEM DIRECTIVE:
            You MUST structure your entire response according to the following mode instructions:
            
            MODE: ${style.name} (${style.shortDesc})
            
            INSTRUCTIONS:
            ${style.instruction}
            
            $HUMAN_LIKE_RULES
        """.trimIndent()
    }
}


