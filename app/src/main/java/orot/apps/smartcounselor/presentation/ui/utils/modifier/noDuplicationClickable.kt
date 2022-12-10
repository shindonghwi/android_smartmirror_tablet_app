package orot.apps.smartcounselor.presentation.ui.utils.modifier

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import orot.apps.smartcounselor.presentation.ui.utils.viewmodel.scope.coroutineScopeOnMain

/** 중복클릭 방지 */
fun Modifier.noDuplicationClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    hasRipple: Boolean = true,
    isTopBar: Boolean = false,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {

    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = {
            ClickableCheckObject.throttledFirstClicks {
                onClick.invoke()
            }
        },
        role = role,
        indication = if (hasRipple) {
            if (!isTopBar) LocalIndication.current else rememberRipple(
                bounded = false,
                radius = 16.dp
            )
        } else null,
        interactionSource = remember { MutableInteractionSource() }
    )
}


object ClickableCheckObject {

    private val throttledState = MutableStateFlow { }

    fun throttledFirstClicks(onClick: () -> Unit) {
        throttledState.value = onClick
    }

    init {
        coroutineScopeOnMain {
            throttledState
                .throttleFirst(300)
                .collect { onClick: () -> Unit ->
                    onClick.invoke()
                }
        }
    }

    // throttle first for coroutine
    private fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
        require(periodMillis > 0) { "period should be positive" }
        return flow {
            var lastTime = 0L
            collect { value ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTime >= periodMillis) {
                    lastTime = currentTime
                    emit(value)
                }
            }
        }
    }
}
