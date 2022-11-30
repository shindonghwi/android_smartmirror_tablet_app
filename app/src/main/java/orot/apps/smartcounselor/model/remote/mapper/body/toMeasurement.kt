package orot.apps.smartcounselor.model.remote.mapper.body

import orot.apps.smartcounselor.model.remote.BodyInfo
import orot.apps.smartcounselor.model.remote.MeasurementInfo

fun BodyInfo.toMeasurement(before: BodyInfo?, measurementInfo: MeasurementInfo?) = BodyInfo(
    before = before,
    measurement = measurementInfo,
)