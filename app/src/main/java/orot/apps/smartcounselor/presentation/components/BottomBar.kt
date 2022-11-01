package orot.apps.smartcounselor.presentation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.*
import orot.apps.smartcounselor.presentation.guide.GuideViewModel
import orot.apps.sognora_compose_extension.animation.clickBounce
import orot.apps.sognora_compose_extension.components.RotationAnimation
import orot.apps.sognora_compose_extension.components.WavesAnimation
import orot.apps.sognora_viewmodel_extension.getViewModel
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnDefault
import orot.apps.sognora_viewmodel_extension.scope.coroutineScopeOnMain

@Composable
fun MagoBottomBar(
    mainViewModel: MainViewModel = getViewModel(hiltViewModel())
) {
    val configuration = LocalConfiguration.current
    val maxHeight = configuration.screenHeightDp * 0.2f

    mainViewModel.currentBottomMenu.value.let { route ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .height(maxHeight.dp),
        ) {
            when (route) {
                BottomMenu.Start.type -> {
                    StartBottomBar()
                }
                BottomMenu.Loading.type -> {
                    LoadingBottomBar()
                }
                BottomMenu.Empty.type -> {

                }
                BottomMenu.Conversation.type -> {
                    VDivider()
                    ConversationBottomBar()
                }
                BottomMenu.BloodPressure.type -> {
                    BloodPressureBottomBar()
                }
                BottomMenu.Retry.type -> {
                    VDivider()
                    RetryBottomBar()
                }
                BottomMenu.RetryAndChat.type -> {
                    VDivider()
                    RetryAndChatBottomBar()
                }
                BottomMenu.Call.type -> {
                    CallBottomBar()
                }
            }
        }
    }
}

@Composable
private fun VDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp), color = GrayDivider
    )
}


/** 홈(시작하기) 화면 바텀 바 */
@Composable
private fun StartBottomBar(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.35f }


    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AgeTextField()
                Spacer(modifier = Modifier.height(8.dp))
                SexRadioButton()
            }

            Text(modifier = Modifier
                .width(startWidth)
                .clickBounce {
                    takeIf { mainViewModel.userAge != 0 }?.run {
                        navigationKit.clearAndMove(Screens.Guide.route) {
                            mainViewModel.updateBottomMenu(BottomMenu.Loading)
                        }
                    } ?: run {
                        Toast
                            .makeText(context, "나이를 입력해주세요", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
                .background(Primary)
                .padding(vertical = 36.dp),
                textAlign = TextAlign.Center,
                text = "시작",
                style = MaterialTheme.typography.Display1,
                color = White)

        }
    }
}

@Composable
fun SexRadioButton(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    val configuration = LocalConfiguration.current
    val radioOptions = listOf("남", "여")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Row(
        modifier = Modifier
            .width((configuration.screenWidthDp * 0.3).dp)
            .background(Gray80),
        verticalAlignment = Alignment.CenterVertically
    ) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            mainViewModel.userSex = text == "남"
                        }
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) },
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = Gray20,
                        selectedColor = Primary
                    )
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = text,
                    style = MaterialTheme.typography.body1,
                    color = White,
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun AgeTextField(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    val configuration = LocalConfiguration.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var text by remember { mutableStateOf("") }
    var hasFocus by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = Unit) {
        coroutineScopeOnDefault {
            delay(1000)
            coroutineScopeOnMain {
                if (!hasFocus) {
                    focusRequester.requestFocus()
                }
            }
        }
    }

    OutlinedTextField(
        modifier = Modifier
            .width((configuration.screenWidthDp * 0.3).dp)
            .focusRequester(focusRequester)
            .onFocusChanged { hasFocus = it.hasFocus },
        value = text,
        onValueChange = { content ->
            content
                .takeIf {
                    content.length < 3
                }
                ?.apply {
                    content
                        .replace("[^0-9]".toRegex(), "")
                        .takeIf { it.isNotEmpty() }
                        ?.apply {
                            mainViewModel.userAge = this.toInt()
                        }
                        ?: apply {
                            mainViewModel.userAge = 0
                        }
                    text = content
                }
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            keyboardController?.hide()
        }),
        label = {
            Text(
                text = if (hasFocus || text.isNotEmpty()) {
                    "당신의 나이는?"
                } else {
                    "나이를 입력해주세요"
                },
                color = Gray30,
                style = MaterialTheme.typography.caption
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = Gray80,
            backgroundColor = Gray80,
            textColor = White
        ),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
    )
}

/** 로딩 바텀 바 */
@Composable
private fun LoadingBottomBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        LoadingText()
    }
}

@Composable
fun LoadingText(
    defaultContent: String = "Loading",
    textStyle: TextStyle = MaterialTheme.typography.body1,
    guideViewModel: GuideViewModel = hiltViewModel()
) {
    guideViewModel.currentRenderText.collectAsState().value.let { dot ->
        Text(
            "${defaultContent}$dot",
            modifier = Modifier.padding(top = 16.dp),
            style = textStyle,
            color = Gray20,
            textAlign = TextAlign.Center
        )
    }
}

/** 권고멘트 바텀 바 */
@Composable
private fun RetryAndChatBottomBar() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("다시하기",
            modifier = Modifier
                .clickBounce {

                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFCFFFCF))
                .padding(vertical = 35.dp, horizontal = 75.dp),
            style = MaterialTheme.typography.Display2,
            color = Color.Black,
            textAlign = TextAlign.Center)
        Text("대화내역",
            modifier = Modifier
                .clickBounce {

                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFFFCFCF))
                .padding(vertical = 35.dp, horizontal = 75.dp),
            style = MaterialTheme.typography.Display2,
            color = Color.Black,
            textAlign = TextAlign.Center)
    }
}

/** 혈압측정 바텀 바 */
@Composable
private fun BloodPressureBottomBar() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "혈압측정 결과는",
            style = MaterialTheme.typography.h2,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            "126",
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(vertical = 8.dp, horizontal = 12.dp),
            style = MaterialTheme.typography.h1,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Text(
            "입니다",
            style = MaterialTheme.typography.h2,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

/** 다시하기 */
@Composable
fun RetryBottomBar(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("다시하기",
            modifier = Modifier
                .clickBounce {
                    navigationKit.clearAndMove(Screens.Home.route) {
                        mainViewModel.updateBottomMenu(BottomMenu.Empty)
                    }
                }
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFCFFFCF))
                .padding(vertical = 35.dp, horizontal = 75.dp),
            style = MaterialTheme.typography.Display2,
            color = Color.Black,
            textAlign = TextAlign.Center)
    }
}

/** 상담원 전화 걸려올때 */
@Composable
fun CallBottomBar() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.padding(all = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "상담원",
                modifier = Modifier.padding(vertical = 35.dp),
                style = MaterialTheme.typography.h3,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            WavesAnimation {
                Icon(
                    painter = painterResource(id = R.drawable.call),
                    "call",
                    tint = Color.White,
                )
            }
        }
    }
}

/** 상담원 전화 걸려올때 */
@Composable
fun ConversationBottomBar(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
    val isPlaying = remember { mainViewModel.micIsAvailable }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RotationAnimation(
            modifier = Modifier.padding(start = 60.dp),
            isPlaying = isPlaying,
            iconDrawable = R.drawable.mago_logo_icon,
            iconSize = 80.dp
        )

//        AnimationText(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 50.dp),
//            initDelay = 1000,
//            enterTransition = fadeIn(),
//            isEnded = isEnded,
//            exitTransition = fadeOut()
//        ) {
//            Text(
//                "",
//                color = Color.White,
//                style = MaterialTheme.typography.Display3.copy(textAlign = TextAlign.Center)
//            )
//        }
    }
}
