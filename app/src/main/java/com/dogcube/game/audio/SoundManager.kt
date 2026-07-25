package com.dogcube.game.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class SoundType(val rawResName: String) {
    MOVE("sfx_move"),
    ROTATE("sfx_rotate"),
    SOFT_DROP("sfx_softdrop"),
    HARD_DROP("sfx_harddrop"),
    LINE_CLEAR("sfx_lineclear"),
    TETRIS("sfx_tetris"),
    GAME_OVER("sfx_gameover")
}

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var soundPool: SoundPool? = null
    private val soundIds = mutableMapOf<SoundType, Int>()
    private var loaded = false
    private var useOgg = false
    private var toneGen: ToneGenerator? = null

    fun load() {
        if (loaded) return
        try {
            val attrs = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            soundPool = SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(attrs)
                .build()

            SoundType.entries.forEach { type ->
                val resId = context.resources.getIdentifier(type.rawResName, "raw", context.packageName)
                if (resId != 0) {
                    soundIds[type] = soundPool!!.load(context, resId, 1)
                    useOgg = true
                }
            }
        } catch (e: Exception) {
            Log.w("SoundManager", "SoundPool init failed, using ToneGenerator", e)
        }
        loaded = true
    }

    fun play(type: SoundType) {
        try {
            if (useOgg) {
                val id = soundIds[type] ?: return
                soundPool?.play(id, 0.8f, 0.8f, 1, 0, 1.0f)
            } else {
                playTone(type)
            }
        } catch (e: Exception) {
            Log.w("SoundManager", "play failed", e)
        }
    }

    fun release() {
        try {
            soundPool?.release()
        } catch (_: Exception) {}
        soundPool = null
        try {
            toneGen?.release()
        } catch (_: Exception) {}
        toneGen = null
        loaded = false
    }

    private fun getToneGen(): ToneGenerator {
        if (toneGen == null) {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 60)
        }
        return toneGen!!
    }

    private fun playTone(type: SoundType) {
        val tg = try { getToneGen() } catch (e: Exception) { return }
        try {
            when (type) {
                SoundType.MOVE -> tg.startTone(ToneGenerator.TONE_DTMF_1, 40)
                SoundType.ROTATE -> tg.startTone(ToneGenerator.TONE_DTMF_3, 60)
                SoundType.SOFT_DROP -> tg.startTone(ToneGenerator.TONE_DTMF_5, 50)
                SoundType.HARD_DROP -> tg.startTone(ToneGenerator.TONE_DTMF_7, 80)
                SoundType.LINE_CLEAR -> tg.startTone(ToneGenerator.TONE_DTMF_5, 150)
                SoundType.TETRIS -> tg.startTone(ToneGenerator.TONE_DTMF_P, 300)
                SoundType.GAME_OVER -> tg.startTone(ToneGenerator.TONE_DTMF_1, 500)
            }
        } catch (e: Exception) {
            Log.w("SoundManager", "tone play failed", e)
        }
    }
}
