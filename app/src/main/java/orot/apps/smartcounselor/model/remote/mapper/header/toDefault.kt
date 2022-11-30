package orot.apps.smartcounselor.model.remote.mapper.header

import orot.apps.smartcounselor.model.remote.HeaderInfo
import orot.apps.smartcounselor.model.remote.MAGO_PROTOCOL

fun HeaderInfo.toDefault(type: MAGO_PROTOCOL) = HeaderInfo(
        protocol_id = type.id,
        protocol_version = "v1.0",
        timestamp = this.timestamp,
    )