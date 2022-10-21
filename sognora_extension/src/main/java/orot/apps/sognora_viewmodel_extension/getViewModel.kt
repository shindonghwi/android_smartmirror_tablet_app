package orot.apps.sognora_viewmodel_extension

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel


val vms = ViewModelStore()

@Composable
inline fun <reified type : ViewModel>getViewModel(key: String, vm: type) =
    viewModel<type>(
        key = key,
        viewModelStoreOwner = { vms },
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return vm as T
            }
        })