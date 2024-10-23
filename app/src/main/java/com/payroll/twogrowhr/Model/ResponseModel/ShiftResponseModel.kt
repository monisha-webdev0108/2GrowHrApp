package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class ShiftResponseModel(
    @SerializedName("success" ) var success: Boolean,
    @SerializedName("Data"    ) var data: List<ShiftData>
):Parcelable



@Keep
@Parcelize
data class ShiftData (

    @SerializedName("id"           ) var id         : String,
    @SerializedName("name"         ) var name       : String,
    @SerializedName("Sft_STime"    ) var shiftStartTime  : String,
    @SerializedName("sft_ETime"    ) var shiftEndTime : String,
    @SerializedName("ACutOff"      ) var aCutOff    : String,
    @SerializedName("NtfyTm"       ) var nyFyTm     : String,

    ):Parcelable