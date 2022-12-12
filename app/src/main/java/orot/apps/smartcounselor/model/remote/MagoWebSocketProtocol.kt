package orot.apps.smartcounselor.model.remote

import android.os.Build

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
    PROTOCOL_15("DEVICE_MEASUREMENT_ENTRY_REQ"),
    PROTOCOL_16("DEVICE_MEASUREMENT_ENTRY_ACK"),
    PROTOCOL_17("DEVICE_MEASUREMENT_DELIVERY"),
    PROTOCOL_18("APP_MEASUREMENT_ENTRY_REQ"),
    PROTOCOL_19("APP_MEASUREMENT_ENTRY_ACK"),
    PROTOCOL_20("APP_DIALOG_END_REQ"),
    PROTOCOL_21("APP_DIALOG_END_ACK"),
    PROTOCOL_99("STT_FALLBACK_DELIVERY")
}

interface IHeaderInfo {
    fun toStream(type: String, age: Int, gender: String): HeaderInfo
}

interface IMeasurementInfo {
    fun toMeasurement(beforeBody: BodyInfo?, measurement: RequestedMeasurementInfo?): BodyInfo
}

data class MessageProtocol(
    val header: HeaderInfo,
    val body: BodyInfo? = null,
)

data class HeaderInfo(
    val protocol_id: String? = null,
    val protocol_version: String = "1.0",
    val timestamp: Long = System.currentTimeMillis() / 1000,
    val device: String = "Mirror",
    val model: String? = null,
    val age: Int? = null,
    val gender: String? = null,
) : IHeaderInfo {
    override fun toStream(type: String, age: Int, gender: String): HeaderInfo {
        return HeaderInfo(
            protocol_id = type,
            protocol_version = this.protocol_version,
            timestamp = this.timestamp,
            device = "Mirror",
            model = Build.MODEL,
            age = age,
            gender = gender
        )
    }
}

data class BodyInfo(
    var before: BodyInfo? = null,
    val measurement: RequestedMeasurementInfo? = null,
    val action: String? = null,
    val turn: Int? = null,
    val voice: VoiceInfo? = null,
    val display: DisplayInfo? = null,
    val code: Int? = null,
    val message: String? = null,
    val reason: String? = null
) : IMeasurementInfo {
    override fun toMeasurement(beforeBody: BodyInfo?, measurement: RequestedMeasurementInfo?): BodyInfo {
        return BodyInfo(
            before = beforeBody,
            measurement = measurement
        )
    }
}

data class RequestedMeasurementInfo(
    val medication: List<String>,
    val bloodPressureSystolic: Int,
    val bloodPressureDiastolic: Int,
    val glucose: Int,
    val heartRate: Int,
    val bodyTemperature: Float,
    val height: Float,
    val weight: Float,
    val bodyMassIndex: Float,
)

data class ReceivedMeasurementInfo(
    val bloodPressureSystolic: MeasurementItemData,
    val bloodPressureDiastolic: MeasurementItemData,
    val glucose: MeasurementItemData,
    val heartRate: MeasurementItemData,
    val bodyTemperature: MeasurementItemData,
    val height: MeasurementItemData,
    val weight: MeasurementItemData,
    val bodyMassIndex: MeasurementItemData,
)

data class MeasurementItemData(
    val valueQuantity: ValueQuantityData,
    val status: String
)

data class ValueQuantityData(
    val value: Float,
    val unit: String
)

data class VoiceInfo(
    val id: String,
    val text: String,
    val type: String,
    val uri: String,
)

data class DisplayInfo(
    val id: String,
    val medication: List<String>,
    val measurement: ReceivedMeasurementInfo? = null,
    val recommendation: RecommendationInfo,
    val today_recommendation: TodayRecommendationData,
)

data class TodayRecommendationData(
    val food: String,
    val exercise: String
)

data class RecommendationInfo(
    val items: List<String>,
    val warning: List<String>,
    val current_status: List<String>,
    val food: List<RecommendationDetailData>,
    val weight: List<RecommendationDetailData>,
    val exercise: List<RecommendationDetailData>,
    val drinking_smoking: List<RecommendationDetailData>,
    val sheet_name: String,
    val line: Int
)

data class RecommendationDetailData(
    val weight: Float,
    val content: String
)
