package orot.apps.smartcounselor.presentation.ui

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
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
import orot.apps.smartcounselor.model.local.*
import orot.apps.smartcounselor.model.remote.*
import orot.apps.smartcounselor.presentation.ui.MagoActivity.Companion.TAG
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
                    Log.w(TAG, "${conversationInfo.value.first} | onMessageText: $msg")

                    try {
                        val receivedMsg: MessageProtocol = Gson().fromJson(msg, MessageProtocol::class.java)

                        val protocol = receivedMsg.header.protocol_id
                        val device = receivedMsg.header.device
                        val voice = receivedMsg.body?.voice
                        val display = receivedMsg.body?.display
                        val measurement = receivedMsg.body?.measurement

                        if (protocol != MAGO_PROTOCOL.PROTOCOL_17.id) {
                            receivedMsg.body?.let {
                                beforeBody = receivedMsg.body
                            }
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

                                val type = when (receivedMsg.body?.action) {
                                    "measurement" -> ActionType.MEASUREMENT
                                    "end" -> ActionType.END
                                    "doctorcall" -> ActionType.DOCTORCALL
                                    "exit" -> ActionType.EXIT
                                    else -> ActionType.CONVERSATION
                                }

                                voice?.let { voiceInfo ->
                                    playGoogleTts(voiceInfo.text)
                                    if (ActionType.DOCTORCALL != type) {
                                        changeConversationList(type, voiceInfo.text, receivedMsg)
                                    }
                                }

                                if (display?.measurement != null) {
                                    changeSaidMeText("")
                                    moveScreen(null, BottomMenu.Conversation)
                                    changeRecommendationBottomSheetFlag(true)
                                }else{
                                    sendProtocol(13, conversationInfo.value.third)
                                }
                            }
                            /** Watch, Chair 데이터 도착 */
                            MAGO_PROTOCOL.PROTOCOL_17.id -> {
                                when (device) {
                                    "Watch" -> {
                                        Log.w(TAG, "onMessageText: WATCH DATA")
                                        val bloodPressureSystolic = measurement?.bloodPressureSystolic ?: 0
                                        val heartRate = measurement?.heartRate ?: 0

                                        userInputData = userInputData?.copy(
                                            bloodPressureSystolic = bloodPressureSystolic,
                                            heartRate = heartRate
                                        )
                                        watchHashData["bloodPressureSystolic"] = bloodPressureSystolic
                                        watchHashData["heartRate"] = heartRate

                                        medicalDeviceWatchData.update {
                                            WatchData(
                                                bloodPressureSystolic = bloodPressureSystolic,
                                                heartRate = heartRate
                                            )
                                        }
                                    }
                                    "Chair" -> {
                                        Log.w(TAG, "onMessageText: Chair DATA")

                                        val bloodPressureSystolic = measurement?.bloodPressureSystolic ?: 0
                                        val glucose = measurement?.glucose ?: 0
                                        val weight = measurement?.weight ?: 0f
                                        val bodyMassIndex = measurement?.bodyMassIndex ?: 0f

                                        userInputData = userInputData?.copy(
                                            bloodPressureSystolic = bloodPressureSystolic,
                                            glucose = glucose,
                                            weight = weight,
                                            bodyMassIndex = bodyMassIndex
                                        )
                                        chairHashData["bloodPressureSystolic"] = bloodPressureSystolic
                                        chairHashData["glucose"] = glucose
                                        chairHashData["weight"] = weight.toInt()
                                        chairHashData["bodyMassIndex"] = bodyMassIndex.toInt()

                                        medicalDeviceChairData.update {
                                            ChairData(
                                                bloodPressureSystolic = bloodPressureSystolic,
                                                glucose = glucose,
                                                weight = weight,
                                                bodyMassIndex = bodyMassIndex
                                            )
                                        }
                                    }
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
            initWebSocket("ws://demo-health-stream.mago52.com/ws/chat")
        }
    }

    /** 권고사항 화면 노출 후 이어서 진행하기 **/
    fun proceedAfterMeasurement() {
        pauseGoogleTts()
        changeRecommendationBottomSheetFlag(false)
        changeSendingStateAudioBuffer(true)
        conversationInfo.value.let {
            changeConversationList(it.first, "건강검진 결과는 어떠셨나요?", it.third)
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

    var userInputData: UserInputData? = UserInputData(
        medication = listOf("htn"),
        glucose = 105,
        bodyTemperature = 36.5f,
        height = 182f,
        weight = 92f,
        bodyMassIndex = 25.2f,
    )

    var watchHashData = HashMap<String, Int>().apply {
        put("bloodPressureSystolic", 0)
        put("heartRate", 0)
    }
    var chairHashData = HashMap<String, Int>().apply {
        put("bloodPressureSystolic", 0)
        put("glucose", 0)
        put("weight", 0)
        put("bodyMassIndex", 0)
    }

    val isEndMedicalMeasurement: MutableStateFlow<Boolean> = MutableStateFlow(false)
    fun updateMedicalEndStatus(flag: Boolean) {
        isEndMedicalMeasurement.update { flag }
    }

    var medicalDeviceWatchData: MutableStateFlow<WatchData> = MutableStateFlow<WatchData>(
        WatchData(0, 0)
    )
    var medicalDeviceChairData: MutableStateFlow<ChairData> = MutableStateFlow<ChairData>(
        ChairData(0, 0, 0f, 0f)
    )

    var tempRecommendationMent: ArrayList<RecommendationMent> = arrayListOf()
    var beforeBody: BodyInfo? = null

    val userListInfo = mutableStateListOf<String>().apply {
        repeat((1..5).count()) { add("user$it") }
    }

    var addAccountInputData = ""

    fun addUser(userInfo: String) {
        if (!userListInfo.contains(userInfo)) {
            userListInfo.add(userInfo)
        }
    }

    var isShowingAccountBottomSheet = MutableStateFlow(false)
    fun changeAccountBottomSheetFlag(flag: Boolean) {
        isShowingAccountBottomSheet.update { flag }
    }

    var isShowingRecommendationBottomSheet = MutableStateFlow(false)
    fun changeRecommendationBottomSheetFlag(flag: Boolean) {
        isShowingRecommendationBottomSheet.update { flag }
    }

    /**
     * ================================================
     *     UI STATE
     * ================================================
     * */
    val currentBottomMenu = mutableStateOf(BottomMenu.Start.type) // 바텀 메뉴

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
                type = protocolId, age = it.age, gender = it.gender
            )

            newBody = if (protocolId == MAGO_PROTOCOL.PROTOCOL_18.id) { // 측정된 값은 measurementInfo 정보로 넘긴다.
                conversationInfo.value.let {
                    changeConversationList(ActionType.IDLE, it.second, it.third)
                }
                body?.body?.toMeasurement(
                    beforeBody = beforeBody, measurement = body.body.measurement
                )
            } else {
                body?.body?.toMeasurement(
                    beforeBody = beforeBody, measurement = null
                )
            }


            val newResMsg = MessageProtocol(
                header = header, body = if (protocolNum <= 2) null else newBody
            )
            val sendingData = Gson().toJson(newResMsg)
            Log.w(TAG, "${conversationInfo.value.first} | sendProtocol: protocol: $protocolId / body: $sendingData")
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
    val isChatViewShowing = MutableStateFlow(true)
    fun changeChatViewShowing(flag: Boolean) = isChatViewShowing.update { flag }
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
                                    changeChatViewShowing(false)
                                    moveScreen(null, BottomMenu.BloodPressure)
                                }

                                ActionType.RESULT_WAITING -> {

                                    val header: HeaderInfo
                                    val newBody: BodyInfo?

                                    userInputData?.let {
                                        header = HeaderInfo().toStream(
                                            type = MAGO_PROTOCOL.PROTOCOL_18.id, age = it.age, gender = it.gender
                                        )

                                        newBody = BodyInfo().toMeasurement(
                                            beforeBody = beforeBody,
                                            measurement = RequestedMeasurementInfo(
                                                medication = it.medication,
                                                bloodPressureSystolic = it.bloodPressureSystolic,
                                                bloodPressureDiastolic = it.bloodPressureDiastolic,
                                                glucose = it.glucose,
                                                heartRate = it.heartRate,
                                                bodyTemperature = it.bodyTemperature,
                                                height = it.height,
                                                weight = it.weight,
                                                bodyMassIndex = it.bodyMassIndex,
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
                                ActionType.IDLE,
                                ActionType.DOCTORCALL -> {}
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

    fun pauseGoogleTts(){
        tts?.run {
            ttsIsSpeaking.value = false
            stop()
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
        type: ActionType, content: String, msgResponse: MessageProtocol?, isFallback: Boolean = false
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