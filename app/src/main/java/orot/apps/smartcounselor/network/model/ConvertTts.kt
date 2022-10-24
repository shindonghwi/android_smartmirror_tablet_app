package orot.apps.smartcounselor.network.model

import com.google.gson.annotations.SerializedName

data class ConvertTtsEntity(
    @SerializedName("msg")
    val msg: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("counts")
    val counts: Int,
)
