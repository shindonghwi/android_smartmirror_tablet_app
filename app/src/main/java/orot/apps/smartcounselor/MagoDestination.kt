package orot.apps.smartcounselor

sealed class Screens(val route: String) {
    object Home: Screens("home")
    object Guide: Screens("guide")
    object Conversation: Screens("conversation")
    object BloodPressure: Screens("bloodPressure")
    object ChatList: Screens("chatList")
}
