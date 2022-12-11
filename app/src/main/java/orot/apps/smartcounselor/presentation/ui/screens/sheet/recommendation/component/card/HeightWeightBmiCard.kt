package orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.card


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import orot.apps.smartcounselor.model.local.ResultMeasurementCardInfo
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.style.Primary
import orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.common.WarningTextContent
import orot.apps.smartcounselor.presentation.ui.utils.modifier.backgroundHGradient

/** 키, 몸무게, bmi */
@Composable
fun HeightWeightBmiCard(modifier: Modifier) {

    val heightAndWeightDataList = listOf<ResultMeasurementCardInfo>(
        ResultMeasurementCardInfo("키", Pair(173f, "cm"), "", null),
        ResultMeasurementCardInfo("몸무게", Pair(83f, "kg"), "", null),
        ResultMeasurementCardInfo("비만도(BMI)", Pair(26f, ""), "주의가 필요해요", null),
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HeightWeightCard(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f),
            heightAndWeightDataList = heightAndWeightDataList
        )

        BmiCard(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(vertical = 20.dp, horizontal = 12.dp),
            heightAndWeightDataList = heightAndWeightDataList
        )
    }
}

@Composable
private fun HeightWeightCard(modifier: Modifier, heightAndWeightDataList: List<ResultMeasurementCardInfo>) {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp * 0.25f

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        heightAndWeightDataList.filterIndexed { index, _ ->
            index < heightAndWeightDataList.lastIndex
        }.map {
            Row(
                modifier = Modifier
                    .width(width)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 20.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = it.type,
                    style = MaterialTheme.typography.h3,
                    color = Black80,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight(500)
                )
                Text(
                    text = "${it.value.first}${it.value.second}",
                    style = MaterialTheme.typography.Display3,
                    color = Black80,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
private fun BmiCard(modifier: Modifier, heightAndWeightDataList: List<ResultMeasurementCardInfo>) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        heightAndWeightDataList.filterIndexed { index, resultMeasurementCardInfo ->
            index == heightAndWeightDataList.lastIndex
        }.map { it ->
            BmiTitleAndValue(title = it.type, value = it.value.first)
            BmiWarningAndPercentage(resultMeasurementCardInfo = it)
        }
    }
}


@Composable
private fun BmiTitleAndValue(title: String, value: Float) {
    Text(
        text = title,
        style = MaterialTheme.typography.h1,
        color = Black80,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight(500)
    )
    Text(
        modifier = Modifier.padding(top = 12.dp),
        text = "$value",
        style = MaterialTheme.typography.Display3,
        color = Black80,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Bold
    )
}


@Composable
private fun BmiWarningAndPercentage(resultMeasurementCardInfo: ResultMeasurementCardInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BmiWarningText(resultMeasurementCardInfo = resultMeasurementCardInfo)
        BmiPercentage(modifier = Modifier.weight(1f), resultMeasurementCardInfo = resultMeasurementCardInfo)
    }
}

@Composable
private fun BmiWarningText(resultMeasurementCardInfo: ResultMeasurementCardInfo) {
    WarningTextContent(
        modifier = Modifier
            .padding(top = 6.dp)
            .wrapContentWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(resultMeasurementCardInfo.getBackgroundColor())
            .padding(horizontal = 6.dp, vertical = 2.dp),
        content = resultMeasurementCardInfo.ment
    )
}


@Composable
private fun BmiPercentage(modifier: Modifier, resultMeasurementCardInfo: ResultMeasurementCardInfo) {
    Column(
        modifier = modifier
    ) {
        val bmiBaseList = listOf(12, 19, 26, 33, 40)

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth(),
            constraintSet = ConstraintSet {
                val circle = createRefFor("circle")
                var circleRatio =
                    (resultMeasurementCardInfo.value.first - bmiBaseList.first()) / (bmiBaseList.last() - bmiBaseList.first())
                if (circleRatio == 0.0f) {
                    circleRatio = 0.001f
                }
                constrain(circle) {
                    linkTo(start = parent.start, end = parent.end, bias = circleRatio)
                }
            }) {
            Canvas(
                modifier = Modifier
                    .layoutId("circle")
                    .size(size = 8.dp)
                    .clip(CircleShape)
                    .border(color = Primary, width = 1.dp)
                    .background(Primary)
            ) {}
        }

        Divider(
            modifier = Modifier
                .padding(top = 2.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(30.dp))
                .backgroundHGradient(
                    listOf(
                        Color(0xFFB5D4F1),
                        Color(0xFF81E5DB),
                        Color(0xFFE8D284),
                        Color(0xFFE2798E)
                    )
                )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            bmiBaseList.forEach {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "$it",
                    style = MaterialTheme.typography.caption,
                    color = Black80,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
