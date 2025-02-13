package mago.apps.sognorawebsocket.websocket.model.protocol.body

data class BodyInfo(
    var before: BodyInfo? = null,
    val measurement: RequestedMeasurementInfo? = null,
    val action: String? = null,
    val turn: Int? = null,
    val voice: VoiceInfo? = null,
    val display: DisplayInfo? = null,
    val risk_prediction: RiskPredictionInfo? = null,
    val code: Int? = null,
    val message: String? = null,
    val reason: String? = null
) : IMeasurementInfo {
    override fun toMeasurement(
        beforeBody: BodyInfo?,
        measurement: RequestedMeasurementInfo?
    ): BodyInfo {
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

data class VoiceInfo(
    val id: String,
    val text: String,
    val type: String,
    val uri: String,
)

data class DisplayInfo(
    val id: String,
    val medication: List<String>? = null,
    val measurement: ReceivedMeasurementInfo? = null,
    val recommendation: RecommendationInfo? = null,
    val today_recommendation: TodayRecommendationData? = null,
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
    val name: String? = null,
    val value: Float,
    val unit: String,
    val score: Int,
    val min: Int? = null,
    val max: Int? = null,
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

data class TodayRecommendationData(
    val food: String?,
    val exercise: String?
)

data class RecommendationDetailData(
    val weight: Float,
    val content: String
)

data class RiskPredictionInfo(
    val measurement: RiskPredictionMeasurementData,
    val recommendation: RiskPredictionRecommendationData
)

data class RiskPredictionMeasurementData(
    val bloodPressureSystolic: ValueQuantityData?,
    val bloodPressureDiastolic: ValueQuantityData?,
    val bodyMassIndex: ValueQuantityData?,
    val glucose: ValueQuantityData?,
    val heartRate: ValueQuantityData?,
)

data class RiskPredictionRecommendationData(
    val current_status: Int,
    val goal_status: Int,
    val sentence: String,
)