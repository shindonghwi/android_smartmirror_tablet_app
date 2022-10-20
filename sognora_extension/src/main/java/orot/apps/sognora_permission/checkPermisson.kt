package orot.apps.sognora_permission

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun CheckPermission(permission: String, requestCode: Int) {
    (LocalContext.current as? Activity)?.let { checkPermission(permission, requestCode, it) }
}

private fun checkPermission(permission: String, requestCode: Int, activity: Activity) {
    if (ContextCompat.checkSelfPermission(
            activity, permission
        ) == PackageManager.PERMISSION_DENIED
    ) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    } else {
        Toast.makeText(activity, "Permission already granted..", Toast.LENGTH_SHORT).show()
    }
}
