package orot.apps.smartcounselor.graph.model

sealed class Screens(val route: String) {
    object Home: Screens("home")
    object Guide: Screens("guide")
    object Conversation: Screens("conversation")
    object BloodPressure: Screens("bloodPressure")
    object ChatList: Screens("chatList")
    object ServerConnectionFailScreen: Screens("serverConnectionFail")
}

sealed class BottomMenu(val type: String) {
    object Start: BottomMenu("start")
    object Loading: BottomMenu("loading")
    object Empty: BottomMenu("empty")
    object Conversation: BottomMenu("conversation")
    object BloodPressure: BottomMenu("bloodPressure")
    object RetryAndChat: BottomMenu("retryAndChat")
    object Retry: BottomMenu("retry")
    object Call: BottomMenu("call")
    object ServerRetry: BottomMenu("serverRetry")
}
