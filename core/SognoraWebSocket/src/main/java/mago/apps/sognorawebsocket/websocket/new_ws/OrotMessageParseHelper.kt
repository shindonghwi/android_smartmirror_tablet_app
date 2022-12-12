package mago.apps.sognorawebsocket.websocket.new_ws

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import mago.apps.sognorawebsocket.websocket.model.ChatData
import mago.apps.sognorawebsocket.websocket.model.OROT_PROTOCOL
import mago.apps.sognorawebsocket.websocket.model.callback.IActionCallback
import mago.apps.sognorawebsocket.websocket.model.protocol.MessageProtocol
import mago.apps.sognorawebsocket.websocket.model.protocol.body.BodyInfo

class OrotMessageParseHelper(private val iActionCallback: IActionCallback?) {

    var currentMessageProtocol: MessageProtocol? = null
    var beforeBody: BodyInfo? = null
    var chatList = MutableStateFlow(arrayListOf<ChatData>())


    fun activeActionCallback(receivedMsg: String) {
        val parseMsg: MessageProtocol = Gson().fromJson(receivedMsg, MessageProtocol::class.java)
        Log.w("OrotMessageParseHelper", "onMessage: $receivedMsg", )

        val protocol = parseMsg.header.protocol_id
        val device = parseMsg.header.device
        val body = parseMsg.body
        val action = parseMsg.body?.action
        val voice = body?.voice
        val display = body?.display
        val measurement = body?.measurement

        saveMessageInfo(info = parseMsg)
        saveBeforeBody(bodyInfo = body)
        saveChatData(protocol = protocol, chatMsg = voice?.text, iActionCallback)

        when (protocol) {
            OROT_PROTOCOL.PROTOCOL_2.id -> iActionCallback?.onStartConversation(voice?.text, voice?.text)
            OROT_PROTOCOL.PROTOCOL_11.id -> iActionCallback?.onReceivedSaidMe(voice?.text)
            OROT_PROTOCOL.PROTOCOL_12.id -> {
                when(action){
                    "measurement" -> {
                        iActionCallback?.moveMeasurement(voice?.text, voice?.text)
                        return
                    }
                    "doctorcall" -> {
                        iActionCallback?.onDoctorCall(voice?.text)
                    }
                    "end" -> {
                        iActionCallback?.onConversationEnd(voice?.text, voice?.text)
                        return
                    }
                    "exit" -> {
                        iActionCallback?.onConversationExit(voice?.text, voice?.text)
                        return
                    }
                }
                if (display?.measurement != null){
                    iActionCallback?.showHealthOverView(voice?.text)
                }else{
                    iActionCallback?.onReceivedSaidAI(voice?.text, voice?.text)
                }
            }
            OROT_PROTOCOL.PROTOCOL_17.id -> {
                when(device){
                    "Watch" -> iActionCallback?.onReceivedDeviceWatchData(measurement)
                    "Chair" -> iActionCallback?.onReceivedDeviceChairData(measurement)
                }
            }
            OROT_PROTOCOL.PROTOCOL_19.id -> iActionCallback?.onMeasurementEnd()
            OROT_PROTOCOL.PROTOCOL_99.id -> iActionCallback?.onReceivedFallback(voice?.text, voice?.text)

            else -> {}
        }

    }

    /** 채팅내역을 보관한다. */
    private fun saveChatData(protocol: String?, chatMsg: String?, iActionCallback: IActionCallback?) {
        val isUser = protocol == OROT_PROTOCOL.PROTOCOL_11.id

        if (!chatMsg.isNullOrEmpty()){
            iActionCallback?.saveChatMessage(chatMsg, isUser)
        }
    }

    /** 서버에서 전달받은 body 정보를 저장한다. */
    private fun saveBeforeBody(bodyInfo: BodyInfo?) {
        beforeBody = bodyInfo
    }

    /** 서버에서 전달받은 MessageProtocol 정보를 저장한다. */
    private fun saveMessageInfo(info: MessageProtocol) {
        currentMessageProtocol = info
    }
}