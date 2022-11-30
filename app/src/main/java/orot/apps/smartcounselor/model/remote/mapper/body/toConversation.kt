package orot.apps.smartcounselor.model.remote.mapper.body

import orot.apps.smartcounselor.model.remote.BodyInfo

fun BodyInfo.toConversation() = BodyInfo(
    action = this.action,
    turn = this.turn,
    voice = this.voice,
    display = this.display,
)