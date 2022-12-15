package orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.BodyL
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.MagoActivity


@Composable
fun ResultFinalCommentCard(modifier: Modifier) {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value

    mainViewModel.recommendationInfo?.let {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "건강한 내일을 위한 오늘의 약속", style = MaterialTheme.typography.Display3, color = Primary
            )
            it.today_recommendation.food.let {
                if (!it.isNullOrEmpty()) {
                    ResultFinalCareItem(typeIsFood = true, content = it)
                }
            }
            it.today_recommendation.exercise.let {
                if (!it.isNullOrEmpty()) {
                    ResultFinalCareItem(typeIsFood = false, content = it)
                }
            }
        }
    }
}

@Composable
private fun ResultFinalCareItem(typeIsFood: Boolean, content: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(0.4f), contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(
                    id = if (typeIsFood) {
                        R.drawable.result_sample_food
                    } else {
                        R.drawable.result_sample_exercise
                    }
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Text(
            modifier = Modifier.weight(0.6f),
            text = content,
            style = MaterialTheme.typography.BodyL,
            color = Black80,
            fontWeight = FontWeight(400),
            textAlign = TextAlign.Center
        )
    }
}