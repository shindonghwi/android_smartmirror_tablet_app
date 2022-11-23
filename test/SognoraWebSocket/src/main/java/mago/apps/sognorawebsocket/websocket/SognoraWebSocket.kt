package mago.apps.sognorawebsocket.websocket

interface SognoraWebSocket {
    fun initWebSocket(url: String)
    fun sendBuffer(buf: ByteArray, bufferSize: Int)
    fun close()
}