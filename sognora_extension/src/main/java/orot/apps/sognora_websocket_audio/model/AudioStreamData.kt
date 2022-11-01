package orot.apps.sognora_websocket_audio.model

sealed class AudioStreamData<out T> {
    data class Success<out T>(val msg: T? = null) : AudioStreamData<T>()
    object Closed : AudioStreamData<Nothing>()
    object Failed : AudioStreamData<Nothing>()
    object Idle : AudioStreamData<Nothing>()
}
