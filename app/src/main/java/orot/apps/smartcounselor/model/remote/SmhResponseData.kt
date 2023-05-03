package orot.apps.smartcounselor.model.remote

import mago.apps.sognorawebsocket.websocket.model.protocol.body.ReceivedMeasurementInfo
import mago.apps.sognorawebsocket.websocket.model.protocol.body.RecommendationInfo
import mago.apps.sognorawebsocket.websocket.model.protocol.body.RiskPredictionInfo
import mago.apps.sognorawebsocket.websocket.model.protocol.body.TodayRecommendationData

data class SmhResponseData(
    val data: SmhUserInputData? = null,
    val recommendation: Recommendation? = null,
    val risk_prediction: RiskPredictionInfo? = null,
) {
    data class Recommendation(
        val medication: List<String>? = null,
        val measurement: ReceivedMeasurementInfo? = null,
        val recommendation: RecommendationInfo? = null,
        val today_recommendation: TodayRecommendationData? = null
    )
}
