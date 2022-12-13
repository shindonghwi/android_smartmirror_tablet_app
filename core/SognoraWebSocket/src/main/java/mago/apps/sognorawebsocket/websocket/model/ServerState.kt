package mago.apps.sognorawebsocket.websocket.model

sealed class ServerState{
    object Idle : ServerState()
    object Opened : ServerState()
    object Connected : ServerState()
    object Error : ServerState()
}