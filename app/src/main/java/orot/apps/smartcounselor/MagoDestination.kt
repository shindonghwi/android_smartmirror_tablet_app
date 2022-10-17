package orot.apps.smartcounselor

sealed class Screens(val route: String) {
    object Home: Screens("home")
    object Guide: Screens("guide")
}
