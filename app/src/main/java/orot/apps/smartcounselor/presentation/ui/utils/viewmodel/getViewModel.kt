package orot.apps.smartcounselor.presentation.ui.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

val vmso = ViewModelStoreOwner()

inline fun <reified type : ViewModel> getViewModel(
    vm: type,
    optionKey: Int? = 0,
): type {
    return ViewModelProvider(
        owner = { vmso.getWithNoCount(optionKey.toString()) },
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return vm as T
            }
        })[type::class.java]
}

fun clearAndNewVMS() {
    vmso.clearAll()
}
