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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.presentation.components.common.CustomTextField
import orot.apps.smartcounselor.presentation.components.common.ITextCallback
import orot.apps.sognora_viewmodel_extension.getViewModel

@Composable
fun AgeTextField(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    val configuration = LocalConfiguration.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    var hasFocus by remember { mutableStateOf(false) }
//    val keyboardController = LocalSoftwareKeyboardController.current

//    LaunchedEffect(key1 = Unit) {
//        coroutineScopeOnDefault {
//            delay(1000)
//            coroutineScopeOnMain {
//                if (!hasFocus) {
//                    focusRequester.requestFocus()
//                }
//            }
//        }
//    }

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
//            keyboardController?.hide()
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
