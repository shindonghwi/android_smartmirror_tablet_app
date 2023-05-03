package orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.component

import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.BuildConfig
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.model.local.BuildShowMode
import orot.apps.smartcounselor.model.remote.UserData
import orot.apps.smartcounselor.presentation.components.input.CustomTextField
import orot.apps.smartcounselor.presentation.components.input.ITextCallback
import orot.apps.smartcounselor.presentation.components.input.KeyBoardActionUnit
import orot.apps.smartcounselor.presentation.ui.MagoActivity

@Composable
fun InputBloodPressure(modifier: Modifier) {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val contentList = listOf(
        Pair("약물복용력: ", "htn ( ,로 구분 )"),
        Pair("수축기 혈압[mmHg]: ", "140"),
        Pair("이완기 혈압[mmHg]: ", "90"),
        Pair("공복 혈당[mg/DL]: ", "105"),
        Pair("맥박[HR/min]: ", "70"),
        Pair("체온[℃]: ", "36.5"),
        Pair("키[cm]: ", "182"),
        Pair("체중[kg]: ", "92"),
        Pair("체질량 지수[몸무게(kg) / 키 (cm^2)]: ", "92"),
    )

    LazyColumn(modifier = modifier) {

        itemsIndexed(items = contentList, key = { index, item -> index }) { index, item ->
            val defaultText: String = getDefaultText(mainViewModel.selectedUser, index)

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(0.8f),
                    text = item.first,
                    style = MaterialTheme.typography.h3,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 시계에서 수집되는 데이터
                    if (index == 1 && mainViewModel.watchHashData["bloodPressureSystolic"] != 0 ||
                        index == 4 && mainViewModel.watchHashData["heartRate"] != 0
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 8.dp),
                            painter = painterResource(id = R.drawable.watch),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    // 의자에서 수집되는 데이터
                    if (BuildConfig.SHOW_MODE == BuildShowMode.FULL.value) {
                        if (index == 1 && mainViewModel.chairHashData["bloodPressureSystolic"] != 0 ||
                            index == 2 && mainViewModel.chairHashData["bloodPressureDiastolic"] != 0 ||
                            index == 3 && mainViewModel.chairHashData["glucose"] != 0 ||
                            index == 7 && mainViewModel.chairHashData["weight"] != 0 ||
                            index == 8 && mainViewModel.chairHashData["bodyMassIndex"] != 0
                        ) {

                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(end = 8.dp),
                                painter = painterResource(id = R.drawable.chair),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }

                CustomTextField(modifier = Modifier
                    .width(screenWidth.dp * 0.3f)
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                    textStyle = MaterialTheme.typography.h3.copy(
                        color = Color.White, textAlign = TextAlign.Center
                    ),
                    defaultText = defaultText,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = when (index) {
                            0 -> KeyboardType.Text
                            else -> KeyboardType.Number
                        },
                        imeAction = if (item.first.contains("체질량")) ImeAction.Done else ImeAction.Next
                    ),
                    keyBoardActionUnit = KeyBoardActionUnit(
                        onDone = { focusManager.clearFocus() },
                        onNext = { focusManager.moveFocus(FocusDirection.Next) },
                    ),
                    textLimit = when (index) {
                        0 -> Int.MAX_VALUE
                        1, 2, 3, 4 -> 3
                        5, 6, 7, 8 -> 5
                        else -> Int.MAX_VALUE
                    },
                    placeholderText = {
                        Text(
                            text = item.second,
                            color = Color.White.copy(0.8f),
                            textAlign = TextAlign.Center
                        )
                    },
                    iTextCallback = object : ITextCallback {
                        override fun renderText(content: String) {

                            var intTextNum = 0
                            var floatTextNum = 0f

                            when (index) {
                                1, 2, 3, 4 -> { // int
                                    content.replace("[^0-9]".toRegex(), "")
                                        .takeIf { it.isNotEmpty() }
                                        ?.apply { intTextNum = this.toInt() }
                                        ?: apply { intTextNum = 0 }
                                }
                                5, 6, 7, 8 -> { // float
                                    content.replace("[^0-9]{0,2}(\\.[^0-9]{0,2})?$".toRegex(), "")
                                        .takeIf { it.isNotEmpty() }
                                        ?.apply { floatTextNum = this.toFloat() }
                                        ?: apply { floatTextNum = 0f }
                                }
                                else -> {}
                            }

                            mainViewModel.selectedUser?.let {
                                when (index) {
                                    0 -> it.medication = content.split(",")
                                    1 -> it.bloodPressureSystolic = intTextNum
                                    2 -> it.bloodPressureDiastolic = intTextNum
                                    3 -> it.glucose = intTextNum
                                    4 -> it.heartRate = intTextNum
                                    5 -> it.bodyTemperature = floatTextNum
                                    6 -> it.height = floatTextNum
                                    7 -> it.weight = floatTextNum
                                    8 -> it.bodyMassIndex = floatTextNum
                                }
                            }
                        }
                    })
            }
        }
    }
}

private fun getDefaultText(userData: UserData?, index: Int): String {
    return when (index) {
        0 -> userData?.medication?.let { it1 -> TextUtils.join(",", it1) }.toString()
        1 -> userData?.bloodPressureSystolic?.takeIf { it != 0 }?.run { this.toString() }
            ?: run { "" }
        2 -> userData?.bloodPressureDiastolic?.takeIf { it != 0 }?.run { this.toString() }
            ?: run { "" }
        3 -> userData?.glucose?.takeIf { it != 0 }?.run { this.toString() } ?: run { "" }
        4 -> userData?.heartRate?.takeIf { it != 0 }?.run { this.toString() } ?: run { "" }
        5 -> userData?.bodyTemperature?.takeIf { it != 0f }?.run { this.toString() } ?: run { "" }
        6 -> userData?.height?.takeIf { it != 0f }?.run { this.toString() } ?: run { "" }
        7 -> userData?.weight?.takeIf { it != 0f }?.run { this.toString() } ?: run { "" }
        8 -> userData?.bodyMassIndex?.takeIf { it != 0f }?.run { this.toString() } ?: run { "" }
        else -> ""
    }
}