package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class LeaveApprovalListResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Data")
    var data: List<UnApproveLeaveData>

): Parcelable


@Keep
@Parcelize
data class UnApproveLeaveData (

@SerializedName("Emp_Id")
val empId : String,

@SerializedName("Emp_code")
val empCode : String,

@SerializedName("emp_name")
val empName : String,

@SerializedName("leave_TypeName")
val leaveTypeName : String,

@SerializedName("Leave_preiod")
val leavePeriod : String,

@SerializedName("Req_Dt" )
val requestDate : String,

@SerializedName("Cnt")
val count : String,

@SerializedName("Sl_No" )
val slNo : String,

@SerializedName("leaveupload")
val leaveUpload : String,

@SerializedName("Year" )
val year : String,

@SerializedName("month")
val month : String,

@SerializedName("from_date" )
val fromDate : String,

@SerializedName("leave_Type")
val leaveType : String

): Parcelable