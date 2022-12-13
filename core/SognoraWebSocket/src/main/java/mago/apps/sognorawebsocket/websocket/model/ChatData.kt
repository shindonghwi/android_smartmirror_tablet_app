package mago.apps.sognorawebsocket.websocket.model

/** AI와 대화가 끝난 후 대화목록을 보관하기 위해서 사용. */
data class ChatData(
    val msg: String,
    val isUser: Boolean
)