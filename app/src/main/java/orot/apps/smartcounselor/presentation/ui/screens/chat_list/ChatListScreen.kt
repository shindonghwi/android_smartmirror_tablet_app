package orot.apps.smartcounselor.presentation.ui.screens.chat_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import orot.apps.smartcounselor.model.local.ChatData
import orot.apps.smartcounselor.presentation.ui.MagoActivity

@Composable
fun ChatListScreen() {
    ChatViewContent()
}

@Composable
fun ChatViewContent() {
    val mainViewModel = (LocalContext.current as MagoActivity).mainViewModel.value
    if (mainViewModel.isChatViewShowing.collectAsState().value) {
        LazyColumn(
            modifier = Modifier.padding(top = 40.dp),
            state = rememberLazyListState(),
            reverseLayout = false,
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(
                count = mainViewModel.chatList.size
            ) { idx ->
                ChatContentTypeHolder(mainViewModel.chatList.elementAtOrNull(idx))
            }
        }
    }
}


@Composable
fun ChatContentTypeHolder(chatData: ChatData?) {

    when (chatData?.isUser) {
        false -> {
            AiTextContent(chatData.msg)
        }
        true -> {
            UserTextContent(chatData.msg)
        }
        else -> {}
    }
}

@Composable
private fun UserTextContent(msg: String) {
    val config = LocalConfiguration.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            modifier = Modifier
                .widthIn(max = config.screenWidthDp.dp * 0.72f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(vertical = 14.dp, horizontal = 12.dp),
            text = msg,
            style = MaterialTheme.typography.h3,
            color = Color.Black
        )
    }
}

@Composable
private fun AiTextContent(msg: String) {
    val config = LocalConfiguration.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, bottom = 35.dp, top = 35.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .widthIn(max = config.screenWidthDp.dp * 0.72f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE16145))
                .padding(vertical = 14.dp, horizontal = 12.dp),
            text = msg,
            style = MaterialTheme.typography.h3,
            color = Color.White
        )
    }
}