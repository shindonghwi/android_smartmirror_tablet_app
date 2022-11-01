package orot.apps.sognora_websocket_audio

interface IAudioStreamManager {
    suspend fun connectedWebSocket()
    suspend fun disConnectedWebSocket()
    suspend fun failedWebSocket()
    suspend fun availableAudioStream()
    suspend fun streamAiTalk()
}