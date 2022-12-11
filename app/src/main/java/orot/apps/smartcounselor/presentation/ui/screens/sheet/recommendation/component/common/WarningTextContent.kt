package orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import orot.apps.smartcounselor.presentation.style.Black80

@Composable
fun WarningTextContent(modifier: Modifier, content: String, keyList: List<String> = listOf("주의", "위험")) {
    var keywordPre = ""
    var keyword = ""
    var keywordAfter = ""
    var findIndex = -1
    var findKey = "정상"

    for (key in keyList) {
        findIndex = content.indexOf(string = key)

        if (findIndex != -1) {
            findKey = key
            break
        }
    }

    if (findIndex != -1) {
        content.mapIndexed { index, item ->
            if (index < findIndex) {
                keywordPre += item
            } else if (index < findIndex + findKey.length) {
                keyword = findKey
            } else {
                keywordAfter += item
            }
        }
    } else {
        keywordAfter = content
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Black80, fontWeight = FontWeight.Normal, fontSize = 18.sp)) {
                    append(keywordPre)
                }
                withStyle(style = SpanStyle(color = Black80, fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                    append(keyword)
                }
                withStyle(style = SpanStyle(color = Black80, fontWeight = FontWeight.Normal, fontSize = 18.sp)) {
                    append(keywordAfter)
                }
            }, overflow = TextOverflow.Ellipsis
        )
    }
}
