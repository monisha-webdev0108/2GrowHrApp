package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



@Keep
@Parcelize
data class AppliedLoanInstalmentDetailResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Data")
    var data: List<AppliedLoanInstalmentData>

): Parcelable


@Keep
@Parcelize
data class AppliedLoanInstalmentData (

    @SerializedName("monthName")
    val monthName : String,

    @SerializedName("Amount")
    val amount : String,

    @SerializedName("month")
    val month : String,

    @SerializedName("year")
    val year : String,

    ): Parcelable