package orot.apps.sognora_websocket_audio.model.protocol

data class MessageProtocol(
    val header: HeaderInfo,
    val body: BodyInfo,
)

data class HeaderInfo(
    val protocol_id: String,
    val protocol_version: String,
    val timestamp: Long,
)

data class BodyInfo(
    val code: Int,
    val message: String,
    val reason: String,
)