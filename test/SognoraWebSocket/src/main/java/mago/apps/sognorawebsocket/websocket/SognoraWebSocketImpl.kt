package mago.apps.sognorawebsocket.websocket

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okio.ByteString
import okio.ByteString.Companion.toByteString
import javax.inject.Inject

class SognoraWebSocketImpl @Inject constructor(private val listener: SognoraWebSocketListener) :
    SognoraWebSocket() {

    val TAG = "SognoraWebSocket"

//    val webSocketURL: String = "ws://172.30.1.15:8080/ws/chat"
    var webSocket: WebSocket? = null
    private var request: Request? = null
    private var client: OkHttpClient? = null

    /** 웹 소켓 연결하기 */
    override fun initWebSocket(url: String) {

        try {
            request = Request.Builder().url(url).build()
            client = OkHttpClient()
        } catch (e: Exception) {
            Log.e(TAG, "initWebSocket: ${e.message}")
            return
        }

        request?.run {
            try {
                webSocket = client!!.newWebSocket(this, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        super.onOpen(webSocket, response)
                        listener.open(response)
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        super.onMessage(webSocket, text)
                        listener.onMessageText(text)
                    }

                    override fun onFailure(
                        webSocket: WebSocket, t: Throwable, response: Response?
                    ) {
                        super.onFailure(webSocket, t, response)
                        listener.fail(response, t)
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        super.onClosed(webSocket, code, reason)
                        listener.close(code, reason)
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        super.onClosing(webSocket, code, reason)
                        listener.onClosing(code, reason)
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                        super.onMessage(webSocket, bytes)
                        listener.onMessageByteString(bytes)
                    }
                })
            } catch (e: Exception) {
                Log.e(TAG, "initWebSocket Error: ${e.message}")
            }
        }
    }

    override fun sendBuffer(buf: ByteArray, bufferSize: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                webSocket?.send(buf.toByteString(0, bufferSize))
            } catch (e: Exception) {
                Log.e(TAG, "sendBuffer Error: ${e.message}")
            }
        }
    }

    override fun close() {
        webSocket?.cancel()
        request = null
        webSocket = null
    }

}