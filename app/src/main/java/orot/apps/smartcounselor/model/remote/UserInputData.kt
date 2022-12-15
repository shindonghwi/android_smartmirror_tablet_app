package orot.apps.smartcounselor.model.remote

import androidx.compose.runtime.mutableStateListOf

interface IUserInputData {
    fun isEmptyCheck(): Boolean
}

data class UserData(
    val age: Int = 60,
    val gender: String = "male",
    var medication: List<String> = listOf(), // 약물 복용력
    var bloodPressureSystolic: Int = 0, // 수축기 혈압
    var bloodPressureDiastolic: Int = 0,// 이완기 혈압
    var glucose: Int = 0, // 공복혈당
    var heartRate: Int = 0,// 맥박
    var bodyTemperature: Float = 0f, // 체온
    var height: Float = 0f, // 키
    var weight: Float = 0f,// 체중
    var bodyMassIndex: Float = 0f,// 체질량 지수
    var name: String = "",
    val history: UserHistoryData? = null
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

data class UserHistoryData(
    val bpList: List<Int>,
    val glucoseList: List<Int>,
    val hrList: List<Int>
)

val userList = mutableStateListOf<UserData>().apply {
    // 1번환자
    add(
        UserData(
            age = 65,
            gender = "female",
            medication = listOf("dm"),
            bloodPressureSystolic = 128,
            bloodPressureDiastolic = 72,
            glucose = 145,
            heartRate = 73,
            bodyTemperature = 36.4f,
            height = 155f,
            weight = 61.0f,
            bodyMassIndex = 25.4f,
            name = "환자1 - 당뇨",
            history = UserHistoryData(
                bpList = listOf(128, 126, 132, 130, 132, 132, 130, 132, 134, 135),
                glucoseList = listOf(145, 138, 160, 157, 143, 155, 132, 144, 152, 150),
                hrList = listOf(73, 70, 74, 72, 75, 76, 72, 70, 73, 75)
            )
        )
    )

    // 2번환자
    add(
        UserData(
            age = 71,
            gender = "male",
            medication = listOf(""),
            bloodPressureSystolic = 115,
            bloodPressureDiastolic = 68,
            glucose = 116,
            heartRate = 92,
            bodyTemperature = 36.2f,
            height = 174f,
            weight = 55.0f,
            bodyMassIndex = 18.16f,
            name = "환자2 - 정상인/고혈당",
            history = UserHistoryData(
                bpList = listOf(115, 110, 114, 118, 115, 114, 116, 118, 116, 116),
                glucoseList = listOf(116, 122, 120, 118, 116, 112, 114, 114, 116, 118),
                hrList = listOf(92, 94, 92, 90, 91, 88, 86, 90, 90, 92)
            )
        )
    )

    // 3번환자
    add(
        UserData(
            age = 63,
            gender = "male",
            medication = listOf("htn"),
            bloodPressureSystolic = 148,
            bloodPressureDiastolic = 86,
            glucose = 99,
            heartRate = 65,
            bodyTemperature = 36.9f,
            height = 185f,
            weight = 94f,
            bodyMassIndex = 27.5f,
            name = "환자3 - 고혈압",
            history = UserHistoryData(
                bpList = listOf(148, 150, 149, 160, 144, 146, 156, 149, 144, 148),
                glucoseList = listOf(99, 98, 99, 95, 96, 98, 99, 95, 96, 94),
                hrList = listOf(65, 68, 67, 65, 66, 67, 68, 66, 65, 68)
            )
        )
    )

    // 4번환자
    add(
        UserData(
            age = 68,
            gender = "female",
            medication = listOf("htn"),
            bloodPressureSystolic = 141,
            bloodPressureDiastolic = 73,
            glucose = 95,
            heartRate = 85,
            bodyTemperature = 36.6f,
            height = 157f,
            weight = 58f,
            bodyMassIndex = 23.5f,
            name = "환자4 - 정상인/고혈압",
            history = UserHistoryData(
                bpList = listOf(141, 138, 136, 140, 142, 144, 141, 139, 140, 138),
                glucoseList = listOf(95, 92, 90, 93, 91, 93, 92, 91, 90, 92),
                hrList = listOf(85, 82, 83, 86, 88, 88, 87, 85, 86, 84)
            )
        )
    )

    // 5번환자
    add(
        UserData(
            age = 66,
            gender = "male",
            medication = listOf("htn", "dm"),
            bloodPressureSystolic = 150,
            bloodPressureDiastolic = 96,
            glucose = 170,
            heartRate = 72,
            bodyTemperature = 36.5f,
            height = 175f,
            weight = 78f,
            bodyMassIndex = 25.5f,
            name = "환자5 - 당뇨/고혈압",
            history = UserHistoryData(
                bpList = listOf(150, 148, 144, 156, 145, 146, 146, 146, 162, 149),
                glucoseList = listOf(170, 160, 158, 155, 154, 156, 160, 168, 176, 165),
                hrList = listOf(72, 71, 70, 68, 70, 69, 68, 69, 68, 70)
            )
        )
    )
}