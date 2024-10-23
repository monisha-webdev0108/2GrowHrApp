package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoanInstallmentResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val data: List<LoanInstallmentData>
) : Parcelable

@Keep
@Parcelize
data class LoanInstallmentData(

    @SerializedName("monthName")
    var monthName: String,

    @SerializedName("Amount")
    var installmentAmount: Double,

    @SerializedName("month")
    var month: String,

    @SerializedName("year")
    var year: Int,

) : Parcelable