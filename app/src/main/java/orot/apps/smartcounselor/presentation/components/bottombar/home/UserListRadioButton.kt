package orot.apps.smartcounselor.presentation.components.bottombar.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.presentation.style.Gray20
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity

@Composable
fun UserRadioButton(modifier: Modifier) {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val radioOptions = mainViewModel.userListInfo
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Log.d("Asdasd", "UserRadioButton: ${radioOptions.chunked(3)}")

    Column {
        radioOptions.chunked(if (radioOptions.size > 5) 3 else 5)
            .forEachIndexed { rowIndex, rowList ->
                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    rowList.forEachIndexed { index, text ->
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .selectable(selected = (text == selectedOption), onClick = {
                                    onOptionSelected(text)
                                }),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(modifier = Modifier.size(26.dp)) {
                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) },
                                    colors = RadioButtonDefaults.colors(
                                        unselectedColor = Gray20, selectedColor = Primary
                                    )
                                )
                            }
                            Text(
                                text = text,
                                style = MaterialTheme.typography.body1,
                                color = White,
                            )
                        }
                    }
                }
            }
    }
}