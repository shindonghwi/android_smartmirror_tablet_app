package mago.apps.sognoraaudio.google_tts

import android.speech.tts.UtteranceProgressListener

abstract class SognoraGoogleTTS {
    abstract fun createTts(listener: UtteranceProgressListener)
    abstract fun startPlay(msg: String)
    abstract fun stop()
}