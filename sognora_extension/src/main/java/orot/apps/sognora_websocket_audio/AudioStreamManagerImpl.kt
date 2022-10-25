package orot.apps.sognora_websocket_audio

import orot.apps.sognora_websocket_audio.model.AudioStreamData

interface AudioStreamManagerImpl {
    suspend fun connectedWebSocket()
    suspend fun disConnectedWebSocket()
    suspend fun receivedMsg(msg: AudioStreamData.ReceivedData<String>)
}