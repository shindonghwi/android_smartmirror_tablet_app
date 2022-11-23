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
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.navigationKit
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.model.local.ConversationType
import orot.apps.smartcounselor.presentation.style.Display1
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.sognora_compose_extension.animation.clickBounce

@Composable
fun BloodPressureSubmitButton() {

    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.35f }

    Box {
        Text(modifier = Modifier
            .width(startWidth)
            .clickBounce {
                takeIf {
                    mainViewModel.bloodPressureMin != 0 &&
                            mainViewModel.bloodPressureMax != 0 &&
                            mainViewModel.bloodPressureSugar != 0
                }?.run {
                    mainViewModel.run {
                        updateHeartAnimationState(false)
                        changeConversationList(
                            ConversationType.RESULT_WAITING,
                            listOf(
                                "헬스케어 결과를 불러오는중입니다\n잠시만 기다려주세요"
                            ),
                            null
                        )
                    }
                    navigationKit.clearAndMove(Screens.Conversation.route) {
                        mainViewModel.updateBottomMenu(BottomMenu.Loading)
                    }
                } ?: run {
                    Toast
                        .makeText(context, "혈압과 혈당량을 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .clip(RoundedCornerShape(corner = CornerSize(20.dp)))
            .background(Primary)
            .padding(vertical = 36.dp),
            textAlign = TextAlign.Center,
            text = "제출",
            style = MaterialTheme.typography.Display1,
            color = Color.White
        )
    }
}
