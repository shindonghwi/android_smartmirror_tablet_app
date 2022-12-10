package orot.apps.smartcounselor.presentation.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

interface ITextCallback {
    fun renderText(content: String)
}

interface IFocusCallback {
    fun onFocus(isFocus: Boolean)
}

data class KeyBoardActionUnit(
    val onDone: (() -> Unit)? = null,
    val onGo: (() -> Unit)? = null,
    val onNext: (() -> Unit)? = null,
    val onPrevious: (() -> Unit)? = null,
    val onSearch: (() -> Unit)? = null,
    val onSend: (() -> Unit)? = null,
)

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    leadingPaddingValues: PaddingValues = PaddingValues(0.dp),
    trailingIcon: (@Composable () -> Unit)? = null,
    trailingPaddingValues: PaddingValues = PaddingValues(0.dp),
    innerTextPaddingValues: PaddingValues = PaddingValues(horizontal = 0.dp),
    defaultText: String = "",
    placeholderText: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.h3,
    isSingleLine: Boolean = true,
    textLimit: Int = Int.MAX_VALUE,
    textAlignment: Alignment = Alignment.Center,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    keyBoardActionUnit: KeyBoardActionUnit? = null,
    actionDoneAfterClearText: Boolean = false,
    enable: Boolean = true,
    iTextCallback: ITextCallback? = null
) {
    var text by rememberSaveable { mutableStateOf(defaultText) }

    Column(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                if (it.length <= textLimit) {
                    text = it
                    iTextCallback?.renderText(it)
                }
            },
            singleLine = isSingleLine,
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = {
                    keyBoardActionUnit?.onDone?.let {
                        if (actionDoneAfterClearText) {
                            text = ""
                        }
                        it()
                    }
                },
                onGo = { keyBoardActionUnit?.onGo?.let { it() } },
                onNext = { keyBoardActionUnit?.onNext?.let { it() } },
                onPrevious = { keyBoardActionUnit?.onPrevious?.let { it() } },
                onSearch = { keyBoardActionUnit?.onSearch?.let { it() } },
                onSend = { keyBoardActionUnit?.onSend?.let { it() } },
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.padding(leadingPaddingValues)) {
                            if (leadingIcon != null) leadingIcon()
                        }
                        Box(
                            Modifier
                                .weight(1f)
                                .padding(innerTextPaddingValues),
                            contentAlignment = textAlignment
                        ) {
                            if (text.isEmpty()) {
                                placeholderText?.let { it() }
                            }
                            innerTextField()
                        }
                        Box(Modifier.padding(trailingPaddingValues)) {
                            if (trailingIcon != null) trailingIcon()
                        }
                    }
                }
            },
            enabled = enable
        )
    }
}