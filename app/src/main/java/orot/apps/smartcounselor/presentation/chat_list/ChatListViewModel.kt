package orot.apps.smartcounselor.presentation.chat_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(): ViewModel(){

    val chatDataList = listOf<ChatData>(
        ChatData(msg = "1김철수님 안녕하세요”\n좋은 아침입니다.\n어제 밤 잘 주무셨나요?", isUser = false),
        ChatData(msg = "잘잤어", isUser = true),
        ChatData(msg = "이제 몇가지 건강측정을 하겠습니다. \n팔을 혈압기에 넣고, 편안히 계시기 바랍니다.", isUser = false),
        ChatData(msg = "네, 지금할게", isUser = true),
        ChatData(msg = "2김철수님 안녕하세요”\n좋은 아침입니다.\n어제 밤 잘 주무셨나요?", isUser = false),
        ChatData(msg = "잘잤어", isUser = true),
        ChatData(msg = "이제 몇가지 건강측정을 하겠습니다. \n팔을 혈압기에 넣고, 편안히 계시기 바랍니다.", isUser = false),
        ChatData(msg = "네, 지금할게", isUser = true),
        ChatData(msg = "3김철수님 안녕하세요”\n좋은 아침입니다.\n어제 밤 잘 주무셨나요?", isUser = false),
        ChatData(msg = "잘잤어", isUser = true),
        ChatData(msg = "이제 몇가지 건강측정을 하겠습니다. \n팔을 혈압기에 넣고, 편안히 계시기 바랍니다.", isUser = false),
        ChatData(msg = "네, 지금할게", isUser = true),
        ChatData(msg = "4김철수님 안녕하세요”\n좋은 아침입니다.\n어제 밤 잘 주무셨나요?", isUser = false),
        ChatData(msg = "잘잤어", isUser = true),
        ChatData(msg = "이제 몇가지 건강측정을 하겠습니다. \n팔을 혈압기에 넣고, 편안히 계시기 바랍니다.", isUser = false),
        ChatData(msg = "네, 지금할게", isUser = true),
        ChatData(msg = "5김철수님 안녕하세요”\n좋은 아침입니다.\n어제 밤 잘 주무셨나요?", isUser = false),
        ChatData(msg = "잘잤어", isUser = true),
        ChatData(msg = "이제 몇가지 건강측정을 하겠습니다. \n팔을 혈압기에 넣고, 편안히 계시기 바랍니다.", isUser = false),
        ChatData(msg = "네, 지금할게", isUser = true),
    )

}

data class ChatData(
    val msg: String,
    val isUser: Boolean
)