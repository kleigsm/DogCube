package com.dogcube.game.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.media.ToneGenerator
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

    fun load() {
        if (loaded) return
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
        loaded = true
    }

    fun play(type: SoundType) {
        if (useOgg) {
            val id = soundIds[type] ?: return
            soundPool?.play(id, 0.8f, 0.8f, 1, 0, 1.0f)
        } else {
            playTone(type)
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        loaded = false
    }

    // --- ToneGenerator fallback ---

    private var toneGen: ToneGenerator? = null

    private fun getToneGen(): ToneGenerator {
        if (toneGen == null) {
            toneGen = ToneGenerator(AudioAttributes.USAGE_GAME, 80)
        }
        return toneGen!!
    }

    private fun playTone(type: SoundType) {
        val tg = getToneGen()
        when (type) {
            SoundType.MOVE -> tg.startTone(ToneGenerator.TONE_DTMF_1, 40)
            SoundType.ROTATE -> tg.startTone(ToneGenerator.TONE_DTMF_3, 60)
            SoundType.SOFT_DROP -> tg.startTone(ToneGenerator.TONE_DTMF_5, 50)
            SoundType.HARD_DROP -> tg.startTone(ToneGenerator.TONE_DTMF_7, 80)
            SoundType.LINE_CLEAR -> tg.startTone(ToneGenerator.TONE_DTMF_5, 150)
            SoundType.TETRIS -> tg.startTone(ToneGenerator.TONE_DTMF_P, 300)
            SoundType.GAME_OVER -> tg.startTone(ToneGenerator.TONE_DTMF_1, 500)
        }
    }
}
