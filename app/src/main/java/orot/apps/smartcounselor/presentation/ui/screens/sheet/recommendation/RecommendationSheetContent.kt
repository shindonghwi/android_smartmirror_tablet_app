package orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.Display2
import orot.apps.smartcounselor.presentation.style.Display3
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.card.GlucoseHrBpCard
import orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.card.ResultFinalCommentCard
import orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation.component.card.TemperatureWeightCard
import orot.apps.smartcounselor.presentation.ui.utils.date.DateUtil.getCurrentDate
import orot.apps.smartcounselor.presentation.ui.utils.modifier.noDuplicationClickable


@Composable
fun RecommendationSheetContent() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val isShowing = mainViewModel.isShowingRecommendationBottomSheet.collectAsState().value

    AnimatedVisibility(
        visible = isShowing,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .clip(RoundedCornerShape(bottomStart = 35.dp))
                    .background(Color.White)
                    .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResultMenu()
            }
            ResultContent()
        }
    }

    BackHandler(enabled = isShowing) {
        mainViewModel.proceedAfterMeasurement()
    }
}


@Composable
private fun ResultMenu() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val selectedIndex = mainViewModel.isSelectedResultMenu.collectAsState().value

    val menuList = listOf<Pair<String, Int>>(
        Pair("홈", R.drawable.result_home),
        Pair("기록", R.drawable.result_chart),
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        menuList.forEachIndexed { index, item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp))
                    .background(if (selectedIndex == index) Black80 else Color.White)
                    .noDuplicationClickable {
                        mainViewModel.changeSelectedResultMenu(index)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    painter = painterResource(id = item.second),
                    contentDescription = null,
                    tint = if (selectedIndex == index) Color.White else Black80
                )
                Text(
                    text = item.first,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                    color = if (selectedIndex == index) Color.White else Black80
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .size(80.dp)
                .clip(RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp))
                .background(Color.White)
                .noDuplicationClickable {
                    mainViewModel.proceedAfterMeasurement()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.result_exit),
                contentDescription = null,
                tint = Black80
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "나가기",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                color = Black80
            )
        }
    }

}


@Composable
private fun ResultContent() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value

    when (mainViewModel.isSelectedResultMenu.collectAsState().value) {
        0 -> {
            ResultContentHome()
        }
        else -> {
            ResultContentHistory()
        }
    }
}

@Composable
private fun ResultContentHistory() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(Color(0xFFFAFAFA))
            .padding(all = 20.dp)
    ) {
        RecommendationTitle("기간별 위험 수치")

        Text(
            modifier = Modifier.padding(vertical = 50.dp),
            text = "최근 2주간 의료데이터 기반으로 추출된 결과입니다.",
            style = MaterialTheme.typography.body2,
            color = Black80.copy(alpha = 0.5f)
        )

        HistoryRadarChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(horizontal = 40.dp)
        )

        HistoryBarChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 40.dp)
                .background(Color.White)
                .padding(vertical = 10.dp)
        )


        RiskPredictionRecommend()
    }
}

@Composable
private fun RiskPredictionRecommend() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value

    mainViewModel.riskPredictionInfo?.recommendation?.let {
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .clip(RoundedCornerShape(12.dp))
                .padding(vertical = 20.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "수집된 2주간의 데이터 분석 결과",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.Display3,
                color = Black80,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 30.dp),
                text = it.sentence,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                color = Black80,
                fontWeight = FontWeight(500)
            )
        }
    }
}

@Composable
private fun HistoryRadarChart(modifier: Modifier) {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        mainViewModel.riskPredictionInfo?.let { info ->
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    RadarChart(context)
                },
                update = { radarChart ->

                    val labels = arrayListOf<String>()

                    val defaultDataset = RadarDataSet(arrayListOf<RadarEntry>().apply {
                        repeat((0 until 5).count()) { add(RadarEntry(50f)) }
                    }, "default").apply {
                        color = Color(0xFF026841).toArgb()
                        fillColor = Color(0xFF026841).copy(alpha = 0.24f).toArgb()
                        setDrawFilled(true)
                    }
                    val healDataset = RadarDataSet(arrayListOf<RadarEntry>().apply {
                        info.measurement.bloodPressureSystolic?.score?.toFloat()?.let {
                            add(RadarEntry(it))
                            labels.add("수축기혈압\n(SBP)")
                        }
                        info.measurement.bloodPressureDiastolic?.score?.toFloat()?.let {
                            add(RadarEntry(it))
                            labels.add("확장기혈압\\n(DBP)")
                        }
                        info.measurement.bodyMassIndex?.score?.toFloat()?.let {
                            add(RadarEntry(it))
                            labels.add("체질량지수\\n(BMI)")
                        }
                        info.measurement.glucose?.score?.toFloat()?.let {
                            add(RadarEntry(it))
                            labels.add("혈당\\n(Glucose)")
                        }
                        info.measurement.heartRate?.score?.toFloat()?.let {
                            add(RadarEntry(it))
                            labels.add("맥박\\n(Mean.HRT)")
                        }
                    }, "healDataset").apply {
                        color = Color(0xFFFF8A65).toArgb()
                        fillColor = Color(0xFFFF8A65).copy(alpha = 0.24f).toArgb()
                        setDrawFilled(true)
                    }

                    val radarData = RadarData()
                    radarData.addDataSet(healDataset)
                    radarData.addDataSet(defaultDataset)

                    val xAxis = radarChart.xAxis
                    xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                    val yAxis = radarChart.yAxis
                    yAxis.valueFormatter = IndexAxisValueFormatter(labels)

                    radarChart.apply {
                        setTouchEnabled(false)
                        legend.isEnabled = false
                        description.isEnabled = false
                        getYAxis().axisMinimum = 0f
                        getYAxis().axisMaximum = 100f
                        getYAxis().setLabelCount(11, true)
                        data = radarData
                    }
                },
            )
        } ?: kotlin.run {
            Text(
                text = "기간별 정보가 없습니다.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.Display3,
                color = Black80
            )
        }
    }
}

@Composable
fun HistoryBarChart(modifier: Modifier) {

    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value

    mainViewModel.riskPredictionInfo?.recommendation?.let { info ->
        Column(modifier = modifier) {

            Text(
                modifier = Modifier
                    .weight(0.2f)
                    .padding(start = 20.dp, top = 10.dp),
                text = "심혈관 질환 점수",
                style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold),
                color = Black80
            )

            Row(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .fillMaxHeight(fraction = info.current_status.toFloat() / 10.0.toFloat())
                        .background(Color(0xFF026841))
                ) {}
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .fillMaxHeight(fraction = info.goal_status.toFloat() / 10.0.toFloat())
                        .background(Color(0xFFFF8A65))
                ) {}
            }
            Row(
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "현재(${info.current_status.toFloat() * 10})",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = Black80
                )
                Text(
                    text = "목표(${info.goal_status.toFloat() * 10})",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    color = Black80
                )

            }
        }
    }
}


@Composable
private fun ResultContentHome() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(Color(0xFFFAFAFA))
            .padding(all = 20.dp)
    ) {
        RecommendationTitle("건강 지표 결과")

        TemperatureWeightCard(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        )

        ResultFinalCommentCard(
            modifier = Modifier
                .padding(top = 35.dp)
                .fillMaxWidth()
                .height(420.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFC0F0F).copy(0.1f))
                .padding(all = 20.dp)
        )

        GlucoseHrBpCard(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Composable
private fun RecommendationTitle(title: String) {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    Row {
        Column {
            Text(
                text = title, style = MaterialTheme.typography.Display2, color = Black80
            )
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = getCurrentDate(),
                style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight(400)),
                color = Black80
            )
        }

        Box(
            modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(modifier = Modifier
                .size(48.dp)
                .noDuplicationClickable {
                    mainViewModel.proceedAfterMeasurement()
                }
                .padding(8.dp), imageVector = Icons.Default.Close, contentDescription = null)
        }
    }
}
