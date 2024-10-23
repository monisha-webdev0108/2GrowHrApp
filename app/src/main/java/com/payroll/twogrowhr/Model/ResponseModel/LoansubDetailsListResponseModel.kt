package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoansubDetailsListResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Detail")
    val Detail: List<LoanSubDetailsData>
) : Parcelable

@Keep
@Parcelize
data class LoanSubDetailsData(
    @SerializedName("Instalment_Amount")
    var instalment_Amount: Int,

    @SerializedName("Months")
    var months: String,

    @SerializedName("Status")
    var status: String,

    @SerializedName("Sl_No")
    var sl_No: Int,

) : Parcelable