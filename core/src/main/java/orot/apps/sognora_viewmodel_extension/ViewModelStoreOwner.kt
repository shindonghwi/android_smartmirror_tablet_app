package orot.apps.sognora_viewmodel_extension


import androidx.lifecycle.ViewModelStore
import java.util.*

class ViewModelStoreOwner {

    val mMap = HashMap<String, Pair<ViewModelStore, Int>>()

    operator fun get(key: String): ViewModelStore {
        if (mMap[key] == null) {
            mMap[key] = Pair(ViewModelStore(), 1)
        } else {
            mMap[key] = Pair(mMap[key]!!.first, mMap[key]!!.second + 1)
        }
        return mMap[key]!!.first
    }

    fun getWithNoCount(key: String): ViewModelStore {
        if (mMap[key] == null) {
            mMap[key] = Pair(ViewModelStore(), 0)
        }
        return mMap[key]!!.first
    }

    fun keys(): Set<String> {
        return HashSet(mMap.keys)
    }

    fun clear(key: String) {
        mMap[key]?.let {
            if (it.second <= 1) {
                it.first.clear()
                mMap.remove(key)
            } else {
                mMap[key] = Pair(it.first, it.second - 1)
            }
        }
    }

    /**
     * Clears internal storage and notifies ViewModels that they are no longer used.
     */
    fun clearAll() {
        for (vm in mMap.values) {
            vm.first.clear()
        }
        mMap.clear()
    }
}