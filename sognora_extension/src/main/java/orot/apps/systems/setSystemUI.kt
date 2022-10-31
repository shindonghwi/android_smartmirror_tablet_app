package orot.apps.systems

import android.graphics.Color
import android.view.View
import androidx.activity.ComponentActivity

fun ComponentActivity.hideSystemUI() {
    // <item name="android:windowTranslucentNavigation">true</item>
    val decorView: View = window.decorView
    var uiVisibility = decorView.systemUiVisibility
//    uiVisibility = uiVisibility or View.SYSTEM_UI_FLAG_LOW_PROFILE
//    uiVisibility = uiVisibility or View.SYSTEM_UI_FLAG_FULLSCREEN
    uiVisibility = uiVisibility or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    uiVisibility = uiVisibility or View.SYSTEM_UI_FLAG_IMMERSIVE
    uiVisibility = uiVisibility or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    window.statusBarColor = Color.TRANSPARENT
    decorView.systemUiVisibility = uiVisibility
}

fun ComponentActivity.showSystemUI() {
    val decorView: View = window.decorView
    var uiVisibility = decorView.systemUiVisibility
//    uiVisibility = uiVisibility and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
//    uiVisibility = uiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
    uiVisibility = uiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
    uiVisibility = uiVisibility and View.SYSTEM_UI_FLAG_IMMERSIVE.inv()
    uiVisibility = uiVisibility and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
    decorView.systemUiVisibility = uiVisibility
}
