package orot.apps.sognora_viewmodel_extension

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel


val vms = ViewModelStore()

@Composable
inline fun <reified type : ViewModel> getViewModel(
    vm: type,
    optionKey: Int? = 0,
): type {

    val vmName = vm.javaClass.simpleName
    val keys = remember { LinkedHashSet<String>() }
    keys.add("$vmName$optionKey")
    val findKey = keys.find { it == "$vmName$optionKey" }

    return viewModel(
        key = findKey,
        viewModelStoreOwner = { vms },
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return vm as T
            }
        })
}

fun clearAndNewVMS(){
    vms.clear()
}
