package mago.apps.sognorawebsocket.websocket

import dagger.Binds
import dagger.Provides
import okhttp3.Response
import okio.ByteString


interface SognoraWebSocketListener {
    var autoLogin: Boolean
//    abstract fun open(response: Response)
//    abstract fun onMessageText(msg: String)
//    abstract fun onMessageByteString(byteString: ByteString)
//    abstract fun fail(response: Response?, t: Throwable)
//    abstract fun close(code: Int, reason: String)
//    abstract fun onClosing(code: Int, reason: String)
}
