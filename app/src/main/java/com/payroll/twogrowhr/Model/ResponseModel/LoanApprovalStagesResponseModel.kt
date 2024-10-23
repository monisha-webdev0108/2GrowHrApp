package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoanApprovalStagesResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Data")
    val data: List<LoanApprovalStagesData>
) : Parcelable

@Keep
@Parcelize
data class LoanApprovalStagesData(

    @SerializedName("loanApprovalDate")
    var loanApprovalDate: String,

    @SerializedName("loanApprovalTime")
    var loanApprovalTime: String,

    @SerializedName("loanApprovalStatus")
    var loanApprovalStatus: String,

    @SerializedName("loanAppEmpName")
    var loanAppEmpName: String,

    @SerializedName("status")
    var status: String

    ) : Parcelable