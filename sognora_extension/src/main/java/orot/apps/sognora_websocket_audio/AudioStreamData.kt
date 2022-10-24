package orot.apps.sognora_websocket_audio

sealed class AudioStreamData<out T>{
    object WebSocketConnected: AudioStreamData<Nothing>()
    object WebSocketDisConnected: AudioStreamData<Nothing>()
    data class ReceivedData(val msg: String) : AudioStreamData<String>()
}
