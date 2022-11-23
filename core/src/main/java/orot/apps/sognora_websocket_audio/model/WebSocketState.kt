package orot.apps.sognora_websocket_audio.model

sealed class WebSocketState{
    object Idle : WebSocketState()
    object Connected : WebSocketState()
    object DisConnected : WebSocketState()
    object Closing : WebSocketState()
    object Failed : WebSocketState()
}