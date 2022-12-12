package mago.apps.sognorawebsocket.websocket.new_ws

import kotlinx.coroutines.flow.MutableStateFlow
import mago.apps.sognorawebsocket.websocket.model.ServerState
import mago.apps.sognorawebsocket.websocket.model.callback.IActionCallback

interface IOrotWebSocket {
    fun initWebSocket(url: String)
    fun sendBuffer(buf: ByteArray, bufferSize: Int)
    fun sendMsg(msg: String)
    fun setActionCallback(callback: IActionCallback)
    fun close()
    fun currentServerState(): MutableStateFlow<ServerState>
}