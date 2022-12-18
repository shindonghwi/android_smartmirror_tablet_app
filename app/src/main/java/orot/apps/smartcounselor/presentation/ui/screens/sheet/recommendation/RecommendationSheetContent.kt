package orot.apps.smartcounselor.presentation.ui.screens.sheet.recommendation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
import orot.apps.smartcounselor.presentation.style.Primary
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
                    .padding(top = 70.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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

    LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        itemsIndexed(menuList, key = { index, item -> index }) { index, item ->
            Column(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(topStart = 35.dp, bottomStart = 35.dp))
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
    }
}


@Composable
private fun ResultContent() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value

    when (mainViewModel.isSelectedResultMenu.collectAsState().value) {
        0 -> ResultContentHome()
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
            text = "최근 10일간 의료데이터 기반으로 추출된 결과입니다.",
            style = MaterialTheme.typography.body2,
            color = Black80.copy(alpha = 0.5f)
        )

        HistoryChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(horizontal = 20.dp)
        )
    }
}

@Composable
private fun HistoryChart(modifier: Modifier) {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
//        mainViewModel.riskPredictionInfo?.let {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                RadarChart(context)
            },
            update = { radarChart ->

                val defaultDataset = RadarDataSet(arrayListOf<RadarEntry>().apply {

                }, "default")
                val healDataset = RadarDataSet(arrayListOf<RadarEntry>().apply {
                    add(RadarEntry(15f))
                    add(RadarEntry(25f))
                    add(RadarEntry(35f))
                    add(RadarEntry(45f))
                    add(RadarEntry(55f))
//                        add(RadarEntry(it.measurement.bloodPressureSystolic.value))
//                        add(RadarEntry(it.measurement.bloodPressureDiastolic.value))
//                        add(RadarEntry(it.measurement.bodyMassIndex.value))
//                        add(RadarEntry(it.measurement.glucose.value))
//                        add(RadarEntry(it.measurement.heartRate.value))
                }, "healDataset")

                defaultDataset.color = Color.Red.toArgb()
                healDataset.color = Color(0xFFFF8A65).toArgb()

                val radarData = RadarData()
                radarData.addDataSet(defaultDataset)
                radarData.addDataSet(healDataset)

                val labels = arrayOf(
                    "수축기혈압\n(SBP)",
                    "확장기혈압\n(DBP)",
                    "체질량지수\n(BMI)",
                    "혈당\n(Glucose)",
                    "맥박\n(Mean.HRT)"
                )
                val xAxis = radarChart.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                val yAxis = radarChart.yAxis
                yAxis.valueFormatter = IndexAxisValueFormatter(labels)

                radarChart.apply {
                    data = radarData
                    legend.isEnabled = false
                    description.isEnabled = false
                    yAxis.mAxisMinimum = 0f
                    yAxis.mAxisMaximum = 200f
                    yAxis.setLabelCount(8, true)
                }
            },
        )
//        } ?: kotlin.run {
//            Text(
//                text = "기간별 정보가 없습니다.",
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.Display3,
//                color = Black80
//            )
//        }
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

        CloseButton(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .height(90.dp),
        )
    }
}

@Composable
private fun CloseButton(modifier: Modifier) {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val configuration = LocalConfiguration.current
    val startWidth: Dp by lazy { configuration.screenWidthDp.dp * 0.3f }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(startWidth)
                .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                .background(Primary)
                .noDuplicationClickable {
                    mainViewModel.proceedAfterMeasurement()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = 18.dp, horizontal = 30.dp),
                text = "이어서 진행하기",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                color = Color.White
            )
        }
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
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .noDuplicationClickable {
                        mainViewModel.proceedAfterMeasurement()
                    }
                    .padding(8.dp),
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
    }
}
