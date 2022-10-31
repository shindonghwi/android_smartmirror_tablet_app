package orot.apps.sognora_websocket_audio.model

sealed class AudioStreamData<out T> {
    data class Available<out T>(val msg: T? = null) : AudioStreamData<T>()
    object UnAvailable : AudioStreamData<Nothing>()
}
