package orot.apps.sognora_viewmodel_extension.scope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun coroutineScopeOnMain(
    crossinline body: suspend CoroutineScope.() -> Unit
) = CoroutineScope(Dispatchers.Main).launch {
    body()
}

inline fun coroutineScopeOnIO(
    crossinline body: suspend CoroutineScope.() -> Unit
) = CoroutineScope(Dispatchers.IO).launch {
    body()
}

inline fun coroutineScopeOnDefault(
    crossinline body: suspend CoroutineScope.() -> Unit
) = CoroutineScope(Dispatchers.Default).launch {
    body()
}