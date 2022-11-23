package orot.apps.smartcounselor.presentation.ui.screens.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.style.Gray20
import orot.apps.smartcounselor.presentation.style.Primary

@Composable
fun SexRadioButton() {

//    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel
//    val configuration = LocalConfiguration.current
//    val radioOptions = listOf("남", "여")
//    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
//
//    Row(
//        modifier = Modifier
//            .width((configuration.screenWidthDp * 0.3).dp)
//            .border(1.dp, White, RoundedCornerShape(16.dp)),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        radioOptions.forEach { text ->
//            Row(
//                Modifier
//                    .selectable(
//                        selected = (text == selectedOption),
//                        onClick = {
//                            onOptionSelected(text)
//                            mainViewModel.userSex = text == "남"
//                        }
//                    )
//                    .padding(horizontal = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                RadioButton(
//                    selected = (text == selectedOption),
//                    onClick = { onOptionSelected(text) },
//                    colors = RadioButtonDefaults.colors(
//                        unselectedColor = Gray20,
//                        selectedColor = Primary
//                    )
//                )
//                Text(
//                    modifier = Modifier.padding(start = 16.dp),
//                    text = text,
//                    style = MaterialTheme.typography.body1,
//                    color = White,
//                )
//            }
//        }
//    }
}
