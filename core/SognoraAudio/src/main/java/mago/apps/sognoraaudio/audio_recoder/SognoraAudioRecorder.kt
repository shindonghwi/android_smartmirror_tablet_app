package mago.apps.sognoraaudio.audio_recoder

import android.media.AudioFormat
import android.media.MediaRecorder

interface SognoraAudioRecorder {
    fun startAudioRecorder(
        source: Int = MediaRecorder.AudioSource.MIC,
        sampleRate: Int = 16000,
        channel: Int = AudioFormat.CHANNEL_IN_MONO,
        encoding: Int = AudioFormat.ENCODING_PCM_16BIT
    )

    fun stopAudioRecorder()

    fun frameBuffer(byteRead: Int): Pair<ByteArray, Int>

    fun getMinBuffer(): Int
}