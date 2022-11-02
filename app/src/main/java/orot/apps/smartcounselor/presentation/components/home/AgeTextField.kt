package orot.apps.smartcounselor.presentation.components.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.MagoActivity
import orot.apps.smartcounselor.presentation.components.common.CustomTextField
import orot.apps.smartcounselor.presentation.components.common.ITextCallback

@Composable
fun AgeTextField() {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel
    val configuration = LocalConfiguration.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    CustomTextField(
        modifier = Modifier
            .width((configuration.screenWidthDp * 0.2).dp)
            .height(50.dp)
            .border(1.dp, White, RoundedCornerShape(16.dp))
//            .onFocusChanged { hasFocus = it.hasFocus }
            .focusRequester(focusRequester),
        textLimit = 2,
        textStyle = MaterialTheme.typography.h3.copy(
            color = White,
            textAlign = TextAlign.Center
        ),
        contentAlignment = Alignment.Center,
        placeholderText = "나이",
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        iTextCallback = object : ITextCallback {
            override fun renderText(content: String) {
                content.replace("[^0-9]".toRegex(), "").takeIf { it.isNotEmpty() }?.apply {
                    mainViewModel.userAge = this.toInt()
                } ?: apply {
                    mainViewModel.userAge = 0
                }
            }
        }
    )
}
