package orot.apps.sognora_websocket_audio.model.protocol

enum class MAGO_PROTOCOL(val id: String) {
    PROTOCOL_1("APP_DIALOG_START_REQ"),
    PROTOCOL_2("APP_DIALOG_START_ACK"),
    PROTOCOL_3("APP_UTTERANCE_START_REQ"),
    PROTOCOL_4("APP_UTTERANCE_START_ACK"),
    PROTOCOL_5("AUDIO STREAM"), // not used
    PROTOCOL_6("EPD"),  // not used
    PROTOCOL_7("STT_REQ"), // not used
    PROTOCOL_8("STT_RES"), // not used
    PROTOCOL_9("STT_REQ"), // not used
    PROTOCOL_10("STT_RES"), // not used
    PROTOCOL_11("STT_RESPONSE_DELIVERY"),
    PROTOCOL_12("AI_NEXT_QUESTION_DELIVERY"),
    PROTOCOL_13("APP_UTTERANCE_END_REQ"),
    PROTOCOL_14("APP_UTTERANCE_END_ACK"),
    PROTOCOL_15("APP_MEASUREMENT_ENTRY_REQ"),
    PROTOCOL_16("APP_MEASUREMENT_ENTRY_ACK"),
    PROTOCOL_17("APP_DIALOG_END_REQ"),
    PROTOCOL_18("APP_DIALOG_END_ACK")
}


data class MessageProtocol(
    val header: HeaderInfo,
    val body: BodyInfo?
)

data class HeaderInfo(
    val protocol_id: String,
    val protocol_version: String = "v1.0",
    val timestamp: Long = System.currentTimeMillis() / 1000
)

data class BodyInfo(
    val ment: MentInfo,
    val action: String,
    val turn: Int,
    val code: Int,
    val message: String,
    val reason: String,
    val measurement: MeasurementInfo
)

data class MentInfo(
    val id: String,
    val text: String,
    val type: String,
    val uri: String,
)

data class MeasurementInfo(
    val blood_pressure: List<Int>
)
