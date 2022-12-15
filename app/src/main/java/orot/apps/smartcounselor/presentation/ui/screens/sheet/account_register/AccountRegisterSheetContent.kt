package orot.apps.smartcounselor.presentation.ui.screens.sheet.account_register

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import orot.apps.smartcounselor.presentation.components.input.CustomTextField
import orot.apps.smartcounselor.presentation.components.input.ITextCallback
import orot.apps.smartcounselor.presentation.components.input.KeyBoardActionUnit
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.style.Gray20
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.utils.modifier.noDuplicationClickable

@Composable
fun AccountRegisterSheetContent() {
    val config = LocalConfiguration.current
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val isShowing = mainViewModel.isShowingAccountBottomSheet.collectAsState().value

    val contentList: List<Triple<String, String, String?>> = listOf(
        Triple("귀하의 성함은 무엇인가요?", "성함", "ex) 홍길동"),
        Triple("귀하의 나이를 입력해주세요", "나이", "ex) 53"),
        Triple("귀하의 신장을 입력해주세요 (cm 기준)", "신장", "ex) 183"),
        Triple("귀하의 성별을 선택해주세요", "성별", null),
        Triple("현재 복용하시는 약은 무엇인가요?", "약", null),
    )

    AnimatedVisibility(
        visible = isShowing,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(config.screenHeightDp.dp * 0.97f)
                .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .background(Color.White)
                .padding(all = 40.dp)
        ) {
            DescriptionContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            )

            contentList.forEachIndexed { index, item ->
                val content = item.first
                val keyword = item.second
                val hint = item.third
                InputContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (index == 0) 50.dp else 30.dp),
                    content = content,
                    key = keyword,
                    hint = hint
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            RegisterButton()
        }
    }

    BackHandler(enabled = isShowing) {
        mainViewModel.changeAccountBottomSheetFlag(false)
    }
}

@Composable
private fun DescriptionContent(modifier: Modifier) {
    Text(
        text = "사용자님,\n스마트 홈과 귀하를 연결해드릴게요",
        style = MaterialTheme.typography.Display3,
        color = MaterialTheme.colors.primary,
    )

    Column(
        modifier = modifier,
    ) {
        Text(buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            ) {
                append("아래의 정보를 입력해주세요.")
            }
        })
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                ) {
                    append("이 과정은 등록을 위해 처음 ")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                ) {
                    append("한 번만 진행")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                ) {
                    append("됩니다")
                }
            })
        }
    }
}

@Composable
private fun InputContent(modifier: Modifier, content: String, key: String, hint: String?) {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val localFocusManager = LocalFocusManager.current

    var keywordPre = ""
    var keyword = ""
    var keywordAfter = ""

    val findIndex = content.indexOf(string = key)

    content.mapIndexed { index, item ->
        if (index < findIndex) {
            keywordPre += item
        } else if (index < findIndex + key.length) {
            keyword = key
        } else {
            keywordAfter += item
        }
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Black80, fontWeight = FontWeight.Normal, fontSize = 14.sp
                )
            ) {
                append(keywordPre)
            }
            withStyle(
                style = SpanStyle(
                    color = Black80, fontWeight = FontWeight.Bold, fontSize = 14.sp
                )
            ) {
                append(keyword)
            }
            withStyle(
                style = SpanStyle(
                    color = Black80, fontWeight = FontWeight.Normal, fontSize = 14.sp
                )
            ) {
                append(keywordAfter)
            }
        })
    }

    hint?.let {
        CustomTextField(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(16.dp)
                ),
            innerTextPaddingValues = PaddingValues(start = 20.dp),
            trailingPaddingValues = PaddingValues(end = 8.dp),
            placeholderText = {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1,
                    color = Black80.copy(0.4f),
                    textAlign = TextAlign.Center
                )
            },
            textStyle = MaterialTheme.typography.body1.copy(color = Black80),
            textAlignment = Alignment.CenterStart,
            isSingleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = when (key) {
                    "나이", "신장" -> {
                        KeyboardType.Number
                    }
                    else -> {
                        KeyboardType.Text
                    }
                },
                imeAction = when (key) {
                    "성함", "나이" -> {
                        ImeAction.Next
                    }
                    else -> {
                        ImeAction.Done
                    }
                },
            ),
            keyBoardActionUnit = KeyBoardActionUnit(
                onNext = {
                    localFocusManager.moveFocus(FocusDirection.Down)
                },
                onDone = {
                    localFocusManager.clearFocus()
                }
            ), iTextCallback = object : ITextCallback {
                override fun renderText(content: String) {
//                    if (key == "성함") {
//                        mainViewModel.addAccountInputData = content
//                    }
                }
            }
        )
    } ?: run {
        when (key) {
            "약" -> {
                MedicineSelector(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(30.dp)
                )
            }
            "성별" -> {
                GenderSelector(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(30.dp)
                )
            }
        }
    }
}

@Composable
private fun GenderSelector(modifier: Modifier) {
    val radioOptions = listOf("남", "여")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        radioOptions.forEach { text ->
            Row(
                modifier = Modifier.selectable(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                    },
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) },
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = Gray20, selectedColor = Primary
                    )
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1,
                    color = Black80,
                )
            }
        }
    }
}


@Composable
private fun MedicineSelector(modifier: Modifier) {
    val radioOptions = listOf("고혈압", "당뇨", "고혈압 + 당뇨", "해당사항 없음")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions.last()) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        radioOptions.forEach { text ->
            Row(
                modifier = Modifier.selectable(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                    },
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) },
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = Gray20, selectedColor = Primary
                    )
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1,
                    color = Black80,
                )
            }
        }
    }
}

@Composable
private fun RegisterButton() {

    val context = LocalContext.current
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.3f }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(startWidth)
                .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                .background(Primary)
                .noDuplicationClickable {
                    mainViewModel.run {
//                        if (addAccountInputData.isNotEmpty()) {
//                            addUser(addAccountInputData)
//                            changeAccountBottomSheetFlag(false)
//                            Toast
//                                .makeText(context, "등록이 완료되었습니다", Toast.LENGTH_SHORT)
//                                .show()
//                        } else {
//                            Toast
//                                .makeText(context, "정보를 입력해주세요", Toast.LENGTH_SHORT)
//                                .show()
//                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = 18.dp, horizontal = 30.dp),
                text = "등록하기",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                color = Color.White
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(startWidth)
                .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                .background(Color(0xFFD9D9D9))
                .noDuplicationClickable {
                    Toast.makeText(context, "등록을 취소하였습니다", Toast.LENGTH_SHORT).show()
                    mainViewModel.changeAccountBottomSheetFlag(false)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = 18.dp, horizontal = 30.dp),
                text = "취소하기",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                color = Black80
            )
        }
    }
}