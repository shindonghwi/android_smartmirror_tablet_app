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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.presentation.style.Black80
import orot.apps.smartcounselor.presentation.style.Display2
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
    val selectedIndex = mainViewModel.isSelectedResultMenu.collectAsState().value

    when (selectedIndex) {
        0 -> ResultContentHome()
        else -> {}
    }
}

@Composable
private fun ResultContentHome() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
            .background(Color(0xFFFAFAFA))
            .padding(all = 20.dp)
    ) {
        RecommendationTitle()

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
private fun RecommendationTitle() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    Row {
        Column {
            Text(
                text = "건강 지표 결과", style = MaterialTheme.typography.Display2, color = Black80
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
