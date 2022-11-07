package orot.apps.sognora_mediaplayer

import android.content.Context
import android.media.MediaPlayer

class SognoraMediaPlayer {
    var player: MediaPlayer? = null
    var position = 0
    var audioDuration = 0

    suspend fun prepareAudio(url: String) {
        try {
            closePlayer()
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

    suspend fun playAudio() {
        player?.run {
            try {
                start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getDuration() = audioDuration

    suspend fun playRaw(context: Context, rawFile: Int) {
        try {
            closePlayer()
            player = MediaPlayer.create(context, rawFile)
            player?.run {
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun closePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    suspend fun pauseAudio() {
        if (player != null) {
            position = player!!.currentPosition
            player!!.pause()
        }
    }

    suspend fun resumeAudio() {
        if (player != null && !player!!.isPlaying) {
            player!!.seekTo(position)
            player!!.start()
        }
    }

    suspend fun stopAudio() {
        if (player != null && player!!.isPlaying) {
            player!!.stop()
        }
    }
}