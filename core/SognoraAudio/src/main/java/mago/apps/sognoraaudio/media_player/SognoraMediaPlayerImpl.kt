package mago.apps.sognoraaudio.media_player


import android.content.Context
import android.media.MediaPlayer

class SognoraMediaPlayerImpl : SognoraMediaPlayer() {
    var player: MediaPlayer? = null
    var position = 0
    var audioDuration = 0
    override fun prepareUrl(url: String) {
        try {
            close()
            player = MediaPlayer()
            player?.run {
                setDataSource(url)
                prepare()
                audioDuration = duration
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun play() {
        player?.run {
            try {
                start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getDuration(): Int {
        return audioDuration
    }

    override fun prepareRaw(context: Context, rawFile: Int) {
        try {
            close()
            player = MediaPlayer.create(context, rawFile)
            player?.run {
                prepare()
                audioDuration = this.duration
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun close() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    override fun pause() {
        if (player != null) {
            position = player!!.currentPosition
            player!!.pause()
        }
    }

    override fun stop() {
        if (player != null && player!!.isPlaying) {
            player!!.stop()
        }
    }

    override fun resume() {
        if (player != null && !player!!.isPlaying) {
            player!!.seekTo(position)
            player!!.start()
        }
    }
}