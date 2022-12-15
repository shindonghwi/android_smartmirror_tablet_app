package mago.apps.sognorawebsocket.websocket.model.protocol.header

interface IHeaderInfo {
    fun toStream(type: String, age: Int, gender: String, height: Float): HeaderInfo
}