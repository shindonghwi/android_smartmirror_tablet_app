package orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import java.util.*


@Composable
fun ResultFinalCommentCard(modifier: Modifier) {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    var content = ""

    mainViewModel.displayInfo?.today_recommendation?.let {
        content = if (!it.food.isNullOrEmpty() && !it.exercise.isNullOrEmpty()) {
            if (Random().nextInt(2) == 0) {
                it.food.toString()
            } else {
                it.exercise.toString()
            }
        } else if (!it.food.isNullOrEmpty()) {
            it.food.toString()
        } else {
            it.exercise.toString()
        }
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 60.dp)
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(120.dp))
                .background(Color.White)
                .padding(vertical = 40.dp, horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "건강한 내일을 위한 오늘의 약속",
                style = MaterialTheme.typography.Display3,
                color = Primary,
            )

            mainViewModel.displayInfo?.today_recommendation?.let {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.h2,
                        color = Black80,
                        fontWeight = FontWeight(400),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Icon(
                modifier = Modifier
                    .padding(bottom = 10.dp, end = 10.dp)
                    .size(150.dp),
                painter = painterResource(id = R.drawable.result_person),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}
