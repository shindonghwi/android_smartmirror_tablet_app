package orot.apps.smartcounselor.model.remote.mapper.header

import android.os.Build
import orot.apps.smartcounselor.model.remote.HeaderInfo
import orot.apps.smartcounselor.model.remote.MAGO_PROTOCOL

fun HeaderInfo.toStream(type: String, age: Int, gender: String) = HeaderInfo(
    protocol_id = type,
    protocol_version = this.protocol_version,
    timestamp = this.timestamp,
    device = Build.MODEL,
    age = age,
    gender = gender
)