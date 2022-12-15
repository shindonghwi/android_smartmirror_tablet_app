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
fun WarningTextContent(modifier: Modifier, status: String?) {
    var preContent = ""
    var mainContent = ""

    status?.let {
        when(it){
            "low","proper" -> {
                preContent = "정상"
                mainContent = "범위에요"
            }
            "warn" -> {
                preContent = "주의"
                mainContent = "가 필요해요"
            }
            "high","alert" -> {
                preContent = "위험"
                mainContent = "한 수치에요"
            }
        }

        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Black80, fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
                        append(preContent)
                    }
                    withStyle(style = SpanStyle(color = Black80, fontWeight = FontWeight.Normal, fontSize = 18.sp)) {
                        append(mainContent)
                    }
                }, overflow = TextOverflow.Ellipsis
            )
        }
    }
}
