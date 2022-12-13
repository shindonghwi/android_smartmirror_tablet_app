package mago.apps.sognorawebsocket.websocket.model.protocol.body

interface IMeasurementInfo {
    fun toMeasurement(beforeBody: BodyInfo?, measurement: RequestedMeasurementInfo?): BodyInfo
}