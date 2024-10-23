package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class RegularizeApprovalListResponseModel(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val Head: List<RegularizeData>

): Parcelable


@Keep
@Parcelize
data class RegularizeData (

    @SerializedName("Name")
    val Name : String,

    @SerializedName("Remark")
    val Remark : String,

    @SerializedName("Emp_Id")
    val Emp_Id : String,

    @SerializedName("Emp_Code")
    val Emp_Code : String,

    @SerializedName("Attendance_Date")
    val Attendance_Date : String,

    @SerializedName("Checkin_Time" )
    val Checkin_Time : String,

    @SerializedName("Checkout_Time")
    val Checkout_Time : String,

    @SerializedName("sl_no")
    val sl_no : Int

):Parcelable