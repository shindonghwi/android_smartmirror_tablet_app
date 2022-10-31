package orot.apps.sognora_mediaplayer

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.*

class SognoraTTS {

    private val params = Bundle()
    var tts: TextToSpeech? = null

    fun createTts(context: Context) {
        if (tts != null) return

        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null)
        tts = TextToSpeech(context) { state ->
            if (state == TextToSpeech.SUCCESS) {
                tts?.language = Locale.KOREAN
            }
        }

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(s: String) {}
            override fun onDone(s: String) {}
            override fun onError(s: String) {}
            override fun onRangeStart(utteranceId: String, start: Int, end: Int, frame: Int) {}
        })
    }

    fun startPlay(msg: String) {
        tts?.let {
            if (it.isSpeaking){
                it.stop()
            }
            it.speak(msg, TextToSpeech.QUEUE_ADD, params, msg);
        }
    }

    fun clear(){
        tts?.run {
            stop()
            null
        }
    }
}