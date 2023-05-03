package orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.component

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.JsonObject
import mago.apps.sognorawebsocket.websocket.model.protocol.body.DisplayInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import orot.apps.smartcounselor.BuildConfig
import orot.apps.smartcounselor.api.retrofitClient
import orot.apps.smartcounselor.model.local.BuildShowMode
import orot.apps.smartcounselor.model.remote.SmhResponseData
import orot.apps.smartcounselor.model.remote.SmhUserInputData
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.utils.modifier.clickBounce
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnDefault
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun BloodPressureSubmitButton(modifier: Modifier = Modifier) {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.5f }
    Box {
        Text(
            modifier = modifier
                .padding(top = 18.dp)
                .width(startWidth)
                .clickBounce {
                    val isValueEmpty = mainViewModel.selectedUser?.isEmptyCheck()

                    if (isValueEmpty == true) {
                        Toast
                            .makeText(context, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        mainViewModel.run {
                            if (BuildConfig.SHOW_MODE == BuildShowMode.RECOMMENDATION.value) {
                                selectedUser?.let {
                                    val data = SmhUserInputData(
                                        user = SmhUserInputData.UserInfo(
                                            id = it.name, age = it.age, gender = it.gender
                                        ),
                                        measurement = SmhUserInputData.Measurement(
                                            medication = listOf(""),
                                            bloodPressureSystolic = it.bloodPressureSystolic,
                                            bloodPressureDiastolic = it.bloodPressureDiastolic,
                                            glucose = it.glucose,
                                            bodyTemperature = it.bodyTemperature,
                                            heartRate = it.heartRate,
                                            height = it.height,
                                            weight = it.weight,
                                        ),
                                    )

                                    Log.w("Asdsad", "request Data: ${Gson().toJson(data)}", )

                                    val mediaType = "application/json".toMediaTypeOrNull()
                                    val requestBody = Gson()
                                        .toJson(data)
                                        .toRequestBody(mediaType)

                                    try {
                                        coroutineScopeOnDefault {
                                            val response =
                                                retrofitClient.apiService.postRecommendation(
                                                    requestBody
                                                )
                                            Log.w("Asdsad", "response Data: ${Gson().toJson(response.body())}", )

                                            var isSuccess =
                                                response.isSuccessful || response.code() == 200

                                            if (isSuccess) {
                                                val body = response.body()
                                                isSuccess =
                                                    !(body?.data == null || body.recommendation == null)
                                                if (isSuccess) {
                                                    body?.let { res ->
                                                        mainViewModel.let { vm ->
                                                            vm.displayInfo = DisplayInfo(
                                                                id = selectedUser?.name ?: "",
                                                                medication = res.recommendation?.medication,
                                                                measurement = res.recommendation?.measurement,
                                                                recommendation = res.recommendation?.recommendation,
                                                                today_recommendation = res.recommendation?.today_recommendation,
                                                            )
                                                            vm.riskPredictionInfo =
                                                                res.risk_prediction
                                                        }
                                                        changeRecommendationBottomSheetFlag(true)
                                                    }
                                                } else {
                                                    showErrorToast(context)
                                                }
                                            } else {
                                                showErrorToast(context)
                                            }
                                        }
                                    } catch (e: Exception) {
                                        showErrorToast(context, "서버 연결 실패. 재 시도 해주세요")
                                    }
                                }
                            } else {
                                startWaitingResult()
                            }
                        }
                    }
                }
                .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
                .background(Primary)
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            text = "제출",
            style = MaterialTheme.typography.Display3,
            color = Color.White,
        )
    }
}

private fun showErrorToast(context: Context, msg: String = "권고멘트를 불러 올 수 없습니다."){
    coroutineScopeOnMain {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}