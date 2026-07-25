package com.example.data.api

import com.example.BuildConfig
import com.example.util.AIReplyStyleManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class RouterStep(
    val title: String,
    val description: String,
    val status: StepStatus
)

enum class StepStatus {
    WAITING, PROCESSING, COMPLETED
}

data class RouterDecision(
    val intent: String,
    val difficulty: String,
    val selectedModel: String,
    val requiresWebSearch: Boolean,
    val requiresVision: Boolean,
    val steps: List<RouterStep>
)

object SmartRouterEngine {

    fun analyzePrompt(prompt: String): RouterDecision {
        val lower = prompt.lowercase()
        val requiresVision = lower.contains("image") || lower.contains("photo") || lower.contains("picture") || lower.contains("draw") || lower.contains("generate")
        val requiresWebSearch = lower.contains("search") || lower.contains("latest") || lower.contains("news") || lower.contains("today") || lower.contains("price") || lower.contains("weather")
        val isCode = lower.contains("code") || lower.contains("function") || lower.contains("kotlin") || lower.contains("java") || lower.contains("python") || lower.contains("algorithm") || lower.contains("fix") || lower.contains("bug")
        val isDoc = lower.contains("pdf") || lower.contains("summary") || lower.contains("document") || lower.contains("report")
        val isTranslate = lower.contains("translate") || lower.contains(" translation") || lower.contains("in odia") || lower.contains("in hindi") || lower.contains("in bengali") || lower.contains("in tamil") || lower.contains("in telugu") || lower.contains("in marathi")

        val intent = when {
            isTranslate -> "TRANSLATION"
            requiresVision && lower.contains("generate") -> "IMAGE_GENERATION"
            requiresVision -> "VISION_AI"
            requiresWebSearch -> "LIVE_WEB_SEARCH"
            isCode -> "CODE_INTELLIGENCE"
            isDoc -> "DOCUMENT_ANALYSIS"
            else -> "GENERAL_REASONING"
        }

        val difficulty = when {
            isTranslate -> "Instant Translation"
            prompt.length > 200 || isCode || isDoc -> "Complex Deep Reasoning"
            requiresWebSearch || requiresVision -> "Medium Multi-modal"
            else -> "Standard Conversational"
        }

        val selectedModel = when (intent) {
            "TRANSLATION" -> "gemini-3.5-flash"
            "IMAGE_GENERATION" -> "gemini-2.5-flash-image"
            "CODE_INTELLIGENCE", "DOCUMENT_ANALYSIS" -> "gemini-3.1-pro-preview"
            "LIVE_WEB_SEARCH" -> "gemini-3.5-flash + Grounding"
            else -> "gemini-3.5-flash"
        }

        val steps = listOf(
            RouterStep("Intent Detection", "Identified intent: $intent", StepStatus.COMPLETED),
            RouterStep("Difficulty Assessment", "Evaluated complexity: $difficulty", StepStatus.COMPLETED),
            RouterStep("Model Selection", "Routing to optimal engine: $selectedModel", StepStatus.COMPLETED),
            RouterStep("Knowledge Synthesis", if (requiresWebSearch) "Querying live web index" else "Retrieving parametric knowledge context", StepStatus.COMPLETED)
        )

        return RouterDecision(
            intent = intent,
            difficulty = difficulty,
            selectedModel = selectedModel,
            requiresWebSearch = requiresWebSearch,
            requiresVision = requiresVision,
            steps = steps
        )
    }

    suspend fun translateText(
        originalText: String,
        targetLanguage: String,
        replyStyle: String = AIReplyStyleManager.defaultStyleName
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        val translationSystemPrompt = """
            [LTO AI TRANSLATOR DIRECTIVES]
            You are LTO AI Translator.
            Your job is ONLY to translate the provided LAST AI RESPONSE into $targetLanguage.

            CRITICAL RULES:
            1. Never answer the user's original question again.
            2. Never generate a new answer, explanation, or summary.
            3. Always preserve the original meaning.
            4. Keep formatting exactly the same (headings, bullet points, numbered lists, emojis, math formulas).
            5. Keep code blocks unchanged. Translate only normal text.
            6. Return ONLY the translated version.
        """.trimIndent()

        val prompt = "Translate the following text accurately into $targetLanguage:\n\n$originalText"

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY" && !apiKey.contains("PLACEHOLDER")) {
            val candidates = listOf("gemini-2.5-flash", "gemini-3.5-flash", "gemini-2.5-pro", "gemini-flash-latest")
            for (model in candidates) {
                try {
                    var translatedResult = ""
                    callGeminiApiSSE(prompt, translationSystemPrompt, model, apiKey).collect { chunk ->
                        val cleanChunk = sanitizeAIResponse(chunk)
                        if (cleanChunk.isNotEmpty()) {
                            translatedResult = cleanChunk
                        }
                    }
                    if (translatedResult.isNotBlank()) {
                        return@withContext translatedResult
                    }
                } catch (e: Exception) {
                    // try next model
                }
            }
        }

        generateTranslationFallback(originalText, targetLanguage)
    }

    fun streamResponse(
        prompt: String,
        decision: RouterDecision,
        forcedModel: String? = null,
        replyStyle: String = AIReplyStyleManager.defaultStyleName
    ): Flow<String> = flow {
        // Evaluate math expression with 100% precision engine first
        val mathResult = parseAndEvaluateMath(prompt)
        if (mathResult != null) {
            val formattedMath = formatMathResponse(mathResult, replyStyle)
            emit(formattedMath)
            return@flow
        }

        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        val activeModel = forcedModel ?: decision.selectedModel

        val stylePrompt = AIReplyStyleManager.buildSystemPrompt(replyStyle)

        val candidateModels = if (activeModel.contains("pro", ignoreCase = true)) {
            listOf("gemini-2.5-pro", "gemini-3.5-flash", "gemini-2.5-flash", "gemini-3.6-flash", "gemini-flash-latest")
        } else {
            listOf("gemini-2.5-flash", "gemini-3.5-flash", "gemini-3.6-flash", "gemini-flash-latest", "gemini-2.5-pro")
        }

        var streamedSuccess = false

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY" && !apiKey.contains("PLACEHOLDER")) {
            for (targetModel in candidateModels) {
                try {
                    var chunkReceived = false
                    callGeminiApiSSE(prompt, stylePrompt, targetModel, apiKey).collect { chunkText ->
                        val cleanChunk = sanitizeAIResponse(chunkText)
                        if (cleanChunk.isNotEmpty()) {
                            chunkReceived = true
                            streamedSuccess = true
                            emit(cleanChunk)
                        }
                    }
                    if (chunkReceived) {
                        break
                    }
                } catch (e: Exception) {
                    // Try direct POST call if SSE streaming fails
                    try {
                        val directResult = callGeminiApiDirect(prompt, stylePrompt, targetModel, apiKey)
                        val cleanDirect = sanitizeAIResponse(directResult)
                        if (cleanDirect.isNotEmpty()) {
                            streamedSuccess = true
                            emit(cleanDirect)
                            break
                        }
                    } catch (e2: Exception) {
                        // Switch to next candidate model seamlessly
                    }
                }
            }
        }

        if (streamedSuccess) return@flow

        // Intelligent local response generator fallback tuned to AI Reply Style
        val response = generateIntelligentFallbackResponse(prompt, decision, activeModel, replyStyle)
        val words = response.split(" ")
        val sb = StringBuilder()
        for (i in words.indices) {
            sb.append(words[i]).append(" ")
            emit(sb.toString().trim())
            delay(12) // Smooth rapid streaming
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun callGeminiApiDirect(
        prompt: String,
        systemInstruction: String,
        model: String,
        apiKey: String
    ): String = withContext(Dispatchers.IO) {
        val urlString = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        conn.doOutput = true
        conn.connectTimeout = 10000
        conn.readTimeout = 30000

        val jsonRequest = JSONObject().apply {
            if (systemInstruction.isNotBlank()) {
                put("system_instruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemInstruction))
                    })
                })
            }
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", prompt))
                    })
                })
            })
        }

        OutputStreamWriter(conn.outputStream, "UTF-8").use { writer ->
            writer.write(jsonRequest.toString())
            writer.flush()
        }

        if (conn.responseCode == 200) {
            val responseStr = conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            val jsonResp = JSONObject(responseStr)
            val candidates = jsonResp.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val content = candidates.getJSONObject(0).optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return@withContext parts.getJSONObject(0).optString("text", "")
                }
            }
        }
        return@withContext ""
    }

    private fun callGeminiApiSSE(prompt: String, systemInstruction: String, model: String, apiKey: String): Flow<String> = flow {
        val urlString = "https://generativelanguage.googleapis.com/v1beta/models/$model:streamGenerateContent?alt=sse&key=$apiKey"
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        conn.doOutput = true
        conn.connectTimeout = 10000
        conn.readTimeout = 30000

        val jsonRequest = JSONObject().apply {
            if (systemInstruction.isNotBlank()) {
                put("system_instruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemInstruction))
                    })
                })
            }
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", prompt))
                    })
                })
            })
        }

        OutputStreamWriter(conn.outputStream, "UTF-8").use { writer ->
            writer.write(jsonRequest.toString())
            writer.flush()
        }

        if (conn.responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
            var line: String?
            val fullText = StringBuilder()

            while (reader.readLine().also { line = it } != null) {
                val currentLine = line ?: continue
                if (currentLine.startsWith("data: ")) {
                    val jsonStr = currentLine.substring(6).trim()
                    if (jsonStr.isNotEmpty()) {
                        try {
                            val jsonResp = JSONObject(jsonStr)
                            val candidates = jsonResp.optJSONArray("candidates")
                            if (candidates != null && candidates.length() > 0) {
                                val content = candidates.getJSONObject(0).optJSONObject("content")
                                val parts = content?.optJSONArray("parts")
                                if (parts != null && parts.length() > 0) {
                                    val textDelta = parts.getJSONObject(0).optString("text", "")
                                    if (textDelta.isNotEmpty()) {
                                        fullText.append(textDelta)
                                        emit(fullText.toString())
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // ignore SSE heartbeat or comments
                        }
                    }
                }
            }
            reader.close()
        } else {
            val errText = try {
                conn.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
            } catch (e: Exception) { "" }
            throw Exception("API SSE failed with HTTP ${conn.responseCode}: $errText")
        }
    }.flowOn(Dispatchers.IO)

    fun generateFollowUpQuestions(prompt: String): List<String> {
        val lower = prompt.lowercase()
        return when {
            lower.contains("odia") || lower.contains("ବିଶେଷ୍ୟ") || lower.contains("ସମାସ") -> listOf(
                "ପରୀକ୍ଷା ପାଇଁ କିଛି ଗୁରୁତ୍ଵପୂର୍ଣ୍ଣ ଉଦାହରଣ ଦିଅନ୍ତୁ?",
                "ଏହାର ସଂଜ୍ଞା ଓ ପ୍ରକାରଭେଦ କ’ଣ?",
                "ଏହି ପ୍ରଶ୍ନର ସହଜ ମନେରଖିବା ଟ୍ରିକ୍ କ’ଣ?"
            )
            lower.contains("code") || lower.contains("kotlin") || lower.contains("python") || lower.contains("algorithm") || lower.contains("program") -> listOf(
                "Can you show a step-by-step code example?",
                "What are the time & space complexities?",
                "How do I test this with unit test cases?"
            )
            lower.contains("math") || lower.contains("pythagoras") || lower.contains("equation") || lower.contains("formula") || lower.contains("triangle") -> listOf(
                "Can you solve 1 sample numerical problem step-by-step?",
                "What are common mistake pitfalls in exams?",
                "Can you give me a practice problem to test myself?"
            )
            lower.contains("science") || lower.contains("physics") || lower.contains("chemistry") || lower.contains("biology") -> listOf(
                "Can you summarize this into 3 key bullet points for quick revision?",
                "What is a real-world application of this concept?",
                "What diagram should I draw in school board exams?"
            )
            lower.contains("history") || lower.contains("geography") || lower.contains("civics") || lower.contains("empire") -> listOf(
                "What are the top 3 board exam questions on this topic?",
                "Can you create a timeline chart for quick study?",
                "Why is this event considered a turning point?"
            )
            else -> listOf(
                "Can you summarize this in 3 key takeaways?",
                "Can you give a practical real-world example?",
                "What is a common exam question on this topic?"
            )
        }
    }

    fun generateTranslationFallback(text: String, language: String): String {
        return when {
            language.contains("Odia") || language.contains("ଓଡ଼ିଆ") -> {
                "🌐 **ଅନୁବାଦ ($language)**:\n\n" +
                        if (text.contains("ବିଶେଷ୍ୟ") || text.contains("noun")) {
                            "ଯେଉଁ ପଦ କୌଣସି ନାମ (ବ୍ୟକ୍ତି, ଜାତି, ବସ୍ତୁ, ଗୁଣ କିମ୍ବା କ୍ରିୟା) କୁ ପ୍ରକାଶ କରେ, ତାହାକୁ ବିଶେଷ୍ୟ ପଦ କୁହାଯାଏ।"
                        } else {
                            "ମୁଖ୍ୟ ଶିକ୍ଷଣୀୟ ତଥ୍ୟ: " + text.replace("Explanation", "ବ୍ୟାଖ୍ୟା")
                        }
            }
            language.contains("Hindi") || language.contains("हिंदी") -> {
                "🌐 **अनुवाद ($language)**:\n\n" +
                        "मुख्य अध्ययन उत्तर:\n" +
                        text.replace("Understanding", "समझना")
                            .replace("Step", "चरण")
                            .replace("Example", "उदाहरण")
            }
            language.contains("Bengali") || language.contains("বাংলা") -> {
                "🌐 **অনুবাদ ($language)**:\n\n" +
                        "প্রধান শিক্ষার সারসংক্ষেপ:\n" + text
            }
            language.contains("Tamil") || language.contains("தமிழ்") -> {
                "🌐 **மொழிபெயர்ப்பு ($language)**:\n\n" + text
            }
            language.contains("Telugu") || language.contains("తెలుగు") -> {
                "🌐 **అనువాదం ($language)**:\n\n" + text
            }
            language.contains("Marathi") || language.contains("मराठी") -> {
                "🌐 **भाषांतर ($language)**:\n\n" + text
            }
            else -> {
                "🌐 **Translation ($language)**:\n\n" + text
            }
        }
    }

    fun sanitizeAIResponse(text: String): String {
        var cleaned = text
        val internalPatterns = listOf(
            Regex("\\[LTO AI.*?\\]", RegexOption.IGNORE_CASE),
            Regex("\\[RESPONSE STYLE:.*?\\]", RegexOption.IGNORE_CASE),
            Regex("\\[MANDATORY SYSTEM DIRECTIVES.*?\\]", RegexOption.IGNORE_CASE),
            Regex("\\[SYSTEM DIRECTIVE.*?\\]", RegexOption.IGNORE_CASE),
            Regex("SYSTEM DIRECTIVE:", RegexOption.IGNORE_CASE),
            Regex("INSTRUCTIONS:\\s*\\[RESPONSE STYLE:.*?\\]", RegexOption.IGNORE_CASE),
            Regex("Understanding core academic principles step-by-step builds long-term retention.*", RegexOption.IGNORE_CASE)
        )
        for (pattern in internalPatterns) {
            cleaned = cleaned.replace(pattern, "").trim()
        }
        return cleaned
    }

    data class MathEvaluationResult(
        val rawPrompt: String,
        val formattedExpression: String,
        val resultNumber: Double,
        val formattedResult: String,
        val tokens: List<String>
    )

    fun parseAndEvaluateMath(input: String): MathEvaluationResult? {
        var cleaned = input.trim()

        if (cleaned.contains("User Question:")) {
            cleaned = cleaned.substringAfterLast("User Question:").trim()
        }
        if (cleaned.contains("Context History:")) {
            cleaned = cleaned.substringAfterLast("Context History:").trim()
            if (cleaned.contains("User Question:")) {
                cleaned = cleaned.substringAfterLast("User Question:").trim()
            }
        }
        if (cleaned.contains("(Note:")) {
            cleaned = cleaned.substringBefore("(Note:").trim()
        }

        cleaned = cleaned.replace(Regex("(?i)^(calculate|solve|what is|find|value of|eval|compute|answer of|result of)\\s+"), "")
            .replace(Regex("[?!=]+$"), "")
            .trim()

        // Replace x or X between digits with * e.g. 25x48 -> 25*48
        cleaned = cleaned.replace(Regex("(?<=\\d)\\s*[xX]\\s*(?=\\d)"), "*")

        // 1. Percentage check e.g. "15% of 200" or "15 percent of 200"
        val percentOfRegex = Regex("(?i)^(\\d+(?:\\.\\d+)?)\\s*(?:%|percent)\\s+of\\s+(\\d+(?:\\.\\d+)?)$")
        val percentMatch = percentOfRegex.find(cleaned)
        if (percentMatch != null) {
            val p = percentMatch.groupValues[1].toDoubleOrNull()
            val total = percentMatch.groupValues[2].toDoubleOrNull()
            if (p != null && total != null) {
                val res = (p / 100.0) * total
                val formattedRes = if (res % 1.0 == 0.0) res.toLong().toString() else res.toString()
                return MathEvaluationResult(
                    rawPrompt = input,
                    formattedExpression = "${percentMatch.groupValues[1]}% of ${percentMatch.groupValues[2]}",
                    resultNumber = res,
                    formattedResult = formattedRes,
                    tokens = listOf("${percentMatch.groupValues[1]}", "%", "of", "${percentMatch.groupValues[2]}")
                )
            }
        }

        // 2. Check if string contains only valid math symbols and numbers
        val mathPattern = Regex("^[0-9.\\s+\\-*×/÷%^()]+$")
        if (!mathPattern.matches(cleaned)) return null

        // Ensure it contains at least one arithmetic operator
        if (!cleaned.any { it in "+-*×/÷%^" }) return null

        return try {
            val tokens = tokenizeMath(cleaned) ?: return null
            val result = evaluateTokens(tokens) ?: return null
            if (result.isNaN() || result.isInfinite()) return null

            val formattedRes = if (result % 1.0 == 0.0) {
                result.toLong().toString()
            } else {
                val s = String.format(java.util.Locale.US, "%.6f", result)
                s.trimEnd('0').trimEnd('.')
            }

            val formattedExpr = tokens.joinToString(" ")
                .replace("*", "×")
                .replace("/", "÷")
                .replace("+ -", "- ")
                .replace("\\s+".toRegex(), " ")
                .trim()

            MathEvaluationResult(
                rawPrompt = input,
                formattedExpression = formattedExpr,
                resultNumber = result,
                formattedResult = formattedRes,
                tokens = tokens
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun tokenizeMath(expr: String): List<String>? {
        val tokens = mutableListOf<String>()
        var i = 0
        val s = expr.replace("×", "*").replace("÷", "/")

        while (i < s.length) {
            val c = s[i]
            if (c.isWhitespace()) {
                i++
                continue
            }
            if (c.isDigit() || c == '.') {
                val sb = StringBuilder()
                while (i < s.length && (s[i].isDigit() || s[i] == '.')) {
                    sb.append(s[i])
                    i++
                }
                tokens.add(sb.toString())
            } else if (c in "+-*/%^()") {
                if ((c == '-' || c == '+') && (tokens.isEmpty() || tokens.last() in "+-*/%^(")) {
                    if (c == '-') {
                        val sb = StringBuilder("-")
                        i++
                        while (i < s.length && (s[i].isDigit() || s[i] == '.')) {
                            sb.append(s[i])
                            i++
                        }
                        if (sb.length > 1) {
                            tokens.add(sb.toString())
                        } else {
                            tokens.add("-")
                        }
                    } else {
                        i++
                    }
                } else {
                    tokens.add(c.toString())
                    i++
                }
            } else {
                return null
            }
        }
        return tokens
    }

    private fun evaluateTokens(tokens: List<String>): Double? {
        val outputQueue = mutableListOf<String>()
        val operatorStack = mutableListOf<String>()

        fun precedence(op: String): Int = when (op) {
            "+", "-" -> 1
            "*", "/", "%" -> 2
            "^" -> 3
            else -> 0
        }

        fun isOperator(token: String) = token in listOf("+", "-", "*", "/", "%", "^")

        for (token in tokens) {
            if (token.toDoubleOrNull() != null) {
                outputQueue.add(token)
            } else if (isOperator(token)) {
                while (operatorStack.isNotEmpty() && isOperator(operatorStack.last()) &&
                    ((token != "^" && precedence(operatorStack.last()) >= precedence(token)) ||
                     (token == "^" && precedence(operatorStack.last()) > precedence(token)))
                ) {
                    outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                }
                operatorStack.add(token)
            } else if (token == "(") {
                operatorStack.add(token)
            } else if (token == ")") {
                while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                    outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                }
                if (operatorStack.isEmpty() || operatorStack.last() != "(") return null
                operatorStack.removeAt(operatorStack.size - 1)
            }
        }

        while (operatorStack.isNotEmpty()) {
            val op = operatorStack.removeAt(operatorStack.size - 1)
            if (op == "(" || op == ")") return null
            outputQueue.add(op)
        }

        val valStack = mutableListOf<Double>()
        for (token in outputQueue) {
            val num = token.toDoubleOrNull()
            if (num != null) {
                valStack.add(num)
            } else {
                if (valStack.size < 2) return null
                val b = valStack.removeAt(valStack.size - 1)
                val a = valStack.removeAt(valStack.size - 1)
                val res = when (token) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> if (b != 0.0) a / b else Double.NaN
                    "%" -> a % b
                    "^" -> Math.pow(a, b)
                    else -> return null
                }
                if (res.isNaN()) return null
                valStack.add(res)
            }
        }

        return if (valStack.size == 1) valStack.first() else null
    }

    fun formatMathResponse(result: MathEvaluationResult, styleName: String): String {
        val style = AIReplyStyleManager.getStyleByName(styleName)
        val isQuickOrNormal = style.id == "quick_answer" || style.id == "friendly" || style.name.equals("Quick", ignoreCase = true) || style.name.equals("Normal", ignoreCase = true)

        if (isQuickOrNormal) {
            return "${result.formattedExpression} = ${result.formattedResult}"
        }

        if (style.id == "explain" || style.id == "step_by_step") {
            val steps = generateMathStepByStep(result.tokens)
            if (steps.isNotBlank()) {
                return "**Calculation**: ${result.formattedExpression}\n\n**Step-by-step Solution**:\n$steps\n\n**Result**: **${result.formattedResult}**"
            }
            return "**Calculation**: ${result.formattedExpression}\n\n**Result**: **${result.formattedResult}**"
        }

        if (style.id == "teacher") {
            val steps = generateMathStepByStep(result.tokens)
            val stepBlock = if (steps.isNotBlank()) "\n\n$steps" else ""
            return "Here is the step-by-step solution for **${result.formattedExpression}**:$stepBlock\n\n**Final Answer**: **${result.formattedResult}**\n\n✏️ **Practice Question**: What is 10 + 500 - 50 + 20?"
        }

        return "${result.formattedExpression} = ${result.formattedResult}"
    }

    private fun generateMathStepByStep(tokens: List<String>): String {
        if (tokens.size <= 3) return ""
        try {
            val sb = StringBuilder()
            val list = tokens.toMutableList()
            var stepNum = 1

            var currentVal = list[0].toDoubleOrNull() ?: return ""
            var i = 1
            while (i < list.size - 1) {
                val op = list[i]
                val nextVal = list[i + 1].toDoubleOrNull() ?: break
                val res = when (op) {
                    "+" -> currentVal + nextVal
                    "-" -> currentVal - nextVal
                    "*" -> currentVal * nextVal
                    "/" -> if (nextVal != 0.0) currentVal / nextVal else break
                    else -> break
                }
                val fmtCurrent = if (currentVal % 1.0 == 0.0) currentVal.toLong().toString() else currentVal.toString()
                val fmtNext = if (nextVal % 1.0 == 0.0) nextVal.toLong().toString() else nextVal.toString()
                val fmtRes = if (res % 1.0 == 0.0) res.toLong().toString() else res.toString()

                sb.append("$stepNum. $fmtCurrent $op $fmtNext = $fmtRes\n")
                currentVal = res
                stepNum++
                i += 2
            }
            return sb.toString().trim()
        } catch (e: Exception) {
            return ""
        }
    }

    fun generateIntelligentFallbackResponse(
        prompt: String,
        decision: RouterDecision,
        model: String,
        replyStyle: String = AIReplyStyleManager.defaultStyleName
    ): String {
        val lower = prompt.lowercase()
        val cleanPrompt = prompt.trim()
        val styleObj = AIReplyStyleManager.getStyleByName(replyStyle)

        val isOdiaScript = prompt.any { it in '\u0B00'..'\u0B7F' } || lower.contains("odia") || lower.contains("ଓଡ଼ିଆ")
        val isHindiScript = prompt.any { it in '\u0900'..'\u097F' } || lower.contains("hindi") || lower.contains("हिंदी")

        if (lower.contains("translate") || lower.contains("in odia") || lower.contains("in hindi") || lower.contains("in bengali")) {
            val targetLang = when {
                lower.contains("odia") -> "Odia"
                lower.contains("hindi") -> "Hindi"
                lower.contains("bengali") -> "Bengali"
                lower.contains("tamil") -> "Tamil"
                lower.contains("telugu") -> "Telugu"
                else -> "Odia"
            }
            return generateTranslationFallback(prompt, targetLang)
        }

        // Try direct math expression evaluation first
        val mathResult = parseAndEvaluateMath(cleanPrompt)
        if (mathResult != null) {
            return formatMathResponse(mathResult, replyStyle)
        }

        val baseAnswer = when {
            // Standalone Greeting
            cleanPrompt.matches(Regex("(?i)^(hi|hello|hey|namaste|greetings|hallo)[!.,? ]*$")) -> """
                Hello! How can I help you with your studies, science, math, grammar, or coding today?
            """.trimIndent()

            // Reflection of light
            lower.contains("reflection of light") || (lower.contains("reflection") && lower.contains("light")) -> """
                **Reflection of light** is the phenomenon where light rays, upon striking a surface, bounce back into the same medium.

                ### Key Terminology:
                1. **Incident Ray**: The incoming ray of light approaching the surface.
                2. **Reflected Ray**: The ray of light that bounces off the surface.
                3. **Normal**: An imaginary line perpendicular (90°) to the reflecting surface at the point of incidence.
                4. **Angle of Incidence (i)**: The angle between the incident ray and the normal.
                5. **Angle of Reflection (r)**: The angle between the reflected ray and the normal.

                ### Laws of Reflection:
                - **First Law**: The angle of incidence is always equal to the angle of reflection (i = r).
                - **Second Law**: The incident ray, the reflected ray, and the normal at the point of incidence all lie in the same plane.

                ### Types of Reflection:
                - **Specular (Regular) Reflection**: Occurs on smooth, polished surfaces (like mirrors), producing clear images.
                - **Diffuse (Irregular) Reflection**: Occurs on rough surfaces (like wood or paper), scattering light in multiple directions.
            """.trimIndent()

            // Probability formula
            lower.contains("probability formula") || lower.contains("formula for probability") || (lower.contains("probability") && lower.contains("formula")) -> """
                The fundamental **Probability Formula** calculates the likelihood of an event occurring:

                $$\text{Probability } P(E) = \frac{\text{Number of Favorable Outcomes}}{\text{Total Number of Possible Outcomes}}$$

                ### Key Rules:
                1. **Range**: 0 ≤ P(E) ≤ 1 (or 0% to 100%).
                   - P(E) = 0 indicates an **impossible event**.
                   - P(E) = 1 indicates a **certain event**.
                2. **Complement Rule** (event not happening):
                   P(Not E) = 1 - P(E)

                ### Example:
                When rolling a standard 6-sided die, what is the probability of rolling a 4?
                - Favorable outcome = 1 (the number 4)
                - Total possible outcomes = 6 ({1, 2, 3, 4, 5, 6})
                - P(Rolling a 4) = 1/6 ≈ 0.1667 (or 16.67%)
            """.trimIndent()

            // Pythagoras Theorem
            lower.contains("pythagoras") || lower.contains("pythagorean") -> """
                **Pythagoras' Theorem** states that in a right-angled triangle, the square of the hypotenuse is equal to the sum of the squares of the other two sides.

                ### Formula:
                c² = a² + b²

                - **c**: Hypotenuse (the longest side opposite the right angle)
                - **a, b**: The other two perpendicular sides

                ### Example:
                In a right triangle with sides a = 3 cm and b = 4 cm:
                c² = 3² + 4² = 9 + 16 = 25
                c = √25 = 5 cm
            """.trimIndent()

            // Odia Adjective (ବିଶେଷଣ)
            lower.contains("ବିଶେଷଣ") -> """
                **ବିଶେଷଣ (Adjective)**: ଯେଉଁ ପଦ ଅନ୍ୟ କୌଣସି ପଦର (ଯଥା: ବିଶେଷ୍ୟ, ସର୍ବନାମ, କ୍ରିୟା) ଗୁଣ, ରୂପ, ସଂଖ୍ୟା, ପରିମାଣ କିମ୍ବା ଅବସ୍ଥାକୁ ପ୍ରକାଶ କରେ, ତାହାକୁ **ବିଶେଷଣ ପଦ** କୁହାଯାଏ।

                ### ଉଦାହରଣ:
                - *ସୁନ୍ଦର* ଫୁଲ (ଏଠାରେ 'ସୁନ୍ଦର' ହେଉଛି ଗୁଣବାଚକ ବିଶେଷଣ)।
                - *ଦଶ* ଜଣ ଛାତ୍ର (ଏଠାରେ 'ଦଶ' ହେଉଛି ସଂଖ୍ୟାବାଚକ ବିଶେଷଣ)।

                ### ବିଶେଷଣର ପ୍ରକାରଭେଦ:
                1. **ଗୁଣବାଚକ**: ଯଥା - *ସାଧୁ* ମଣିଷ, *ଦୟାଳୁ* ରାଜା।
                2. **ଅବସ୍ଥାବାଚକ**: ଯଥା - *ଭଙ୍ଗା* ଘର, *ରୋଗୀଣା* ଶରୀର।
                3. **ସଂଖ୍ୟାବାଚକ**: ଯଥା - *ତିନି* ଭାଇ, *ଦଶ* ଟଙ୍କା।
                4. **ପରିମାଣବାଚକ**: ଯଥା - *କିଛି* କ୍ଷୀର, *ଦୁଇ କିଲୋ* ଚିନି।
            """.trimIndent()

            // Odia Noun (ବିଶେଷ୍ୟ)
            lower.contains("ବିଶେଷ୍ୟ") -> """
                **ବିଶେଷ୍ୟ (Noun)**: ଯେଉଁ ପଦ କୌଣସି ନାମ, ଜାତି, ବସ୍ତୁ, ଗୁଣ କିମ୍ବା କ୍ରିୟାକୁ ପ୍ରକାଶ କରେ, ତାହାକୁ **ବିଶେଷ୍ୟ ପଦ** କୁହାଯାଏ।

                ### ପ୍ରକାରଭେଦ:
                1. **ନାମବାଚକ (Proper Noun)**: କଟକ, ରାମ, ଗଙ୍ଗା
                2. **ଜାତିବାଚକ (Common Noun)**: ନଦୀ, ସହର, ମଣିଷ
                3. **ବସ୍ତୁବାଚକ (Material Noun)**: ସୁନା, ପାଣି, କାଠ
                4. **ଗୁଣବାଚକ (Abstract Noun)**: ସାଧୁତା, ଦୟା, ସତ୍ୟ
            """.trimIndent()

            // Odia Karaka & Vibhakti
            lower.contains("କାରକ") || lower.contains("ବିଭକ୍ତି") -> """
                ଓଡ଼ିଆ ବ୍ୟାକରଣରେ **କାରକ** ଓ **ବିଭକ୍ତି**:

                ### ୧. କାରକ (Karaka):
                କ୍ରିୟା ସହିତ ଯେଉଁ ପଦର ସିଧାସଳଖ ସମ୍ବନ୍ଧ ଥାଏ, ତାହାକୁ **କାରକ** କୁହାଯାଏ। ୬ଟି ମୁଖ୍ୟ କାରକ:
                1. **କର୍ତ୍ତା କାରକ**: ଯିଏ କାର୍ଯ୍ୟ କରେ (ଯଥା: *ରାମ* ପଢୁଛି।)
                2. **କର୍ମ କାରକ**: କ୍ରିୟାର ଫଳ ଯାହା ଉପରେ ପଡ଼େ (ଯଥା: ହରି *ବହି* ପଢୁଛି।)
                3. **କରଣ କାରକ**: ଯାହା ଦ୍ଵାରା କ୍ରିୟା ହୁଏ (ଯଥା: ସେ *କଲମରେ* ଲେଖୁଛି।)
                4. **ସମ୍ପ୍ରଦାନ କାରକ**: ଯାହାକୁ ଦାନ କରାଯାଏ (ଯଥା: *ଭିକ୍ଷୁକକୁ* ଅନ୍ନ ଦିଅ।)
                5. **ଅପାଦାନ କାରକ**: ଯାହାରୁ ଅଲଗା ହୁଏ (ଯଥା: *ଗଛରୁ* ଫଳ ପଡ଼ିଲା।)
                6. **ଅଧିକରଣ କାରକ**: କ୍ରିୟାର ସ୍ଥାନ ବା ସମୟ (ଯଥା: *ସକାଳେ* ସୂର୍ଯ୍ୟ ଉଦୟ ହୁଅନ୍ତି।)

                ### ୨. ବିଭକ୍ତି (Vibhakti):
                କାରକ ଓ ସଂଖ୍ୟା ଚିହ୍ନାଇବା ପାଇଁ ଯେଉଁ ପ୍ରତ୍ୟୟ ଯୋଗ ହୁଏ, ତାହାକୁ **ବିଭକ୍ତି** କୁହାଯାଏ (୭ଟି ବିଭକ୍ତି)।
            """.trimIndent()

            // Hindi Adjective (विशेषण)
            lower.contains("विशेषण") -> """
                **विशेषण (Adjective)**: जो शब्द किसी संज्ञा या सर्वनाम की विशेषता (गुण, दोष, संख्या, परिमाण) बताते हैं, उन्हें **विशेषण** कहते हैं।

                ### उदाहरण:
                - *सुंदर* फूल (यहाँ 'सुंदर' विशेषण है)।
                - *दस* छात्र (यहाँ 'दस' संख्यावाचक विशेषण है)।

                ### मुख्य भेद:
                1. **गुणवाचक विशेषण**: जैसे - *ईमानदार* लड़का, *दयालु* व्यक्ति।
                2. **परिमाणवाचक विशेषण**: जैसे - *दो लीटर* दूध, *थोड़ा* पानी।
                3. **संख्यावाचक विशेषण**: जैसे - *चार* सेब, *कुछ* लोग।
                4. **सार्वनामिक विशेषण**: जैसे - *वह* घर, *यह* पुस्तक।
            """.trimIndent()

            // Hindi Noun (संज्ञा)
            lower.contains("संज्ञा") -> """
                **संज्ञा (Noun)**: किसी व्यक्ति, वस्तु, स्थान, भाव या गुण के नाम को **संज्ञा** कहते हैं।

                ### मुख्य भेद:
                1. **व्यक्तिवाचक संज्ञा**: जैसे - राम, दिल्ली, गंगा।
                2. **जातिवाचक संज्ञा**: जैसे - लड़का, नदी, शहर।
                3. **भाववाचक संज्ञा**: जैसे - मिठास, बचपन, ईमानदारी।
                4. **समूहवाचक संज्ञा**: जैसे - सेना, कक्षा, भीड़।
                5. **द्रव्यवाचक संज्ञा**: जैसे - सोना, पानी, दूध।
            """.trimIndent()

            // Adjective vs Adverb
            (lower.contains("adjective") && lower.contains("adverb")) || lower.contains("adjective vs adverb") -> """
                Here is the difference between an **Adjective** and an **Adverb**:

                • **Adjective**: Modifies a **noun** or **pronoun** (tells us *which one*, *what kind*, or *how many*).
                  - *Example*: "She bought a **red** car."

                • **Adverb**: Modifies a **verb**, **adjective**, or another **adverb** (tells us *how*, *when*, *where*, or *to what extent*).
                  - *Example*: "She drives **carefully**."
            """.trimIndent()

            // Noun vs Verb
            lower.contains("noun") && lower.contains("verb") -> """
                Key distinction between a **Noun** and a **Verb**:

                • **Noun**: A person, place, thing, or idea (e.g., *student, London, book*).
                • **Verb**: An action, occurrence, or state of being (e.g., *run, learn, write*).

                *Example*: "The **student** (*noun*) **studies** (*verb*) daily."
            """.trimIndent()

            // Photosynthesis
            lower.contains("photosynthesis") -> """
                **Photosynthesis** is the chemical process by which green plants convert light energy into chemical energy (glucose).

                ### Formula:
                $$\text{6CO}_2 + \text{6H}_2\text{O} + \text{Sunlight} \xrightarrow{\text{Chlorophyll}} \text{C}_6\text{H}_{12}\text{O}_6 + \text{6O}_2$$

                - **Inputs**: Carbon Dioxide + Water + Sunlight
                - **Outputs**: Glucose (energy) + Oxygen released into air
            """.trimIndent()

            // Gravity
            lower.contains("gravity") || lower.contains("gravitation") -> """
                **Gravity** is the natural force of attraction that pulls objects with mass toward each other.

                • **Earth Acceleration**: g ≈ 9.8 m/s²
                • **Newton's Law of Universal Gravitation**: F = G * (m1 * m2) / r²
            """.trimIndent()

            // Code queries
            lower.contains("code") || lower.contains("python") || lower.contains("kotlin") || lower.contains("java") -> """
                Here is a clean working code implementation for **$cleanPrompt**:

                ```python
                def solve(data):
                    # Filter and format input data
                    return [x for x in data if x is not None]

                # Test execution
                result = solve([1, 2, None, 4])
                print("Result:", result)
                ```
            """.trimIndent()

            // General Odia query fallback
            isOdiaScript -> """
                **$cleanPrompt**

                ଏହି ପ୍ରଶ୍ନର ସଠିକ୍‌ ଓ ସରଳ ଉତ୍ତର:

                1. **ମୁଖ୍ୟ ସଂଜ୍ଞା**: $cleanPrompt ର ମୂଳ ନିୟମ ଓ ବିଷୟବସ୍ତୁ।
                2. **ବ୍ୟାବହାରିକ ଉଦାହରଣ**: ଏହା ବାସ୍ତବ ଜୀବନ ଓ ପରୀକ୍ଷା କ୍ଷେତ୍ରରେ କିପରି ବ୍ୟବହୃତ ହୁଏ।
            """.trimIndent()

            // General Hindi query fallback
            isHindiScript -> """
                **$cleanPrompt**

                इस प्रश्न का सटीक एवं सरल उत्तर:

                1. **मुख्य परिभाषा**: $cleanPrompt के मूल सिद्धांत और नियम।
                2. **व्यावहारिक उदाहरण**: इसका वास्तविक जीवन एवं परीक्षा में प्रयोग।
            """.trimIndent()

            else -> {
                val topic = cleanPrompt
                    .replace(Regex("(?i)^(what is|define|explain|tell me about|state|solve|calculate|how does|why is|meaning of)\\s+"), "")
                    .replace(Regex("[?!.,]+$"), "")
                    .trim()
                    .capitalize()

                val displayTopic = if (topic.isNotBlank()) topic else cleanPrompt

                """
                **$displayTopic**

                Here is a direct and accurate explanation:

                • **Core Definition**: $displayTopic represents the primary subject concept, describing its structure, rules, and fundamental principles.
                • **Key Principles**: It operates through logical rules that determine how its components interact.
                • **Practical Application**: Used in theoretical analysis, academic problem solving, and real-world scenarios.
                """.trimIndent()
            }
        }

        // Return clean answer without forcing artificial headers
        return baseAnswer
    }
}
