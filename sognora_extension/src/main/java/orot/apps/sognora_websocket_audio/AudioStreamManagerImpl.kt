package orot.apps.sognora_websocket_audio

interface AudioStreamManagerImpl {
    suspend fun connectedWebSocket()
    suspend fun disConnectedWebSocket()
    suspend fun receivedMsg(msg: AudioStreamData.ReceivedData<String>)
}