package orot.apps.smartcounselor.presentation.ui

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.TextUtils
import android.util.Log
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import mago.apps.sognoraaudio.audio_recoder.SognoraAudioRecorder
import mago.apps.sognorawebsocket.websocket.SognoraWebSocket
import mago.apps.sognorawebsocket.websocket.SognoraWebSocketListener
import mago.apps.sognorawebsocket.websocket.model.WebSocketState
import okhttp3.Response
import okio.ByteString
import orot.apps.smartcounselor.graph.NavigationKit
import orot.apps.smartcounselor.graph.model.BottomMenu
import orot.apps.smartcounselor.graph.model.Screens
import orot.apps.smartcounselor.model.local.ActionType
import orot.apps.smartcounselor.model.local.ChatData
import orot.apps.smartcounselor.model.local.RecommendationMent
import orot.apps.smartcounselor.model.remote.*
import orot.apps.smartcounselor.model.remote.mapper.body.toMeasurement
import orot.apps.smartcounselor.model.remote.mapper.header.toStream
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.TAG
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnDefault
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.onDefault
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.onIO
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val sognoraWebSocket: SognoraWebSocket,
    val sognoraAudioRecorder: SognoraAudioRecorder,
) : ViewModel() {
    lateinit var navigationKit: NavigationKit

    /**
     * ================================================
     *     Web Sokcet
     * ================================================
     * */
    fun connectWebSocket() {
        sognoraWebSocket.apply {
            setWebSocketListener(object : SognoraWebSocketListener {
                override fun open(response: Response) {
                    Log.w(TAG, "open: ${response.body} | ${response.message}")
                    moveScreen(Screens.Conversation, BottomMenu.Conversation)
                    startAudioRecorder()
                    sendProtocol(1)
                }

                override fun onMessageText(msg: String) {
                    Log.w(TAG, "onMessageText: $msg")

                    try {
                        val receivedMsg: MessageProtocol =
                            Gson().fromJson(msg, MessageProtocol::class.java)

                        val protocol = receivedMsg.header.protocol_id
                        val voice = receivedMsg.body?.voice
                        val display = receivedMsg.body?.display

                        receivedMsg.body?.let {
                            beforeBody = receivedMsg.body
                        }

                        val isUser = protocol == MAGO_PROTOCOL.PROTOCOL_11.id

                        voice?.let {
                            if (it.text.isNotEmpty()) {
                                chatList.add(ChatData(msg = it.text, isUser = isUser))
                            }
                        }

                        when (protocol) {
                            /** AI의 첫 인사 */
                            MAGO_PROTOCOL.PROTOCOL_2.id -> {
                                voice?.let {
                                    playGoogleTts(it.text)
                                    changeConversationList(
                                        ActionType.GREETING_END, it.text, receivedMsg
                                    )
                                }
                            }

                            /** 내가 말한 내용을 STT 해서 결과를 응답받음. */
                            MAGO_PROTOCOL.PROTOCOL_11.id -> {
                                stopGoogleTts()
                                changeSendingStateAudioBuffer(false)
                                voice?.let {
                                    changeSaidMeText(it.text)
                                }
                            }
                            /** AI의 다음 질문 */
                            MAGO_PROTOCOL.PROTOCOL_12.id -> {
                                sendProtocol(13, conversationInfo.value.third)

                                val type = when (receivedMsg.body?.action) {
                                    "measurement" -> ActionType.MEASUREMENT
                                    "end" -> ActionType.END
                                    "doctorcall" -> ActionType.DOCTORCALL
                                    "exit" -> ActionType.EXIT
                                    else -> ActionType.CONVERSATION
                                }

                                voice?.let { voiceInfo ->
                                    playGoogleTts(voiceInfo.text)

                                    if (ActionType.DOCTORCALL == type) {
                                        val showingContent = "[recommendation]"

                                        display?.current_status?.let {
                                            if (it.isNotEmpty()) {
                                                tempRecommendationMent.add(
                                                    RecommendationMent(
                                                        "현재상태",
                                                        TextUtils.join("\n", it)
                                                    )
                                                )
                                            }
                                        }

                                        display?.food?.let {
                                            if (it.isNotEmpty()) {
                                                tempRecommendationMent.add(
                                                    RecommendationMent(
                                                        "음식",
                                                        TextUtils.join("\n", it)
                                                    )
                                                )
                                            }
                                        }

                                        display?.exercise?.let {
                                            if (it.isNotEmpty()) {
                                                tempRecommendationMent.add(
                                                    RecommendationMent(
                                                        "운동",
                                                        TextUtils.join("\n", it)
                                                    )
                                                )
                                            }
                                        }

                                        display?.warning?.let {
                                            if (it.isNotEmpty()) {
                                                tempRecommendationMent.add(
                                                    RecommendationMent(
                                                        "경고",
                                                        TextUtils.join("\n", it)
                                                    )
                                                )
                                            }
                                        }

                                        changeConversationList(type, showingContent, receivedMsg)
                                    } else {
                                        changeConversationList(type, voiceInfo.text, receivedMsg)
                                    }
                                }

                                if (conversationInfo.value.first == ActionType.DOCTORCALL) {
                                    changeSaidMeText("")
                                    moveScreen(null, BottomMenu.Conversation)
                                }
                            }
                            MAGO_PROTOCOL.PROTOCOL_19.id -> {
                                changeSaidMeText("")
                            }
                            MAGO_PROTOCOL.PROTOCOL_99.id -> {
                                changeSaidMeText("")
                                voice?.let {
                                    playGoogleTts(it.text)
                                    changeConversationList(
                                        ActionType.CONVERSATION, it.text, null, isFallback = true
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "onMessageText Error: ${e.message}")
                    }
                }

                override fun onMessageByteString(byteString: ByteString) {
                    Log.w(TAG, "onMessageByteString: ")
                }

                override fun fail(response: Response?, t: Throwable) {
                    Log.w(TAG, "fail: $response || ${t.message}")
                }

                override fun close(code: Int, reason: String) {
                    Log.w(TAG, "close: ")
                }

                override fun onClosing(code: Int, reason: String) {
                    Log.w(TAG, "onClosing: ")
                }
            })
        }.run {
//            initWebSocket("ws://172.30.1.15:8080/ws/chat")
            initWebSocket("ws://demo-health-stream.mago52.com/ws/chat")
        }
    }

    private var isAvailableAudioBuffer: Boolean = false
    fun startAudioRecorder() {
        sognoraAudioRecorder.startAudioRecorder()
        onIO {
            try {
                do {
                    if (isAvailableAudioBuffer) {
                        val bufferSize = sognoraAudioRecorder.getMinBuffer()
                        val bufferInfo = sognoraAudioRecorder.frameBuffer()
                        bufferInfo.second?.let {
                            if (it < -1) return@let
                            sognoraWebSocket.sendBuffer(bufferInfo.first, bufferSize)
                        }
                    }
                } while (true)
            } catch (e: Exception) {
                Log.d(TAG, "sendAudioRecord ERROR: ${e.message}")
                sognoraAudioRecorder.stopAudioRecorder()
            }
        }
    }

    /**
     * ================================================
     *     SAVE DATA
     * ================================================
     * */
    var tempRecommendationMent: ArrayList<RecommendationMent> = arrayListOf()
    var beforeBody: BodyInfo? = null

    val userListInfo = mutableStateListOf<String>().apply {
        repeat((1..5).count()) { add("user$it") }
    }

    var addAccountInputData = ""

    fun addUser(userInfo: String){
        if (!userListInfo.contains(userInfo)){
            userListInfo.add(userInfo)
        }
    }

    var isShowingAccountBottomSheet = MutableStateFlow(false)
    fun changeBottomSheetFlag(flag: Boolean){
        isShowingAccountBottomSheet.update { flag }
    }

    val userAge = 60 // TODO: 사용자 나이, 성별 셋팅해야함.
    val userGender = "M"

    var userInputData: UserInputData? = UserInputData(
        medication = listOf("htn"),
        glucose = 105,
        bodyTemperature = 36.5f,
        height = 182f,
        weight = 92f,
        bodyMassIndex = 25.2f,
    )

    /**
     * ================================================
     *     UI STATE
     * ================================================
     * */
    val currentBottomMenu = mutableStateOf(BottomMenu.Start.type) // 바텀 메뉴

    var heartAnimationState = mutableStateOf(true)

    fun updateHeartAnimationState(flag: Boolean) {
        heartAnimationState.value = flag
    }

    fun moveScreen(screen: Screens? = null, bottomMenu: BottomMenu? = null) {
        screen?.let {
            navigationKit.clearAndMove(it.route) {
                bottomMenu?.run { currentBottomMenu.value = this.type }
            }
        } ?: run {
            bottomMenu?.run { currentBottomMenu.value = this.type }
        }

    }

    /**
     * ================================================
     *     Audio Stream
     * ================================================
     * */
    fun getWebSocketState() = sognoraWebSocket.getWebSocketState()
    val micIsAvailable = mutableStateOf(false) // 마이크 사용가능 상태


    /** 웹 소켓으로 이벤트 전달하기 */
    var lastProtocolNum: Int = -1
    fun sendProtocol(protocolNum: Int, body: MessageProtocol? = null) {
        if (lastProtocolNum == protocolNum) return
        else lastProtocolNum = protocolNum

        var protocolId = ""

        when (protocolNum) {
            1 -> protocolId = MAGO_PROTOCOL.PROTOCOL_1.id
            2 -> protocolId = MAGO_PROTOCOL.PROTOCOL_2.id
            3 -> protocolId = MAGO_PROTOCOL.PROTOCOL_3.id
            4 -> protocolId = MAGO_PROTOCOL.PROTOCOL_4.id
            5 -> protocolId = MAGO_PROTOCOL.PROTOCOL_5.id
            6 -> protocolId = MAGO_PROTOCOL.PROTOCOL_6.id
            7 -> protocolId = MAGO_PROTOCOL.PROTOCOL_7.id
            8 -> protocolId = MAGO_PROTOCOL.PROTOCOL_8.id
            9 -> protocolId = MAGO_PROTOCOL.PROTOCOL_9.id
            10 -> protocolId = MAGO_PROTOCOL.PROTOCOL_10.id
            11 -> protocolId = MAGO_PROTOCOL.PROTOCOL_11.id
            12 -> protocolId = MAGO_PROTOCOL.PROTOCOL_12.id
            13 -> protocolId = MAGO_PROTOCOL.PROTOCOL_13.id
            14 -> protocolId = MAGO_PROTOCOL.PROTOCOL_14.id
            15 -> protocolId = MAGO_PROTOCOL.PROTOCOL_15.id
            16 -> protocolId = MAGO_PROTOCOL.PROTOCOL_16.id
            17 -> protocolId = MAGO_PROTOCOL.PROTOCOL_17.id
            18 -> protocolId = MAGO_PROTOCOL.PROTOCOL_18.id
            19 -> protocolId = MAGO_PROTOCOL.PROTOCOL_19.id
            20 -> protocolId = MAGO_PROTOCOL.PROTOCOL_20.id
            21 -> protocolId = MAGO_PROTOCOL.PROTOCOL_21.id
            99 -> protocolId = MAGO_PROTOCOL.PROTOCOL_99.id
        }

        val header: HeaderInfo
        val newBody: BodyInfo?

        userInputData?.let {
            header = HeaderInfo().toStream(
                type = protocolId, age = userAge, gender = userGender
            )

            newBody = body?.body?.toMeasurement(
                before = beforeBody, measurementInfo = null
            )

            val newResMsg = MessageProtocol(
                header = header, body = if (protocolNum <= 2) null else newBody
            )
            val sendingData = Gson().toJson(newResMsg)
            Log.w(TAG, "sendProtocol: protocol: $protocolId / body: $sendingData")
            sognoraWebSocket.sendMsg(sendingData)
        }
    }

    /** 오디오스트림 생성*/
    fun createAudioStreamManager() {
        connectWebSocket()
    }

    fun changeSendingStateAudioBuffer(flag: Boolean) {
        isAvailableAudioBuffer = flag
        if (getWebSocketState().value is WebSocketState.Connected) {
            micIsAvailable.value = flag
            if (flag) {
                sendProtocol(3, conversationInfo.value.third)
            }
        }
    }

    val saidMeText = MutableStateFlow("")
    val chatList = mutableStateListOf<ChatData>()
    fun changeSaidMeText(msg: String) {
        saidMeText.value = msg
    }

    /**
     * ================================================
     *     TTS
     * ================================================
     * */

    enum class TTSCallback {
        IDLE, START, DONE, ERROR
    }

    private val params = Bundle()
    var conversationVisibleState = MutableTransitionState(false)
    var tts: TextToSpeech? = null
    val ttsState = MutableStateFlow(TTSCallback.IDLE)
    val ttsIsSpeaking = mutableStateOf(false) // TTS 말하는중 여부

    fun initTTS(context: Context) {
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, null)
        tts = TextToSpeech(context) { state ->
            if (state == TextToSpeech.SUCCESS) {
                tts?.apply {
                    language = Locale.KOREAN
                    setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            ttsIsSpeaking.value = true
                            changeSendingStateAudioBuffer(false)
                            conversationVisibleState.targetState = true
                            ttsState.value = TTSCallback.START
                        }

                        override fun onDone(utteranceId: String?) {
                            ttsIsSpeaking.value = false
                            conversationVisibleState.targetState = false
                            ttsState.value = TTSCallback.DONE

                            when (conversationInfo.value.first) {
                                ActionType.MEASUREMENT -> {
                                    moveScreen(Screens.BloodPressure, BottomMenu.BloodPressure)
                                }

                                ActionType.RESULT_WAITING -> {

                                    val header: HeaderInfo
                                    val newBody: BodyInfo?

                                    userInputData?.let {
                                        header = HeaderInfo().toStream(
                                            type = MAGO_PROTOCOL.PROTOCOL_18.id,
                                            age = userAge, gender = userGender
                                        )

                                        newBody = BodyInfo().toMeasurement(
                                            before = beforeBody, measurementInfo = MeasurementInfo(
                                                medication = it.medication ?: listOf("hnr"),
                                                bloodPressureSystolic = it.bloodPressureSystolic
                                                    ?: 120,
                                                bloodPressureDiastolic = it.bloodPressureDiastolic
                                                    ?: 80,
                                                glucose = it.glucose ?: 100,
                                                heartRate = it.heartRate ?: 60,
                                                bodyTemperature = it.bodyTemperature ?: 36.5f,
                                                height = it.height ?: 170f,
                                                weight = it.weight ?: 60f,
                                                bodyMassIndex = it.bodyMassIndex ?: 20.8f,
                                            )
                                        )
                                        sendProtocol(
                                            protocolNum = 18, body = MessageProtocol(
                                                header = header,
                                                body = newBody,
                                            )
                                        )
                                    }


                                }
                                ActionType.EXIT -> {
                                    onDefault {
                                        sendProtocol(20) // dialog end action을 보낸다
                                        changeSendingStateAudioBuffer(false)
                                        moveScreen(bottomMenu = BottomMenu.Loading)
                                        delay(2000)
                                        moveScreen(bottomMenu = BottomMenu.RetryAndChat)
                                    }
                                }
                                else -> {
                                    changeSendingStateAudioBuffer(true)
                                }
                            }
                        }

                        override fun onError(utteranceId: String?) {
                            ttsIsSpeaking.value = false
                            ttsState.value = TTSCallback.ERROR
                        }
                    })
                }
            }
        }
    }

    fun playGoogleTts(msg: String?) {
        tts?.let {
            if (it.isSpeaking) {
                ttsIsSpeaking.value = false
                it.stop()
            }
            msg?.let { content ->
                it.speak(content, TextToSpeech.QUEUE_ADD, params, content)
            }
        }
    }

    fun stopGoogleTts() {
        tts?.run {
            ttsIsSpeaking.value = false
            stop()
            null
        }
    }

    val conversationInfo: MutableStateFlow<Triple<ActionType, String, MessageProtocol?>> =
        MutableStateFlow(Triple(ActionType.IDLE, "", null))

    fun changeConversationList(
        type: ActionType,
        content: String,
        msgResponse: MessageProtocol?,
        isFallback: Boolean = false
    ) {
        if (!isFallback) {
            conversationInfo.value = Triple(type, content, msgResponse)
        } else {
            conversationInfo.value = Triple(type, content, conversationInfo.value.third)
        }
    }


    fun reset() {
        clearConversationData()
        clearUserInputData()
        clearWebSocketAudio()
        moveScreen(Screens.Home, BottomMenu.Start)
    }

    private fun clearConversationData() {
        tempRecommendationMent.clear()
        chatList.clear()
        conversationInfo.value = Triple(ActionType.IDLE, "", null)
        updateHeartAnimationState(false)
        changeSaidMeText("")
    }

    private fun clearWebSocketAudio() {
        ttsState.value = TTSCallback.IDLE
        stopGoogleTts()
        micIsAvailable.value = false
        lastProtocolNum = -1
        changeSendingStateAudioBuffer(false)
        sognoraWebSocket.close()
        sognoraAudioRecorder.stopAudioRecorder()
    }

    private fun clearUserInputData() {
        userInputData = null
        userInputData = UserInputData(
            medication = listOf("htn"),
            glucose = 105,
            bodyTemperature = 36.5f,
            height = 182f,
            weight = 92f,
            bodyMassIndex = 25.2f,
        )
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MAINVIEWMODEL", "onCleared: $this")
        sognoraWebSocket.close()
        sognoraAudioRecorder.stopAudioRecorder()
    }
}