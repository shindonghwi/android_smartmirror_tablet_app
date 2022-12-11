package orot.apps.smartcounselor.model.local

interface IWatch {
    fun isDataExist(): Boolean
}

interface IChair {
    fun isDataExist(): Boolean
}

data class WatchData(
    val bloodPressureSystolic: Int,// 혈압
    val heartRate: Int,// 맥박
) : IWatch {
    override fun isDataExist(): Boolean {
        return heartRate + bloodPressureSystolic != 0
    }
}

data class ChairData(
    val bloodPressureSystolic: Int, // 혈압
    val glucose: Int, // 혈당
    val weight: Float, // 체중
    val bodyMassIndex: Float, // 체지방
) : IChair {
    override fun isDataExist(): Boolean {
        return bloodPressureSystolic + glucose + weight + bodyMassIndex != 0f
    }
}
