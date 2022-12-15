package orot.apps.smartcounselor.model.remote

import androidx.compose.runtime.mutableStateListOf

interface IUserInputData {
    fun isEmptyCheck(): Boolean
}

data class UserData(
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
    var name: String = ""
) : IUserInputData {
    override fun isEmptyCheck(): Boolean {
        return medication.isEmpty() ||
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


val userList = mutableStateListOf<UserData>().apply {
    // 1번환자
    add(
        UserData(
            age = 65,
            gender = "W",
            medication = listOf("dm"),
            bloodPressureSystolic = 128,
            bloodPressureDiastolic = 72,
            glucose = 145,
            heartRate = 73,
            bodyTemperature = 36.4f,
            height = 155f,
            weight = 61.0f,
            bodyMassIndex = 25.4f,
            name = "환자1 - 당뇨"
        )
    )

    // 2번환자
    add(
        UserData(
            age = 71,
            gender = "M",
            medication = listOf(""),
            bloodPressureSystolic = 115,
            bloodPressureDiastolic = 68,
            glucose = 116,
            heartRate = 92,
            bodyTemperature = 36.2f,
            height = 174f,
            weight = 55.0f,
            bodyMassIndex = 18.16f,
            name = "환자2 - 정상인/고혈당"
        )
    )

    // 3번환자
    add(
        UserData(
            age = 63,
            gender = "M",
            medication = listOf("htn"),
            bloodPressureSystolic = 148,
            bloodPressureDiastolic = 86,
            glucose = 99,
            heartRate = 65,
            bodyTemperature = 36.9f,
            height = 185f,
            weight = 94f,
            bodyMassIndex = 27.5f,
            name = "환자3 - 고혈압"
        )
    )

    // 4번환자
    add(
        UserData(
            age = 68,
            gender = "W",
            medication = listOf("htn"),
            bloodPressureSystolic = 141,
            bloodPressureDiastolic = 73,
            glucose = 95,
            heartRate = 85,
            bodyTemperature = 36.6f,
            height = 157f,
            weight = 58f,
            bodyMassIndex = 23.5f,
            name = "환자4 - 정상인/고혈압"
        )
    )

    // 5번환자
    add(
        UserData(
            age = 66,
            gender = "M",
            medication = listOf("htn", "dm"),
            bloodPressureSystolic = 150,
            bloodPressureDiastolic = 96,
            glucose = 170,
            heartRate = 72,
            bodyTemperature = 36.5f,
            height = 175f,
            weight = 78f,
            bodyMassIndex = 25.5f,
            name = "환자5 - 당뇨/고혈압"
        )
    )
}