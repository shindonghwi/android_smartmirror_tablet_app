package orot.apps.sognora_websocket_audio

sealed class AudioStreamData<out T>{
    object WebSocketConnected: AudioStreamData<Nothing>()
    object WebSocketDisConnected: AudioStreamData<Nothing>()
    data class ReceivedData<out T>(val msg: T) : AudioStreamData<T>()
}
