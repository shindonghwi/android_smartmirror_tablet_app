package mago.apps.sognorawebsocket.websocket

import okhttp3.Response
import okio.ByteString


interface SognoraWebSocketListener {
    fun open(response: Response)
    fun onMessageText(msg: String)
    fun onMessageByteString(byteString: ByteString)
    fun fail(response: Response?, t: Throwable)
    fun close(code: Int, reason: String)
    fun onClosing(code: Int, reason: String)
}
