package mago.apps.sognoraaudio.google_tts

import android.content.Context
import android.speech.tts.UtteranceProgressListener

interface IOrotTTS {
    fun createTts(context: Context, listener: UtteranceProgressListener)
    fun start(msg: String?)
    fun pause()
    fun clear()
}