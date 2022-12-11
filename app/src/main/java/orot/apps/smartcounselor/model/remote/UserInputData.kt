package orot.apps.smartcounselor.model.remote

interface IUserInputData {
    fun isEmptyCheck(): Boolean
}

data class UserInputData(
    val age: Int = 60,
    val gender: String = "M",
    var medication: List<String> = listOf(), // 약물 복용력
    var bloodPressureSystolic: Int = 0, // 수축기 혈압
    var bloodPressureDiastolic: Int = 0,// 이완기 혈압
    var glucose: Int = 0, // 공복혈당
    var heartRate: Int = 0,// 맥박
    var bodyTemperature: Float = 0f, // 체온
    var height: Float = 0f, // 키
    var weight: Float = 0f,// 체중
    var bodyMassIndex: Float = 0f,// 체질량 지수
) : IUserInputData {
    override fun isEmptyCheck(): Boolean {
        return medication.isNullOrEmpty() ||
                bloodPressureSystolic == 0 ||
                bloodPressureDiastolic == 0 ||
                glucose == 0 ||
                heartRate == 0 ||
                bodyTemperature == 0f ||
                height == 0f ||
                weight == 0f ||
                bodyMassIndex == 0f
    }
}
