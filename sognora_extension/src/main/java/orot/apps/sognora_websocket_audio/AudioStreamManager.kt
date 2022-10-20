package orot.apps.sognora_websocket_audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat

private const val RECORDER_SAMPLERATE = 44100
private val RECORDER_CHANNELS: Int = AudioFormat.CHANNEL_IN_STEREO
private val RECORDER_AUDIO_ENCODING: Int = AudioFormat.ENCODING_PCM_16BIT

class AudioStreamManager(private val ctx: Context) {

    private var audioRecord: AudioRecord? = null

    private val BUFFER_SIZE_RECORDING = AudioRecord.getMinBufferSize(
        RECORDER_SAMPLERATE,
        RECORDER_CHANNELS,
        RECORDER_AUDIO_ENCODING
    ) * 4

    fun initAudioRecorder() {
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }else{
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BUFFER_SIZE_RECORDING
            )
            audioRecord?.startRecording()
        }
    }
}