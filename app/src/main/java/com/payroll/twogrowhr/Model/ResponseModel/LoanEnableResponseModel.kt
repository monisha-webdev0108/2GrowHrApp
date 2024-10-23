package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoanEnableResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val data: List<LoanEnableData>
) : Parcelable

@Keep
@Parcelize
data class LoanEnableData(

    @SerializedName("flag")
    var flag: String

) : Parcelable