package mago.apps.sognorawebsocket.websocket.model.callback

enum class OrotActionType {
    IDLE,
    GUIDE,
    SAID_ME,
    SAID_ME_END,
    SAID_AI,
    WAITING_RESULT,
    DOCTOR_CALL,
    MEASUREMENT_START,
    MEASUREMENT_END,
    CONVERSATION_END,
    CONVERSATION_EXIT,
    FALL_BACK,
    HEALTH_OVERVIEW
}