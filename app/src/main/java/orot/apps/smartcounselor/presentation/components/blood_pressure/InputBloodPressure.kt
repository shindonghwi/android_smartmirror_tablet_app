package orot.apps.smartcounselor.presentation.components.blood_pressure

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.presentation.components.common.CustomTextField
import orot.apps.smartcounselor.presentation.components.common.ITextCallback
import orot.apps.sognora_viewmodel_extension.getViewModel

@Composable
fun InputBloodPressure(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val focusManager = LocalFocusManager.current

    val contentList = listOf(
        Pair("최고혈압: ", "120"),
        Pair("최저혈압: ", "80"),
        Pair("혈당량: ", "85"),
    )

    Column {
        repeat(contentList.size) { index ->
            val item = contentList[index]

            Row(
                modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.first,
                    style = MaterialTheme.typography.h3,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis
                )
                CustomTextField(modifier = Modifier
                    .padding(start = 20.dp)
                    .width((screenWidth * 0.15).dp)
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp),
                    textStyle = MaterialTheme.typography.h3.copy(
                        color = Color.White, textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (item.first.contains("혈당량")) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    }),
                    textLimit = 3,
                    placeholderText = item.second,
                    contentAlignment = Alignment.Center,
                    iTextCallback = object : ITextCallback {
                        override fun renderText(content: String) {
                            var innerTextNum = 0
                            content.replace("[^0-9]".toRegex(), "").takeIf { it.isNotEmpty() }?.apply {
                                innerTextNum = this.toInt()
                            } ?: apply {
                                innerTextNum = 0
                            }
                            when (index) {
                                0 -> {
                                    mainViewModel.bloodPressureMax = innerTextNum
                                }
                                1 -> {
                                    mainViewModel.bloodPressureMin = innerTextNum
                                }
                                2 -> {
                                    mainViewModel.bloodPressureSugar = innerTextNum
                                }
                            }
                        }
                    })
            }
        }
    }
}
