package mago.apps.sognorawebsocket.websocket

abstract class SognoraWebSocket {
    abstract fun initWebSocket(url: String)
    abstract fun sendBuffer(buf: ByteArray, bufferSize: Int)
    abstract fun close()
}