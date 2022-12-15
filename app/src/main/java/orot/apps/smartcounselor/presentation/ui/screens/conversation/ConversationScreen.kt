package orot.apps.smartcounselor.presentation.ui.screens.conversation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.R
import orot.apps.smartcounselor.model.local.RecommendationMent
import orot.apps.smartcounselor.presentation.components.animation.RotationAnimation
import orot.apps.smartcounselor.presentation.ui.MagoActivity
import orot.apps.smartcounselor.presentation.ui.screens.chat_list.ChatContentTypeHolder
import orot.apps.smartcounselor.presentation.ui.screens.chat_list.ChatViewContent

@Composable
fun ConversationScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(start = 20.dp, end = 20.dp, top = 40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(0.2f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    RotateIconContent()
                }
                Row(
                    modifier = Modifier.weight(0.8f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AIReceivedTextContent()
                }
            }
        }
    }
}

@Composable
private fun AIReceivedTextContent() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val content = mainViewModel.displayText.collectAsState().value
    val isVisible = remember { mainViewModel.conversationVisibleState }

    AnimatedVisibility(
        visibleState = isVisible, enter = fadeIn(
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        ), exit = fadeOut(
            animationSpec = tween(
                durationMillis = Int.MAX_VALUE,
                easing = LinearOutSlowInEasing
            ),
            targetAlpha = 0.3f
        )
    ) {
        DefaultDisplayText(content.toString())
    }
}

@Composable
private fun RotateIconContent() {
    val mainViewModel = ((LocalContext.current) as MagoActivity).mainViewModel.value
    val ttsIsSpeaking = remember { mutableStateOf(mainViewModel.ttsIsSpeaking) }

    RotationAnimation(
        isPlaying = ttsIsSpeaking.value,
        iconDrawable = R.drawable.mago_logo_icon,
        iconSize = 70.dp
    )
}

@Composable
private fun RecommendationText(showingContentList: ArrayList<RecommendationMent>) {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp / 2
    val height = width / 1.5f
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(items = showingContentList, key = { index, item -> index }) { index, item ->
            val title = item.title
            val content = item.content

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 18.dp)
                    .width(width),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Center)
                )

                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(height)
                        .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                        .background(color = Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = content,
                        color = Color.White,
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun DefaultDisplayText(showingContent: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = showingContent,
            color = Color.White,
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.Center
        )
    }
}