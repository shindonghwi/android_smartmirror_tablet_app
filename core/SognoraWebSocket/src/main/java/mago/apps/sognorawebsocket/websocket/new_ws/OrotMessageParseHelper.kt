package mago.apps.sognorawebsocket.websocket.new_ws

import android.util.Log
import com.google.gson.Gson
import mago.apps.sognorawebsocket.websocket.model.OROT_PROTOCOL
import mago.apps.sognorawebsocket.websocket.model.callback.IActionCallback
import mago.apps.sognorawebsocket.websocket.model.protocol.MessageProtocol
import mago.apps.sognorawebsocket.websocket.model.protocol.body.BodyInfo

class OrotMessageParseHelper(private val iActionCallback: IActionCallback?) {

    var beforeBody: BodyInfo? = null

    fun parse(receivedMsg: String) {
        val parseMsg: MessageProtocol = Gson().fromJson(receivedMsg, MessageProtocol::class.java)
        Log.w("OrotMessageParseHelper", "onMessage: $receivedMsg", )

        val protocol = parseMsg.header.protocol_id
        val device = parseMsg.header.device
        val body = parseMsg.body
        val voice = body?.voice
        val display = body?.display
        val measurement = body?.measurement

        saveBeforeBody(bodyInfo = body)

        when (protocol) {
            OROT_PROTOCOL.PROTOCOL_2.id -> iActionCallback?.onStartConversation(voice?.text, voice?.text)
            OROT_PROTOCOL.PROTOCOL_11.id -> iActionCallback?.onReceivedSaidMe(voice?.text)
            OROT_PROTOCOL.PROTOCOL_12.id -> {
                if (display?.measurement != null){
                    iActionCallback?.showHealthOverView(voice?.text)
                }else{
                    iActionCallback?.onReqActiveSpeak()
                }
            }
            OROT_PROTOCOL.PROTOCOL_19.id -> iActionCallback?.onAckEndMeasurement()
            OROT_PROTOCOL.PROTOCOL_99.id -> iActionCallback?.onReceivedFallback()

            else -> {}
        }

    }

    /** 서버에서 전달받은 body 정보를 저장한다. */
    private fun saveBeforeBody(bodyInfo: BodyInfo?) {
        beforeBody = bodyInfo
    }

}