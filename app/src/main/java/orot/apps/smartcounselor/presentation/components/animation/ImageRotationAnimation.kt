package orot.apps.smartcounselor.presentation.components.animation
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun RotationAnimation(
    modifier: Modifier = Modifier,
    isPlaying: MutableState<Boolean> = mutableStateOf(false),
    iconDrawable: Int,
    iconSize: Dp
) {
    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isPlaying.value) {
        if (isPlaying.value) {
            rotation.animateTo(
                targetValue = currentRotation + 360f, animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart
                )
            ) {
                currentRotation = value
            }
        } else {
            if (currentRotation > 0f) {
                // Slow down rotation on pause
                rotation.animateTo(
                    targetValue = currentRotation + 50, animationSpec = tween(
                        durationMillis = 1250, easing = LinearOutSlowInEasing
                    )
                ) {
                    currentRotation = value
                }
            }
        }
    }
    Image(
        modifier = modifier.then(
            Modifier
                .size(iconSize)
                .rotate(rotation.value)
        ),
        painter = painterResource(id = iconDrawable),
        contentDescription = "icon"
    )
}