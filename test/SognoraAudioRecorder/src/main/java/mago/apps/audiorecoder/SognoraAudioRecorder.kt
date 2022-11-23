package mago.apps.audiorecoder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class SognoraAudioRecorder @Inject constructor(private val context: Context) {

    val TAG = "SognoraAudioRecorder"

    /** 오디오 레코드 */
    var sognoraAudioRecorder: AudioRecord? = null // 오디오 녹음을 위함.

    /** 오디오 녹음기 초기화 */
    fun startAudioRecorder(
        source: Int = MediaRecorder.AudioSource.MIC,
        sampleRate: Int = 16000,
        channel: Int = AudioFormat.CHANNEL_IN_MONO,
        encoding: Int = AudioFormat.ENCODING_PCM_16BIT,
    ) {
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

    /** 오디오 버퍼 전송 중단하기 */
    fun stopAudioRecorder() {
        sognoraAudioRecorder?.stop()
        sognoraAudioRecorder?.release()
        sognoraAudioRecorder = null
    }

}