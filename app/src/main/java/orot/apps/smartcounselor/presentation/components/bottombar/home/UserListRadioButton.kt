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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.model.remote.UserData
import orot.apps.smartcounselor.model.remote.userList
import orot.apps.smartcounselor.presentation.style.Gray20
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity

@Composable
fun UserRadioButton(modifier: Modifier) {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val radioOptions = userList
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(UserData()) }

    Column {
        radioOptions.chunked(if (radioOptions.size > 3) 3 else 3)
            .forEachIndexed { rowIndex, rowList ->
                Row(
                    modifier = modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    rowList.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .selectable(selected = (item == selectedOption), onClick = {
                                    mainViewModel.selectedUser = item
                                    onOptionSelected(item)
                                }),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(modifier = Modifier.size(26.dp)) {
                                RadioButton(
                                    selected = (item == selectedOption),
                                    onClick = {
                                        onOptionSelected(item)
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        unselectedColor = Gray20, selectedColor = Primary
                                    )
                                )
                            }

                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.body1,
                                color = White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
    }
}