package mago.apps.sognorawebsocket.websocket.model.callback

import mago.apps.sognorawebsocket.websocket.model.protocol.body.DisplayInfo
import mago.apps.sognorawebsocket.websocket.model.protocol.body.RequestedMeasurementInfo
import mago.apps.sognorawebsocket.websocket.model.protocol.body.RiskPredictionInfo

interface IActionCallback {

    fun onStartDialog(
        actionType: OrotActionType = OrotActionType.IDLE
    )

    /** 대화 시작 */
    fun onStartConversation(
        voiceComment: String?,
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.GUIDE
    )

    fun onDoctorCall(
        voiceComment: String?,
        actionType: OrotActionType = OrotActionType.DOCTOR_CALL
    )

    fun onMeasurementEnd(
        actionType: OrotActionType = OrotActionType.MEASUREMENT_END
    )

    fun onConversationExit(
        voiceComment: String?,
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.CONVERSATION_EXIT
    )

    fun onConversationEnd(
        voiceComment: String?,
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.CONVERSATION_END
    )

    fun onReceivedFallback(
        voiceComment: String?,
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.FALL_BACK
    )

    fun onReceivedDeviceWatchData(measurement: RequestedMeasurementInfo?)
    fun onReceivedDeviceChairData(measurement: RequestedMeasurementInfo?)

    /** 내가 말한내용 수신 */
    fun onReceivedSaidMe(
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.SAID_ME
    )

    /** AI의 질문 수신 */
    fun onReceivedSaidAI(
        voiceComment: String?,
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.SAID_AI
    )

    fun moveMeasurement(
        voiceComment: String?,
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.MEASUREMENT_START
    )

    /** 헬스 측정 결과 보여주기 */
    fun showHealthOverView(
        voiceComment: String?,
        display: DisplayInfo?,
        riskPrediction: RiskPredictionInfo?,
        actionType: OrotActionType = OrotActionType.HEALTH_OVERVIEW
    )

    fun saveChatMessage(
        msg: String,
        isUser: Boolean
    )
}