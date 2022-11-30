package orot.apps.smartcounselor.model.remote.mapper.body

import orot.apps.smartcounselor.model.remote.BodyInfo

fun BodyInfo.toError() = BodyInfo(
    code = this.code,
    message = this.message,
    reason = this.reason,
)