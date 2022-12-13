package mago.apps.sognoraaudio.google_tts

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.*

class OrotTTS : IOrotTTS {

    private val params = Bundle()
    private var tts: TextToSpeech? = null

    override fun createTts(context: Context, listener: UtteranceProgressListener) {
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

    override fun start(msg: String?) {
        tts?.let {
            if (it.isSpeaking) {
                it.stop()
            }
            msg.let { content ->
                it.speak(content, TextToSpeech.QUEUE_ADD, params, content)
            }
        }
    }

    override fun pause() {
        tts?.stop()
    }

    override fun clear() {
        tts?.stop()
        tts = null
    }
}