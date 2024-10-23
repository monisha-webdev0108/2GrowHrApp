package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import androidx.annotation.Keep

@Parcelize
@Keep
data class CheckInOutResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val head: List<CheckInData>
) : Parcelable

@Parcelize
data class CheckInData(
    @SerializedName("flag")
    val flag: Int,

    @SerializedName("cnt")
    val CheckInCount: Int,

    @SerializedName("Mode")
    val orgMode: Int

) : Parcelable