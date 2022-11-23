package mago.apps.sognorawebsocket.websocket

import kotlinx.coroutines.flow.StateFlow
import mago.apps.sognorawebsocket.websocket.model.WebSocketState

interface SognoraWebSocket {
    fun initWebSocket(url: String)
    fun sendBuffer(buf: ByteArray, bufferSize: Int)
    fun sendMsg(msg: String)
    fun setWebSocketListener(listener: SognoraWebSocketListener)
    fun getWebSocketState(): StateFlow<WebSocketState>
    fun close()
}