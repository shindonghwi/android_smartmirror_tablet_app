package orot.apps.smartcounselor.presentation.ui.screens.blood_pressure.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.graph.model.BottomMenu
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.model.local.ActionType
import orot.apps.smartcounselor.model.remote.asMap
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.utils.modifier.clickBounce

@Composable
fun BloodPressureSubmitButton(modifier: Modifier = Modifier) {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.5f }
    Box {
        Text(modifier = modifier
            .padding(top = 18.dp)
            .width(startWidth)
            .clickBounce {

                val remainKey = mainViewModel.userInputData?.asMap()?.entries
                    ?.filter { it.value == null }
                    ?.map { it.key } // 아직 입력하지 않은 정보

                takeIf {
                    remainKey?.size == 0
                }?.run {
                    mainViewModel.run {
                        val content = "헬스케어 결과를 불러오는중입니다\n잠시만 기다려주세요"
                        updateHeartAnimationState(false)
                        playGoogleTts(content)
                        changeConversationList(ActionType.RESULT_WAITING, content, null)
                        moveScreen(Screens.Conversation, BottomMenu.Loading)
                    }
                } ?: run {
                    Toast
                        .makeText(context, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
            .background(Primary)
            .padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            text = "제출",
            style = MaterialTheme.typography.Display3,
            color = Color.White)
    }
}
