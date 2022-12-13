package mago.apps.sognorawebsocket.websocket.new_ws

import okhttp3.Response
import okio.ByteString


interface OrotWebSocketListener {
    fun open(response: Response)
    fun onMessageText(msg: String)
    fun fail(response: Response?, t: Throwable)
    fun close(code: Int, reason: String)
    fun onClosing(code: Int, reason: String)
}
