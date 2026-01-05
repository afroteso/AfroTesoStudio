package com.ijakaa.elly.afroteso.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.concurrent.thread
import kotlin.math.sin

class DrumEngine {

    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private val sampleRate = 44100

    fun startBeat(bpm: Int = 96) {
        if (isPlaying) return
        isPlaying = true

        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
            AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build(),
            bufferSize,
            AudioTrack.MODE_STREAM,
            AudioTrack.AUDIO_SESSION_ID_GENERATE
        )

        audioTrack?.play()

        val secondsPerBeat = 60.0 / bpm
        val samplesPerBeat = (sampleRate * secondsPerBeat).toInt()

        thread {
            val buffer = ShortArray(bufferSize)

            while (isPlaying) {
                for (i in buffer.indices) {
                    val phase = i.toDouble() / sampleRate

                    // Kick drum (low frequency pulse)
                    val kick = sin(2 * Math.PI * 90 * phase) *
                            Math.exp(-6 * phase)

                    // Click (high frequency)
                    val click = sin(2 * Math.PI * 2000 * phase) *
                            Math.exp(-25 * phase)

                    val value = (kick * 0.9 + click * 0.3) * Short.MAX_VALUE
                    buffer[i] = value.toInt().toShort()
                }

                audioTrack?.write(buffer, 0, buffer.size)
                Thread.sleep((secondsPerBeat * 1000).toLong())
            }
        }
    }

    fun stopBeat() {
        isPlaying = false
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
}