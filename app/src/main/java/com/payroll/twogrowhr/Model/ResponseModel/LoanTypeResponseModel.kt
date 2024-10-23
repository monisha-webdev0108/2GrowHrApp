package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize




@Keep
@Parcelize
data class LoanTypeResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val data: List<LoanTypeData>
) : Parcelable

@Keep
@Parcelize
data class LoanTypeData(
    @SerializedName("ruleName")
    var ruleName: String,

    @SerializedName("slNo")
    var slNo: Int,

    @SerializedName("interestRate")
    var interestRate: Double,

    @SerializedName("interestRateType")
    var interestRateType: Int

    ) : Parcelable