package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.util.Log

object SoundManager {

    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 50)
        } catch (e: Exception) {
            Log.e("SoundManager", "Failed to initialize ToneGenerator", e)
        }
    }

    fun playTypingSound(context: Context, enabled: Boolean) {
        if (!enabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 15)
        } catch (e: Exception) {
            // Ignore sound errors on unsupported devices
        }
    }

    fun playSendSound(context: Context, enabled: Boolean) {
        if (!enabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 30)
        } catch (e: Exception) {
            // Ignore sound errors
        }
    }

    fun playNotificationSound(context: Context, enabled: Boolean) {
        if (!enabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 40)
        } catch (e: Exception) {
            // Ignore sound errors
        }
    }
}
