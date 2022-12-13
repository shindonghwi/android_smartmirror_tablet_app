package mago.apps.sognorawebsocket.websocket.model.protocol

import mago.apps.sognorawebsocket.websocket.model.protocol.body.BodyInfo
import mago.apps.sognorawebsocket.websocket.model.protocol.header.HeaderInfo

data class MessageProtocol(
    val header: HeaderInfo,
    val body: BodyInfo? = null,
)
