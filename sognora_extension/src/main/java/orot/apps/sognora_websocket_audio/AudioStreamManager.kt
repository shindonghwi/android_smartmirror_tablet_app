package orot.apps.sognora_websocket_audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import okhttp3.*
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnIO
import java.util.concurrent.TimeUnit


class AudioStreamManager(private val audioStreamImpl: AudioStreamManagerImpl) {

    val webSocketURL: String = "ws://www.naver.com"

    /** 오디오 */
    private var audioRecord: AudioRecord? = null // 오디오 녹음을 위함.
    private val RECORDER_SOURCE = MediaRecorder.AudioSource.VOICE_RECOGNITION
    private val RECORDER_SAMPLERATE = 44100
    private val RECORDER_CHANNELS: Int = AudioFormat.CHANNEL_IN_MONO
    private val RECORDER_AUDIO_ENCODING: Int = AudioFormat.ENCODING_PCM_16BIT
    private val BUFFER_SIZE_RECORDING = AudioRecord.getMinBufferSize( // 오디오 버퍼 설정
        RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING
    ) * 12

    init {
        initWebSocket()
    }

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
                    coroutineScopeOnDefault {
                        audioStreamImpl.connectedWebSocket()
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    coroutineScopeOnIO {
                        audioStreamImpl.receivedMsg(AudioStreamData.ReceivedData(text))
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    coroutineScopeOnDefault {
                        audioStreamImpl.disConnectedWebSocket()
                    }
                }
            })
        }
    }

    /** 오디오 녹음 시작 */
    @SuppressLint("MissingPermission")
    fun initAudioRecorder() {
        audioRecord = AudioRecord(
            RECORDER_SOURCE,
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
                    var start = System.currentTimeMillis()
                    val byteRead = audioRecord?.read(buf, 0, buf.size) ?: break
                    if (byteRead < -1) break
                    var end = System.currentTimeMillis()
                    Log.d("ASdasddsaads", "sendAudioRecord: time: ${end - start}")
//                    webSocket?.send(buf.toByteString(0, byteRead))
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