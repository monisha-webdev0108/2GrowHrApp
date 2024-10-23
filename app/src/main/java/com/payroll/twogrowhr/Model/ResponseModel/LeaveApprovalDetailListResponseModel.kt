package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class LeaveApprovalDetailListResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Data")
    var data: List<UnApproveDetailLeaveData>

): Parcelable


@Keep
@Parcelize
data class UnApproveDetailLeaveData (

    @SerializedName("Sl_No")
    val slNo : String,

    @SerializedName("Emp_Id")
    val empId : String,

    @SerializedName("emp_name")
    val empName : String,

    @SerializedName("Flag" )
    val flag : String,

    @SerializedName("Reson_Leave")
    val reasonForLeave : String,

    @SerializedName("From_Date" )
    val fromDate : String,

    @SerializedName("To_Date" )
    val toDate : String,

    @SerializedName("leave_TypeName")
    val leaveTypeName : String,

    @SerializedName("Leave_Type")
    val leaveType : String,

    @SerializedName("Day_Type")
    val dayType : String,

    @SerializedName("Cnt")
    val count : String,

    @SerializedName("leaveupload")
    val filePath : String,

): Parcelable