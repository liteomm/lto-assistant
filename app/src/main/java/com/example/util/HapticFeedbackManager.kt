package com.example.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

object HapticFeedbackManager {

    fun performHaptic(context: Context, level: String, composeHaptic: HapticFeedback? = null) {
        if (level.equals("OFF", ignoreCase = true)) return

        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                val durationMs = when (level.uppercase()) {
                    "LIGHT" -> 10L
                    "STRONG" -> 45L
                    else -> 22L // Medium
                }
                val amplitude = when (level.uppercase()) {
                    "LIGHT" -> 50
                    "STRONG" -> 255
                    else -> 128 // Medium
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, amplitude))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            } else {
                composeHaptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
        } catch (e: Exception) {
            composeHaptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }
}
