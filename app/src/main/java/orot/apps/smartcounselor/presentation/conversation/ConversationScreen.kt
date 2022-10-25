package orot.apps.smartcounselor.presentation.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import orot.apps.smartcounselor.presentation.app_style.Display2
import orot.apps.sognora_compose_extension.components.AnimationText

@Composable
fun ConversationScreen() {
    val guideTextList = listOf(
        "안녕하세요",
        "Mago Healthcare 서비스에 오신걸 환영합니다",
        "AI와 대화를 시작하세요"
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimationText(
            modifier = Modifier,
            textList = guideTextList
        ) {
            Text(
                text = it,
                color = Color.White,
                style = MaterialTheme.typography.Display2.copy(textAlign = TextAlign.Center)
            )
        }
    }

}