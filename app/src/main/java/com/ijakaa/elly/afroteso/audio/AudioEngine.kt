package com.ijakaa.elly.afroteso.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import com.ijakaa.elly.afroteso.R

object AudioEngine {

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<String, Int>()
    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        // 🥁 Load your drum sounds here
        soundMap["djembe"] = soundPool!!.load(context, R.raw.djembe, 1)
        soundMap["shaker"] = soundPool!!.load(context, R.raw.shaker, 1)
        soundMap["bass"] = soundPool!!.load(context, R.raw.bass_drum, 1)

        isInitialized = true
        Log.d("AudioEngine", "Initialized with ${soundMap.size} sounds")
    }

    fun playDrum(name: String) {
        if (!isInitialized) {
            Log.e("AudioEngine", "AudioEngine not initialized!")
            return
        }
        val soundId = soundMap[name]
        if (soundId != null) {
            soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
            Log.d("AudioEngine", "Playing $name")
        } else {
            Log.e("AudioEngine", "Sound not found: $name")
        }
    }

    fun stopAll() {
        soundPool?.autoPause()
        Log.d("AudioEngine", "Stopped all sounds")
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        isInitialized = false
        Log.d("AudioEngine", "Released resources")
    }
}
