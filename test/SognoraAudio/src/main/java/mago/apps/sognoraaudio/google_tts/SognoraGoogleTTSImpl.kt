package mago.apps.sognoraaudio.google_tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.*
import javax.inject.Inject


class SognoraGoogleTTSImpl @Inject constructor(private val context: Context) : SognoraGoogleTTS() {

    private val params = Bundle()
    var tts: TextToSpeech? = null

    override fun createTts(listener: UtteranceProgressListener) {
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null)
        tts = TextToSpeech(context) { state ->
            if (state == TextToSpeech.SUCCESS) {
                tts?.apply {
                    language = Locale.KOREAN
                    setOnUtteranceProgressListener(listener)
                }
            }
        }
    }

    override fun startPlay(msg: String) {
        tts?.let {
            if (it.isSpeaking) {
                it.stop()
            }
            it.speak(msg, TextToSpeech.QUEUE_ADD, params, msg)
            return@let
        }
    }

    override fun stop() {
        tts?.run {
            stop()
            null
        }
    }
}