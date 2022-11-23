package orot.apps.smartcounselor.presentation.ui.screens.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import orot.apps.smartcounselor.presentation.components.input.CustomTextField
import orot.apps.smartcounselor.presentation.components.input.ITextCallback
import orot.apps.smartcounselor.presentation.ui.MagoActivity

@Composable
fun AgeTextField() {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val configuration = LocalConfiguration.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    CustomTextField(
        modifier = Modifier
            .widthIn(min = 130.dp)
            .width((configuration.screenWidthDp * 0.2).dp)
            .height(50.dp)
            .border(1.dp, White, RoundedCornerShape(16.dp))
            .focusRequester(focusRequester),
        textLimit = 2,
        textStyle = MaterialTheme.typography.h3.copy(
            color = White,
            textAlign = TextAlign.Center
        ),
        contentAlignment = Alignment.Center,
        placeholderText = "나이",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
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
