package mago.apps.sognorawebsocket.websocket.model.callback

interface IActionCallback {

    fun onStartDialog()

    /** 대화 시작 */
    fun onStartConversation(
        voiceComment: String?,
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.GUIDE
    )

    /** 말하기 시작 */
    fun onReqActiveSpeak()

    fun onAckEndMeasurement()

    fun onReceivedFallback()

    /** 내가 말한내용 수신 */
    fun onReceivedSaidMe(
        displayComment: String?,
        actionType: OrotActionType = OrotActionType.SAID_ME
    )

//    /** AI의 질문 수신 */
//    fun onReceivedSaidAI(
//        voiceComment: String?,
//        displayComment: String?,
//        actionType: OrotActionType = OrotActionType.SAID_AI
//    )

    /** 헬스 측정 결과 보여주기 */
    fun showHealthOverView(
        voiceComment: String?,
        actionType: OrotActionType = OrotActionType.HEALTH_OVERVIEW
    )
}