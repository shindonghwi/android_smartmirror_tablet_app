package orot.apps.sognora_websocket_audio

import orot.apps.sognora_websocket_audio.model.protocol.MessageProtocol

interface IAudioStreamManager {
    suspend fun connectedWebSocket()
    suspend fun disConnectedWebSocket()
    suspend fun failedWebSocket()
    suspend fun availableAudioStream()
    suspend fun streamAiTalk(id: String, receivedMsg: MessageProtocol)
}