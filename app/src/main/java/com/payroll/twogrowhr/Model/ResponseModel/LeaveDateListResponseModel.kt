package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class LeaveDateListResponseModel(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Head")
    var Head: List<LeaveDateListData>
): Parcelable


@Keep
@Parcelize
data class LeaveDateListData (

    @SerializedName("LevTyp")
    val leaveType  : String= "",

    @SerializedName("Weekname")
    val weekName : String= "",

    @SerializedName("Day")
    val day  : String= "",

    @SerializedName("Date")
    val date  : String= "",

    @SerializedName("LeaveName")
    val leaveName  : String = "",

    @SerializedName("Precount")
    val preCount  : String= "",

    @SerializedName("enable")
    val enableStatus  : String= "",

    @SerializedName("type")
    val type  : String= "",

    @SerializedName("LeaveCode")
    val leaveCode  : String= "",

    @SerializedName("bal")
    val leaveBalance  : String= "",

    ): Parcelable
