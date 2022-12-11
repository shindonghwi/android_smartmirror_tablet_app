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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.model.local.ResultMeasurementCardInfo
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.Display2
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.common.WarningTextContent


/** 혈당, 맥박, 혈압 */
@Composable
fun GlucoseHrBpCard(modifier: Modifier) {

    val measurementCarList = listOf(
        ResultMeasurementCardInfo(
            "혈당", Pair(80f, "mg/dL"),
            "주의가 필요해요", painterResource(id = R.drawable.result_glucose)
        ),
        ResultMeasurementCardInfo(
            "맥박", Pair(92f, "bpm"),
            "위험한 수치에요", painterResource(id = R.drawable.result_heartbeat)
        ),
        ResultMeasurementCardInfo(
            "혈압", Pair(80f, "mmhg"),
            "정상 범위에요", painterResource(id = R.drawable.result_bloodpressure)
        ),
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        measurementCarList.forEach {
            CardItem(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 40.dp, horizontal = 20.dp),
                cardInfo = it
            )
        }
    }
}


@Composable
private fun CardItem(modifier: Modifier, cardInfo: ResultMeasurementCardInfo) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            cardInfo.image?.let {
                Icon(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(cardInfo.getBackgroundColor())
                        .padding(10.dp),
                    painter = it,
                    contentDescription = null,
                    tint = cardInfo.getIconColor()
                )
            }
            Text(
                modifier = Modifier.padding(start = 18.dp),
                text = cardInfo.type,
                style = MaterialTheme.typography.Display3,
                color = Black80,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(modifier = Modifier.padding(top = 24.dp)) {
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = "${cardInfo.value.first}",
                style = MaterialTheme.typography.Display2,
                color = Black80,
                fontWeight = FontWeight(400),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(start = 8.dp, bottom = 2.dp),
                text = cardInfo.value.second,
                style = MaterialTheme.typography.body1,
                color = Black80.copy(0.4f),
                overflow = TextOverflow.Ellipsis
            )
        }

        WarningTextContent(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(cardInfo.getBackgroundColor())
                .padding(horizontal = 6.dp, vertical = 2.dp),
            content = cardInfo.ment
        )
    }
}
