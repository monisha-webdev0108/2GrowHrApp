package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class LeaveTypeDetailResponseModel(

        @SerializedName("success" )
        val success: Boolean,

        @SerializedName("Data"    )
        val Data: List<LeaveTypeDetailData>

):Parcelable



@Keep
@Parcelize
data class LeaveTypeDetailData(

    @SerializedName("id" )
    val id: Int = 0,

    @SerializedName("name" )
    val name: String = "",

    @SerializedName("Leave_SName" )
    val leaveName: String = "",

    @SerializedName("Balance" )
    val balance: Double = 0.0,

    @SerializedName("COffType" )
    val coffType: Int = 0,

    @SerializedName("LeaveType" )
    val leaveType: String = "",

    @SerializedName("Durallow" )
    val durationAllow: String = "",

    @SerializedName("IsFileEnabled" )
    val isFileEnabled: String = "",

    @SerializedName("FileUnits" )
    val fileUnits: String = ""

): Parcelable



@Keep
@Parcelize
data class LeaveDurationResponseModel(
    @SerializedName("id" )
    val id: String = "",

    @SerializedName("name" )
    val name: String = "",

    @SerializedName("des" )
    val des: String = ""
): Parcelable