package mago.apps.sognoraaudio.audio_recoder

abstract class SognoraAudioRecorder {
    abstract fun startAudioRecorder(source: Int, sampleRate: Int, channel: Int, encoding: Int)
    abstract fun stopAudioRecorder()
}