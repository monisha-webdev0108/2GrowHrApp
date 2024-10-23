package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ApprovalListResponseModel(

    @SerializedName("success" ) val success: Boolean,
    @SerializedName("Head"    ) val Head: List<ApprovalList>

): Parcelable

@Keep
@Parcelize
data class ApprovalList (
    @SerializedName("LeaveCount")
    val leaveCount: Int,

    @SerializedName("WFHCount")
    val wfhCount: Int,

    @SerializedName("ODCount")
    val odCount: Int,

    @SerializedName("RegularizeCount")
    val regularizeCount: Int,

    @SerializedName("Shiftcnt")
    val shiftCount: Int,

    @SerializedName("Compoffcnt")
    val compOffCount: Int,

    @SerializedName("Loancount")
    val loanCount: Int,

): Parcelable