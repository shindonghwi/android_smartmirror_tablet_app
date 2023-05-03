package orot.apps.smartcounselor.model.remote

data class SmhUserInputData(
    val user: UserInfo,
    val measurement: Measurement,
) {
    data class UserInfo(
        val id: String,
        val age: Int,
        val gender: String
    )

    data class Measurement(
        val medication: List<String> = listOf(),
        val bloodPressureSystolic: Int = 0,
        val bloodPressureDiastolic: Int = 0,
        val glucose: Int = 0,
        val bodyTemperature: Float = 0f,
        val heartRate: Int = 0,
        val height: Float = 0f,
        val weight: Float = 0f,
    )
}
