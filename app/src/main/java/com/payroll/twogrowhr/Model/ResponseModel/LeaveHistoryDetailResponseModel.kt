package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class LeaveHistoryDetailResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("data")
    var data: List<LeaveHistoryDetailData>

): Parcelable


@Keep
@Parcelize
data class LeaveHistoryDetailData (

    @SerializedName("sl_no")
    val slNo : String,

    @SerializedName("emp_id")
    val empId : String,

    @SerializedName("emp_code")
    val empCode : String,

    @SerializedName("Leave_TypeName")
    val leaveTypeName : String,

    @SerializedName("from_date" )
    val fromDate : String,

    @SerializedName("status")
    val status : String,

    @SerializedName("App_Rej_by" )
    val actionTakenBy : String,

    @SerializedName("App_by" )
    val approvedBy : String,

    @SerializedName("Rejected_by" )
    val rejectedBy : String,

    @SerializedName("Rej_Reason" )
    val reason : String,

    @SerializedName("app_rej_dt")
    val actionTakenDate : String,

    @SerializedName("Next_level_Approve" )
    val nextLevel : String,

    @SerializedName("daytyp" )
    val dayType : String

    ): Parcelable