package orot.apps.smartcounselor.presentation.components.blood_pressure

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
import androidx.hilt.navigation.compose.hiltViewModel
import orot.apps.smartcounselor.BottomMenu
import orot.apps.smartcounselor.MagoActivity
import orot.apps.smartcounselor.MainViewModel
import orot.apps.smartcounselor.Screens
import orot.apps.smartcounselor.presentation.app_style.Display1
import orot.apps.smartcounselor.presentation.app_style.Primary
import orot.apps.sognora_compose_extension.animation.clickBounce
import orot.apps.sognora_viewmodel_extension.getViewModel

@Composable
fun BloodPressureSubmitButton(
    mainViewModel: MainViewModel = getViewModel(vm = hiltViewModel())
) {
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
                    MagoActivity.navigationKit.clearAndMove(Screens.Conversation.route) {
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
