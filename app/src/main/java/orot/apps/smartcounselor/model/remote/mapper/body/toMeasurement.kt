package orot.apps.smartcounselor.model.remote.mapper.body

import orot.apps.smartcounselor.model.remote.BodyInfo

fun BodyInfo.toMeasurement() = BodyInfo(
    before = this.before,
    measurement = this.measurement,
)