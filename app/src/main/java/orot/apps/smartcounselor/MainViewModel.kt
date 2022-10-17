package orot.apps.smartcounselor

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import orot.apps.sognora_viewmodel_extension.scope.onDefault
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val currentTime: MutableStateFlow<String> = MutableStateFlow(getCurrentTime())

    init {
        setCurrentTime()
    }

    private fun setCurrentTime() = onDefault {
        while (true) {
            currentTime.emit(getCurrentTime())
            delay(1000L)
        }
    }

    private fun getCurrentTime() = SimpleDateFormat("yyyy.MM.dd-HH:mm-EE", Locale.KOREA).format(Date())


}
