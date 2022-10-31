package orot.apps.sognora_websocket_audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.toByteString
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnIO
import orot.apps.sognora_websocket_audio.model.protocol.HeaderInfo
import orot.apps.sognora_websocket_audio.model.protocol.MAGO_PROTOCOL
import orot.apps.sognora_websocket_audio.model.protocol.MessageProtocol


class AudioStreamManager(private val audioStreamImpl: IAudioStreamManager) {

    var audioSendAvailable = false
    val webSocketURL: String = "ws://172.30.1.65:8080/ws/chat"

    /** 오디오 */
    private var audioRecord: AudioRecord? = null // 오디오 녹음을 위함.
    private val RECORDER_SOURCE = MediaRecorder.AudioSource.MIC // 마이크 ( 보이스 인식만 가능한 마이크도 있음 )
    private val RECORDER_SAMPLERATE = 16000 // 샘플링 속도
    private val RECORDER_CHANNELS: Int = AudioFormat.CHANNEL_IN_MONO // 모노 ( 스테레오 선택가능 )
    private val RECORDER_AUDIO_ENCODING: Int = AudioFormat.ENCODING_PCM_16BIT // 오디오 인코딩 타입
    private val BUFFER_SIZE_RECORDING = AudioRecord.getMinBufferSize( // 오디오 버퍼 설정 , 대략 0.48 ~ 0.5초임
        RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING
    ) * 12


    /** 웹 소켓 */
    var webSocket: WebSocket? = null
    private var request: Request? = Request.Builder().url(webSocketURL).build() // 웹 소켓 연결 빌더 생성
    private var client: OkHttpClient = OkHttpClient()

    init {
        initWebSocket()
    }


    /** 웹 소켓 연결하기 */
    private fun initWebSocket() {
        request?.run {
            webSocket = client.newWebSocket(this, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    Log.d("WEBSOCKET", "onOpen: $response")
                    coroutineScopeOnDefault {
                        audioStreamImpl.connectedWebSocket()
                    }
                    sendProtocol(1)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    Log.d("WEBSOCKET", "onMessage: $text")
                    coroutineScopeOnIO {
                        when (parsingProtocolFromReceiveMsg(text)) {
                            MAGO_PROTOCOL.PROTOCOL_2.id -> { // 클라이언트 연결 요청 후 응답 ACK
                                sendProtocol(3)
                            }
                            MAGO_PROTOCOL.PROTOCOL_4.id -> { // 클라이언트 UTTERANCE 요청 후 응답 ACK -> audio stream start
                                audioStreamImpl.availableAudioStream()
                            }
//                            MAGO_PROTOCOL.PROTOCOL_12.id -> { // -> audio stream start
//                                audioStreamImpl.startAudioStream()
//                            }
//                            MAGO_PROTOCOL.PROTOCOL_12.id -> { // -> audio stream start
//                                audioStreamImpl.startAudioStream()
//                            }
                        }
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    Log.d("WEBSOCKET", "onFailure: $t || $response")
                    coroutineScopeOnDefault {
                        audioStreamImpl.disConnectedWebSocket()
                    }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    Log.d("WEBSOCKET", "onClosed: $code || $reason")
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosing(webSocket, code, reason)
                    Log.d("WEBSOCKET", "onClosing: $code || $reason")
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                    Log.d("WEBSOCKET", "onMessage: $bytes")
                }
            })
        }
    }

    /** 서버에서 전달받은 메세지 파싱 */
    private fun parsingProtocolFromReceiveMsg(text: String): String {
        val receivedMsg: MessageProtocol = Gson().fromJson(text, MessageProtocol::class.java)
        return receivedMsg.header.protocol_id
    }

    /** 오디오 녹음기 초기화 */
    @SuppressLint("MissingPermission")
    fun initAudioRecorder() {
        if (audioRecord != null) {
            stopAudioRecord()
        }
        audioRecord = AudioRecord(
            RECORDER_SOURCE, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BUFFER_SIZE_RECORDING
        )

        audioRecord?.startRecording()
    }

    /** 웹 소켓으로 이벤트 전달하기 */
    private fun sendProtocol(protocolNum: Int) {
        var protocolId = ""

        when (protocolNum) {
            1 -> protocolId = MAGO_PROTOCOL.PROTOCOL_1.id
            3 -> protocolId = MAGO_PROTOCOL.PROTOCOL_3.id
            11 -> protocolId = MAGO_PROTOCOL.PROTOCOL_11.id
            13 -> protocolId = MAGO_PROTOCOL.PROTOCOL_13.id
        }

        val msg = Gson().toJson(MessageProtocol(header = HeaderInfo(protocol_id = protocolId), body = null))
        webSocket?.send(msg)
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