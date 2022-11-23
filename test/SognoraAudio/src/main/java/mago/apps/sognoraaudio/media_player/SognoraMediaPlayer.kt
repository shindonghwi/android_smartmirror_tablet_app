package mago.apps.sognoraaudio.media_player

import android.content.Context

abstract class SognoraMediaPlayer {
    abstract fun prepareUrl(url: String)
    abstract fun prepareRaw(context: Context, rawFile: Int)
    abstract fun getDuration(): Int
    abstract fun play()
    abstract fun close()
    abstract fun pause()
    abstract fun stop()
    abstract fun resume()
}