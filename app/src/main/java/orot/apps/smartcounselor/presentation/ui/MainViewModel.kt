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
import mago.apps.sognoraaudio.google_tts.OrotTTS
import mago.apps.sognorawebsocket.websocket.model.ChatData
import mago.apps.sognorawebsocket.websocket.model.OROT_PROTOCOL
import mago.apps.sognorawebsocket.websocket.model.ServerState
import mago.apps.sognorawebsocket.websocket.model.callback.IActionCallback
import mago.apps.sognorawebsocket.websocket.model.callback.OrotActionType
import mago.apps.sognorawebsocket.websocket.model.protocol.MessageProtocol
import mago.apps.sognorawebsocket.websocket.model.protocol.body.BodyInfo
import mago.apps.sognorawebsocket.websocket.model.protocol.body.RequestedMeasurementInfo
import mago.apps.sognorawebsocket.websocket.model.protocol.header.HeaderInfo
import mago.apps.sognorawebsocket.websocket.new_ws.OrotWebSocket
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
    val sognoraAudioRecorder: SognoraAudioRecorder,
) : ViewModel() {
    lateinit var navigationKit: NavigationKit

    var orotWebSocket: OrotWebSocket? = null
    val orotServerURL: String = "ws://demo-health-stream.mago52.com/ws/chat"
    var orotTTS: OrotTTS? = OrotTTS()

    val displayText = MutableStateFlow<String?>("")
    var resultActionType: OrotActionType = OrotActionType.IDLE
    fun updateDisplayText(msg: String? = "") = displayText.update { msg }

    init {
        orotWebSocket = OrotWebSocket().apply {
            setActionCallback(
                object : IActionCallback {

                    /** 서버 오픈 수신 */
                    override fun onStartDialog(
                        actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        startDialog()
                    }

                    /** AI의 첫 대화 시작 */
                    override fun onStartConversation(
                        voiceComment: String?, displayComment: String?, actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        playTts(voiceComment)
                        updateDisplayText(displayComment)
                    }

                    override fun onDoctorCall(voiceComment: String?, actionType: OrotActionType) {
                        resultActionType = actionType
                        playTts(voiceComment)
                    }

                    override fun onMeasurementEnd(
                        actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        changeSaidMeText("")
                    }

                    override fun onConversationExit(
                        voiceComment: String?, displayComment: String?, actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        playTts(voiceComment)
                        updateDisplayText(displayComment)
                    }

                    override fun onConversationEnd(
                        voiceComment: String?, displayComment: String?, actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        playTts(voiceComment)
                        updateDisplayText(displayComment)
                    }

                    override fun onReceivedFallback(
                        voiceComment: String?, displayComment: String?, actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        playTts(voiceComment)
                        updateDisplayText(displayComment)
                    }

                    override fun onReceivedDeviceWatchData(measurement: RequestedMeasurementInfo?) {
                        setWatchData(measurement)
                    }

                    override fun onReceivedDeviceChairData(measurement: RequestedMeasurementInfo?) {
                        setChairData(measurement)
                    }

                    override fun onReceivedSaidMe(
                        displayComment: String?, actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        receivedSaidMe(displayComment)
                    }

                    override fun onReceivedSaidAI(
                        voiceComment: String?, displayComment: String?, actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        voiceComment?.let { playTts(it) }
                        updateDisplayText(displayComment)
                    }

                    override fun moveMeasurement(
                        voiceComment: String?, displayComment: String?, actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        voiceComment?.let { playTts(it) }
                        updateDisplayText(displayComment)
                    }

                    override fun showHealthOverView(
                        voiceComment: String?, actionType: OrotActionType
                    ) {
                        resultActionType = actionType
                        playTts(voiceComment)
                        changeSaidMeText("")
                        moveScreen(null, BottomMenu.Conversation)
                        changeRecommendationBottomSheetFlag(true)
                    }

                    override fun saveChatMessage(msg: String, isUser: Boolean) {
                        saveChatData(msg, isUser)
                    }
                },
            )
        }
    }

    /** 다이얼로그 시작 */
    private fun startDialog() {
        moveScreen(Screens.Conversation, BottomMenu.Conversation)
        startAudioRecorder()
        sendProtocol(1)
    }

    /** 내가 말한 내용을 수신 했을때 */
    private fun receivedSaidMe(msg: String?) {
        pauseTts()
        changeMicState(false)
        changeSaidMeText(msg.toString())
    }

    private fun saveChatData(msg: String, isUser: Boolean) {
        chatList.add(ChatData(msg, isUser))
    }

    fun startWaitingResult() {
        val msg = "헬스케어 결과를 불러오는중입니다\n잠시만 기다려주세요"
        resultActionType = OrotActionType.WAITING_RESULT
        playTts(msg)
        updateDisplayText(msg)
        moveScreen(Screens.Conversation, BottomMenu.Loading)
    }

    /** 권고사항 화면 노출 후 이어서 진행하기 **/
    fun proceedAfterMeasurement() {
        val msg = "건강검진 결과는 어떠셨나요?"
        pauseTts()
        changeRecommendationBottomSheetFlag(false)
        changeMicState(true)
        updateDisplayText(msg)
    }

    private fun setWatchData(measurement: RequestedMeasurementInfo?) {
        val bloodPressureSystolic = measurement?.bloodPressureSystolic ?: 0
        val heartRate = measurement?.heartRate ?: 0

        selectedUser = selectedUser?.copy(
            bloodPressureSystolic = bloodPressureSystolic, heartRate = heartRate
        )

        watchHashData["bloodPressureSystolic"] = bloodPressureSystolic
        watchHashData["heartRate"] = heartRate

        medicalDeviceWatchData.update {
            WatchData(
                bloodPressureSystolic = bloodPressureSystolic, heartRate = heartRate
            )
        }
    }

    private fun setChairData(measurement: RequestedMeasurementInfo?) {
        val bloodPressureSystolic = measurement?.bloodPressureSystolic ?: 0
        val glucose = measurement?.glucose ?: 0
        val weight = measurement?.weight ?: 0f
        val bodyMassIndex = measurement?.bodyMassIndex ?: 0f

        selectedUser = selectedUser?.copy(
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

    fun connectWebSocket() {
        orotWebSocket?.initWebSocket(url = orotServerURL)
    }

    var isAvailableAudioBuffer = mutableStateOf(false)
    fun updateAudioBufferStatus(flag: Boolean) {
        isAvailableAudioBuffer.value = flag
    }

    fun startAudioRecorder() {
        sognoraAudioRecorder.startAudioRecorder()
        onIO {
            try {
                do {
                    if (isAvailableAudioBuffer.value) {
                        val bufferSize = sognoraAudioRecorder.getMinBuffer()
                        val bufferInfo = sognoraAudioRecorder.frameBuffer()
                        bufferInfo.second?.let {
                            if (it < -1) return@let
                            orotWebSocket?.sendBuffer(bufferInfo.first, bufferSize)
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

    var selectedUser: UserData? = null

    fun addUser(userInfo: UserData) {
        if (!userList.contains(userInfo)) {
            userList.add(userInfo)
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
    fun getServerState() = orotWebSocket?.getServerState()

    /** 웹 소켓으로 이벤트 전달하기 */
    fun sendProtocol(protocolNum: Int, measurementBody: BodyInfo? = null) {
        var protocolId = ""

        when (protocolNum) {
            1 -> protocolId = OROT_PROTOCOL.PROTOCOL_1.id
            2 -> protocolId = OROT_PROTOCOL.PROTOCOL_2.id
            3 -> protocolId = OROT_PROTOCOL.PROTOCOL_3.id
            4 -> protocolId = OROT_PROTOCOL.PROTOCOL_4.id
            5 -> protocolId = OROT_PROTOCOL.PROTOCOL_5.id
            6 -> protocolId = OROT_PROTOCOL.PROTOCOL_6.id
            7 -> protocolId = OROT_PROTOCOL.PROTOCOL_7.id
            8 -> protocolId = OROT_PROTOCOL.PROTOCOL_8.id
            9 -> protocolId = OROT_PROTOCOL.PROTOCOL_9.id
            10 -> protocolId = OROT_PROTOCOL.PROTOCOL_10.id
            11 -> protocolId = OROT_PROTOCOL.PROTOCOL_11.id
            12 -> protocolId = OROT_PROTOCOL.PROTOCOL_12.id
            13 -> protocolId = OROT_PROTOCOL.PROTOCOL_13.id
            14 -> protocolId = OROT_PROTOCOL.PROTOCOL_14.id
            15 -> protocolId = OROT_PROTOCOL.PROTOCOL_15.id
            16 -> protocolId = OROT_PROTOCOL.PROTOCOL_16.id
            17 -> protocolId = OROT_PROTOCOL.PROTOCOL_17.id
            18 -> protocolId = OROT_PROTOCOL.PROTOCOL_18.id
            19 -> protocolId = OROT_PROTOCOL.PROTOCOL_19.id
            20 -> protocolId = OROT_PROTOCOL.PROTOCOL_20.id
            21 -> protocolId = OROT_PROTOCOL.PROTOCOL_21.id
            99 -> protocolId = OROT_PROTOCOL.PROTOCOL_99.id
        }

        val header: HeaderInfo
        val newBody: BodyInfo?

        selectedUser?.let {
            header = HeaderInfo().toStream(
                type = protocolId, age = it.age, gender = it.gender
            )

            newBody = if (measurementBody != null) {
                BodyInfo(
                    before = orotWebSocket?.getLastInfo()?.body, measurement = measurementBody.measurement
                )
            } else {
                BodyInfo(before = orotWebSocket?.getLastInfo()?.body)
            }

            val newResMsg = MessageProtocol(
                header = header, body = if (protocolNum <= 2) null else newBody
            )
            val sendingData = Gson().toJson(newResMsg)
            Log.w(TAG, "sendProtocol: protocol: $protocolId / body: $sendingData")
            orotWebSocket?.sendMsg(sendingData)
        }
    }

    fun changeMicState(flag: Boolean) {
        ttsIsSpeaking.value = !flag
        conversationVisibleState.targetState = !flag
        if (getServerState()?.value is ServerState.Connected) {
            updateAudioBufferStatus(flag)
            if (flag) {
                sendProtocol(3)
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

    var conversationVisibleState = MutableTransitionState(false)
    val ttsIsSpeaking = mutableStateOf(false) // TTS 말하는중 여부
    fun changeTtsStatus(flag: Boolean) {
        ttsIsSpeaking.value = flag
    }

    fun initTTS(context: Context) {
        orotTTS?.createTts(context, object : UtteranceProgressListener() {
            override fun onStart(p0: String?) {
                changeMicState(false)
            }

            override fun onDone(p0: String?) {
                when (resultActionType) {
                    OrotActionType.GUIDE -> {
                        changeMicState(true)
                    }
                    OrotActionType.SAID_AI -> {
                        changeMicState(true)
                    }
                    OrotActionType.MEASUREMENT_START -> {
                        changeTtsStatus(false)
                        changeChatViewShowing(false)
                        moveScreen(null, BottomMenu.BloodPressure)
                    }
                    OrotActionType.WAITING_RESULT -> {

                        selectedUser?.let {
                            sendProtocol(
                                protocolNum = 18,
                                measurementBody = orotWebSocket?.getLastInfo()?.body?.toMeasurement(
                                    beforeBody = orotWebSocket?.getLastInfo()?.body,
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
                                ),
                            )
                        }
                    }
                    OrotActionType.CONVERSATION_END -> {
                        changeMicState(true)
                    }
                    OrotActionType.CONVERSATION_EXIT -> {
                        sendProtocol(20)
                        changeTtsStatus(false)
                        onDefault {
                            moveScreen(bottomMenu = BottomMenu.Loading)
                            delay(2000)
                            moveScreen(bottomMenu = BottomMenu.RetryAndChat)
                        }
                    }
                    OrotActionType.FALL_BACK -> {
                        changeMicState(true)
                    }
                    else -> {}
                }
            }

            override fun onError(p0: String?) {
                changeTtsStatus(false)
            }

        })
    }

    private fun playTts(msg: String?) {
        changeTtsStatus(false)
        orotTTS?.start(msg)
    }

    fun pauseTts() {
        changeTtsStatus(false)
        orotTTS?.pause()
    }

    private fun clearTts() {
        changeTtsStatus(false)
        orotTTS?.clear()
    }


    fun reset() {
        clearConversationData()
        clearUserInputData()
        clearWebSocketAudio()
        moveScreen(Screens.Home, BottomMenu.Start)
    }

    private fun clearMedicationDeviceData() {
        updateMedicalEndStatus(false)
        watchHashData.entries.map { watchHashData[it.key] = 0 }
        chairHashData.entries.map { chairHashData[it.key] = 0 }
        medicalDeviceWatchData.update { WatchData(0, 0) }
        medicalDeviceChairData.update { ChairData(0, 0, 0f, 0f) }
    }

    private fun clearConversationData() {
        chatList.clear()
        clearMedicationDeviceData()
        changeSaidMeText("")
        updateDisplayText("")
    }

    private fun clearWebSocketAudio() {
        pauseTts()
        changeMicState(false)
        orotWebSocket?.close()
        sognoraAudioRecorder.stopAudioRecorder()
    }

    private fun clearUserInputData() {
        userList.filterIndexed { index, t -> index > 4 }.map { userList.remove(it) }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MAINVIEWMODEL", "onCleared: $this")
        clearTts()
        orotTTS = null
        orotWebSocket?.close()
        orotWebSocket = null
        sognoraAudioRecorder.stopAudioRecorder()
    }
}