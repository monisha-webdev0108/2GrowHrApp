package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoanEligibleResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val data: List<LoanEligibilityData>
) : Parcelable

@Keep
@Parcelize
data class LoanEligibilityData(

    @SerializedName("Count")
    var count: String,

    @SerializedName("Salary")
    var salary: String,

    @SerializedName("DOJ")
    var doj: String

) : Parcelable