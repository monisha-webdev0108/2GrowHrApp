package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



@Keep
@Parcelize
data class LeaveHistoryResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("data")
    var data: List<LeaveHistoryData>

): Parcelable


@Keep
@Parcelize
data class LeaveHistoryData (

    @SerializedName("Created_Date")
    val createdDate : String,

    @SerializedName("Reason")
    val reason : String,

    @SerializedName("Leave_Type")
    val leaveTypeName : String,

    @SerializedName("From_Date" )
    val fromDate : String,

    @SerializedName("LastUpdt_Date")
    val lastUpdateDate : String,

    @SerializedName("To_Date")
    val toDate : String,

    @SerializedName("No_of_Days" )
    val noOfDays : String,

    @SerializedName("LStatus" )
    val leaveStatus : String,

    @SerializedName("StusClr")
    val statusColor : String,

    @SerializedName("Rejected_Reason" )
    val rejectedReason : String,

    @SerializedName("Leave_Active_Flag" )
    val leaveActiveFlag : String,

    @SerializedName("Sl_NO")
    val slNo : String,

    @SerializedName("Emp_Id")
    val empId : String,

    @SerializedName("leaveunit")
    val leaveUnit : String

    ): Parcelable