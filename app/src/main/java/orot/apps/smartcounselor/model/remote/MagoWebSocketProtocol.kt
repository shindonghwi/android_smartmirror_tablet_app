package orot.apps.smartcounselor.model.remote

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
    PROTOCOL_18("APP_DIALOG_END_ACK"),
    PROTOCOL_99("STT_FALLBACK_DELIVERY")
}

data class MessageProtocol(
    val header: HeaderInfo,
    val body: BodyInfo? = null,
)

data class HeaderInfo constructor(
    val protocol_id: String? = null,
    val protocol_version: String = "1.0",
    val timestamp: Long = System.currentTimeMillis() / 1000,
    val device: String? = null,
    val age: Int? = null,
    val gender: String? = null,
)

data class BodyInfo constructor(
    var before: BodyInfo? = null,
    val measurement: MeasurementInfo? = null,
    val action: String? = null,
    val turn: Int? = null,
    val voice: VoiceInfo? = null,
    val display: DisplayInfo? = null,
    val code: Int? = null,
    val message: String? = null,
    val reason: String? = null
)

data class MeasurementInfo(
    val medication: List<String>,
    val bloodPressureSystolic: String,
    val bloodPressureDiastolic: String,
    val glucose: String,
    val heartRate: String,
    val bodyTemperature: String,
    val height: String,
    val weight: String,
    val bodyMassIndex: String,
)

data class VoiceInfo(
    val id: String,
    val text: String,
    val type: String,
    val uri: String,
)

data class DisplayInfo(
    val id: String,
    val recommendation: RecommendationInfo
)

data class RecommendationInfo(
    val food: String,
    val weight: String,
    val exercise: String,
)