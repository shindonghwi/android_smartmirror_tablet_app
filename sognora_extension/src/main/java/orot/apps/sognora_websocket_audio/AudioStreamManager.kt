package orot.apps.sognora_websocket_audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import okhttp3.*
import okio.ByteString.Companion.toByteString
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnIO
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val RECORDER_SAMPLERATE = 44100
private val RECORDER_CHANNELS: Int = AudioFormat.CHANNEL_IN_MONO
private val RECORDER_AUDIO_ENCODING: Int = AudioFormat.ENCODING_PCM_16BIT

class AudioStreamManager @Inject constructor() {

    val webSocketURL: String = "http://localhost"

    init {
        initWebSocket() // 웹 소켓 연결
    }

    /** 오디오 */
    private var audioRecord: AudioRecord? = null // 오디오 녹음을 위함.
    private val BUFFER_SIZE_RECORDING = AudioRecord.getMinBufferSize( // 오디오 버퍼 설정
        RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING
    ) * 4

    /** 웹 소켓 */
    var webSocket: WebSocket? = null
    private val okHttpClient = OkHttpClient.Builder() // 웹 소켓 클라이언트 설정
        .connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build()

    private var request: Request? = Request.Builder().url(webSocketURL).build() // 웹 소켓 연결 빌더 생성

    /** 웹 소켓 연결하기 */
    private fun initWebSocket() {
        request?.run {
            webSocket = okHttpClient.newWebSocket(this, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    initAudioRecorder() // 오디오 레코더 생성
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                }
            })
        }
    }

    /** 오디오 녹음 시작 */
    @SuppressLint("MissingPermission") // 권한이 추가되어야 사용가능
    fun initAudioRecorder() {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            RECORDER_SAMPLERATE,
            RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING,
            BUFFER_SIZE_RECORDING
        )
        audioRecord?.startRecording()
    }

    /** 녹음한 오디오 버퍼 전송하기 */
    fun sendAudioRecord() {
        val buf = ByteArray(BUFFER_SIZE_RECORDING)
        coroutineScopeOnIO {
            try {
                do {
                    val byteRead = audioRecord?.read(buf, 0, buf.size) ?: break
                    if (byteRead < -1) break
                    webSocket?.send(buf.toByteString(0, byteRead))
                } while (true)
            } catch (e: Exception) {
                stopAudioRecord()
            }
        }
    }

    /** 오디오 버퍼 전송 중단하기 */
    fun stopAudioRecord() {
        webSocket?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

}