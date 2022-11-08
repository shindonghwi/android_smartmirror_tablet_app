package orot.apps.sognora_websocket_audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.NoiseSuppressor
import android.util.Log
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.toByteString
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnIO
import orot.apps.sognora_websocket_audio.model.WebSocketState


/** 웹소켓 연결 및 음성 버퍼를 서버에 전달하는 매니저 */
class AudioStreamManager {

    val TAG = "AudioStreamManager"
    var audioSendAvailable = false

    val webSocketURL: String = "ws://172.30.1.15:8080/ws/chat"
//    val webSocketURL: String = "ws://mago-demo-server.orotcode.com:8080/ws/chat"

    /** 오디오 */
    var audioRecord: AudioRecord? = null // 오디오 녹음을 위함.
    private val RECORDER_SOURCE = MediaRecorder.AudioSource.MIC // 마이크 ( 보이스 인식만 가능한 마이크도 있음 )
    private val RECORDER_SAMPLERATE = 16000 // 샘플링 속도
    private val RECORDER_CHANNELS: Int = AudioFormat.CHANNEL_IN_MONO // 모노 ( 스테레오 선택가능 )
    private val RECORDER_AUDIO_ENCODING: Int = AudioFormat.ENCODING_PCM_16BIT // 오디오 인코딩 타입
    private val BUFFER_SIZE_RECORDING = (AudioRecord.getMinBufferSize( // 오디오 버퍼 설정 , 대략 0.48 ~ 0.5초임
        RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING
    ) * 3)


    /** 웹 소켓 */
    var webSocket: WebSocket? = null
    private var request: Request? = Request.Builder().url(webSocketURL).build() // 웹 소켓 연결 빌더 생성
    private var client: OkHttpClient = OkHttpClient()

    /** 웹 소켓 연결하기 */
    fun initWebSocket(manageable: AudioStreamManageable) {
        request?.run {
            webSocket = client.newWebSocket(this, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    Log.d(TAG, "onOpen: $response")
                    coroutineScopeOnDefault { manageable.stateWebSocket(state = WebSocketState.Connected) }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    Log.d(TAG, "onMessage: $text")
                    coroutineScopeOnDefault { manageable.receivedMessageString(text) }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    Log.d(TAG, "onFailure: $t || $response || ${t.message} || ${response?.message}")
                    coroutineScopeOnDefault { manageable.stateWebSocket(state = WebSocketState.Failed) }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    Log.d(TAG, "onClosed: $code || $reason")
                    coroutineScopeOnDefault { manageable.stateWebSocket(state = WebSocketState.DisConnected) }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    Log.d(TAG, "onClosing: $code || $reason")
                    coroutineScopeOnDefault { manageable.stateWebSocket(state = WebSocketState.Closing) }
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                    Log.d(TAG, "onMessage: $bytes")
                    coroutineScopeOnDefault { manageable.receivedMessageByteString(bytes) }
                }
            })
        }
    }

    /** 오디오 녹음기 초기화 */
    @SuppressLint("MissingPermission")
    fun initAudioRecorder() {
        if (audioRecord != null) {
            stopAudioRecord()
            Log.d(TAG, "initAudioRecorder:")
        }
        audioRecord = AudioRecord(
            RECORDER_SOURCE, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BUFFER_SIZE_RECORDING
        )

        audioRecord?.run {
            NoiseSuppressor.create(audioSessionId).enabled = true
            AcousticEchoCanceler.create(audioSessionId).enabled = true
            startRecording()
        }
    }

    /** 녹음한 오디오 버퍼 전송하기 */
    fun sendAudioRecord() {
        val buf = ByteArray(BUFFER_SIZE_RECORDING)
        coroutineScopeOnIO {
            try {
                do {
                    if (audioSendAvailable) {
                        val byteRead = audioRecord?.read(buf, 0, buf.size, AudioRecord.READ_BLOCKING) ?: break
                        if (byteRead < -1) break
                        webSocket?.send(buf.toByteString(0, byteRead))
                    }
                } while (true)
            } catch (e: Exception) {
                Log.d(TAG, "sendAudioRecord ERROR: ${e.message}")
                stopAudioRecord()
            }
        }
    }

    /** 오디오 버퍼 전송 중단하기 */
    fun stopAudioRecord() {
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
        webSocket?.cancel()
        webSocket = null
    }

}
