package mago.apps.sognorawebsocket.websocket.model.protocol.header

import android.os.Build

data class HeaderInfo(
    val protocol_id: String? = null,
    val protocol_version: String = "1.0",
    val timestamp: Long = System.currentTimeMillis() / 1000,
    val device: String = "Mirror",
    val model: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val height: Float? = null
) : IHeaderInfo {
    override fun toStream(type: String, age: Int, gender: String, height: Float): HeaderInfo {
        return HeaderInfo(
            protocol_id = type,
            protocol_version = this.protocol_version,
            timestamp = this.timestamp,
            device = "Mirror",
            model = Build.MODEL,
            age = age,
            gender = gender,
            height = height,
        )
    }
}