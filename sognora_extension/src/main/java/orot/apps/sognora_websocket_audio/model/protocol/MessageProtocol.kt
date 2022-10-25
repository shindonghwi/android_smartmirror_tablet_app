package orot.apps.sognora_websocket_audio.model.protocol

enum class MAGO_PROTOCOL(val id: String) {
    PROTOCOL_1("APP_DIALOG_START_REQ"),
    PROTOCOL_2("APP_DIALOG_START_ACK"),
    PROTOCOL_3("APP_UTTERANCE_START_REQ"),
    PROTOCOL_5("AUDIO STREAM"), // not used
    PROTOCOL_6("EPD"),  // not used
    PROTOCOL_7("STT_REQ"), // not used
    PROTOCOL_8("STT_RES"), // not used
    PROTOCOL_9("STT_REQ"), // not used
    PROTOCOL_10("STT_RES"), // not used
    PROTOCOL_11("APP_UTTERANCE_END_REQ"),
    PROTOCOL_12("APP_UTTERANCE_END_ACK"),
    PROTOCOL_13("APP_DIALOG_END_REQ"),
    PROTOCOL_14("APP_DIALOG_END_ACK")
}

@kotlinx.serialization.Serializable
data class MessageProtocol(
    val header: HeaderInfo,
    val body: BodyInfo?,
)

data class HeaderInfo(
    val protocol_id: String,
    val protocol_version: String = "v1.0",
    val timestamp: Long = System.currentTimeMillis() / 1000
)

data class BodyInfo(
    val code: Int,
    val message: String,
    val reason: String,
)