package mago.apps.sognoraaudio.audio_recoder


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.util.Log
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class SognoraAudioRecorderImpl @Inject constructor(private val context: Context) :
    SognoraAudioRecorder {

    val TAG = "SognoraAudioRecorder"
    var minBufferSize: Int = 0

    /** 오디오 레코드 */
    var sognoraAudioRecorder: AudioRecord? = null // 오디오 녹음을 위함.
    override fun startAudioRecorder(source: Int, sampleRate: Int, channel: Int, encoding: Int) {
        if (sognoraAudioRecorder != null) {
            Log.d(TAG, "startAudioRecorder:")
            stopAudioRecorder()
        }
        try {
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Audio Start Failed: Not Allowed Audio Record Permission")
                return
            } else {
                minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channel, encoding) * 2
                sognoraAudioRecorder = AudioRecord(
                    source,
                    sampleRate,
                    channel,
                    encoding,
                    minBufferSize,
                )

                sognoraAudioRecorder?.run {
                    startRecording()
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Audio init Failed: ${e.message}")
        }
    }

    override fun stopAudioRecorder() {
        sognoraAudioRecorder?.stop()
        sognoraAudioRecorder?.release()
        sognoraAudioRecorder = null
    }

    override fun frameBuffer(byteRead: Int): Pair<ByteArray, Int> {
        val buf = ByteArray(minBufferSize)
        return Pair(
            buf,
            sognoraAudioRecorder?.read(buf, 0, buf.size, AudioRecord.READ_BLOCKING) ?: -1
        )
    }

    override fun getMinBuffer() = minBufferSize
}