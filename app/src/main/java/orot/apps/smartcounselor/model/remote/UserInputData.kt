package orot.apps.smartcounselor.model.remote
import kotlin.reflect.full.memberProperties

data class UserInputData(
    var userAge: Int = 0, // Default: 0세
    var userSex: Boolean = true, // Default: 남
    var medication: List<String>? = null, // 약물 복용력
    var bloodPressureSystolic: Int? = null, // 수축기 혈압
    var bloodPressureDiastolic: Int? = null,// 이완기 혈압
    var glucose: Int? = null, // 공복혈당
    var heartRate: Int? = null,// 맥박
    var bodyTemperature: Float? = null, // 체온
    var height: Float? = null, // 키
    var weight: Float? = null,// 체중
    var bodyMassIndex: Float? = null,// 체질량 지수
)

fun UserInputData.asMap(): Map<String, Any?> = UserInputData::class.memberProperties.associate { it.name to it.get(this)}
