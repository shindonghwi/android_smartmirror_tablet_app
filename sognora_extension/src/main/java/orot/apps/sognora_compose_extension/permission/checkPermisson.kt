package orot.apps.sognora_compose_extension.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun CheckPermission(permission: String, requestCode: Int, denyMessage: String? = null) {
    (LocalContext.current as? Activity)?.let { checkPermission(permission, requestCode, it, denyMessage) }
}

private fun checkPermission(permission: String, requestCode: Int, activity: Activity, denyMessage: String? = null) {
    if (ContextCompat.checkSelfPermission(
            activity, permission
        ) == PackageManager.PERMISSION_DENIED
    ) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    } else {
        denyMessage?.let { Toast.makeText(activity, it, Toast.LENGTH_SHORT).show() }
    }
}
