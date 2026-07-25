package com.dogcube.game.audio


import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


enum class SoundType(val rawResName: String) {
    MOVE("sfx_move"), ROTATE("sfx_rotate"), SOFT_DROP("sfx_softdrop"),
    HARD_DROP("sfx_harddrop"), LINE_CLEAR("sfx_lineclear"), TETRIS("sfx_tetris"), GAME_OVER("sfx_gameover")
}


@Singleton
class SoundManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val soundPool: SoundPool
    private val soundIds = mutableMapOf<SoundType, Int>()
    private var loaded = false


    init {
        val attrs = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        soundPool = SoundPool.Builder().setMaxStreams(4).setAudioAttributes(attrs).build()
    }


    fun load() {
        if (loaded) return
        SoundType.entries.forEach { type ->
            val resId = context.resources.getIdentifier(type.rawResName, "raw", context.packageName)
            if (resId != 0) soundIds[type] = soundPool.load(context, resId, 1)
        }
        loaded = true
    }


    fun play(type: SoundType) { soundIds[type]?.let { soundPool.play(it, 0.8f, 0.8f, 1, 0, 1.0f) } }
    fun release() { soundPool.release(); loaded = false }
}
