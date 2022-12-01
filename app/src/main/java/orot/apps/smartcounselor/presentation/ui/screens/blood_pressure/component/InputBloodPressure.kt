package orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.component

import android.text.TextUtils
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.model.remote.UserInputData
import orot.apps.smartcounselor.presentation.components.input.CustomTextField
import orot.apps.smartcounselor.presentation.components.input.ITextCallback
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.MainViewModel

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
            val defaultText: String = getDefaultText(mainViewModel.userInputData, index)

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
                        imeAction = if (item.first.contains("체질량 지수")) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    textLimit = when (index) {
                        0 -> Int.MAX_VALUE
                        1, 2, 3, 4 -> 3
                        5, 6, 7, 8 -> 5
                        else -> Int.MAX_VALUE
                    },
                    placeholderText = item.second,
                    contentAlignment = Alignment.Center,
                    iTextCallback = object : ITextCallback {
                        override fun renderText(content: String) {

                            var intTextNum: Int? = null
                            var floatTextNum: Float? = null

                            when (index) {
                                1, 2, 3, 4 -> { // int
                                    content.replace("[^0-9]".toRegex(), "")
                                        .takeIf { it.isNotEmpty() }?.apply {
                                            intTextNum = this.toInt()
                                        } ?: apply {
                                        intTextNum = null
                                    }
                                }
                                5, 6, 7, 8 -> { // float
                                    content.replace("^[0-9]{1,2}(\\.[0-9]{1,2})?$".toRegex(), "")
                                        .takeIf { it.isNotEmpty() }?.apply {
                                            floatTextNum = this.toFloat()
                                        } ?: apply {
                                        floatTextNum = null
                                    }
                                }
                                else -> {}
                            }

                            mainViewModel.userInputData?.let {
                                when (index) {
                                    0 -> {
                                        it.medication = content.split(",")
                                    }
                                    1 -> {
                                        it.bloodPressureSystolic = intTextNum
                                    }
                                    2 -> {
                                        it.bloodPressureDiastolic = intTextNum
                                    }
                                    3 -> {
                                        it.glucose = intTextNum
                                    }
                                    4 -> {
                                        it.heartRate = intTextNum
                                    }
                                    5 -> {
                                        it.bodyTemperature = floatTextNum
                                    }
                                    6 -> {
                                        it.height = floatTextNum
                                    }
                                    7 -> {
                                        it.weight = floatTextNum
                                    }
                                    8 -> {
                                        it.bodyMassIndex = floatTextNum
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
}

private fun getDefaultText(userInputData: UserInputData?, index: Int): String {
    return when (index) {
        0 -> userInputData?.medication?.let { it1 -> TextUtils.join(",", it1) }.toString()
        3 -> userInputData?.glucose.toString()
        5 -> userInputData?.bodyTemperature.toString()
        6 -> userInputData?.height.toString()
        7 -> userInputData?.weight.toString()
        8 -> userInputData?.bodyMassIndex.toString()
        else -> ""
    }
}