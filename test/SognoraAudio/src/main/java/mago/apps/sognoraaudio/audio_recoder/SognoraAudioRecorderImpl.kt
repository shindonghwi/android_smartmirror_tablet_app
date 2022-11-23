package mago.apps.sognoraaudio.audio_recoder


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.util.Log
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class SognoraAudioRecorderImpl @Inject constructor(private val context: Context):
    SognoraAudioRecorder() {

    val TAG = "SognoraAudioRecorder"

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
                sognoraAudioRecorder = AudioRecord(
                    source,
                    sampleRate,
                    channel,
                    encoding,
                    AudioRecord.getMinBufferSize(sampleRate, channel, encoding) * 6
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
}