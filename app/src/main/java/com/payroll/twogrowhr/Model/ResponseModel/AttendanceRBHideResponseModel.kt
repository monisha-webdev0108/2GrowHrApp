package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AttendanceRBHideResponseModel(

    @SerializedName("success" ) val success: Boolean,
    @SerializedName("Head"    ) val Head: List<AttendenceRBHide>

): Parcelable

@Keep
@Parcelize
data class AttendenceRBHide (

    @SerializedName("FilePath")
    val filePath: String,

    @SerializedName("EmpCode")
    val empCode: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Remark")
    val remark: String,

    @SerializedName("Emp_Id")
    val emp_id: String,

    @SerializedName("Emp_Code")
    val emp_code: String,

    @SerializedName("Attendance_Date")
    val attendance_date : String,

    @SerializedName("Checkin_Time")
    val checkin_time: String,

    @SerializedName("Checkout_Time")
    val checkout_time: String,

    @SerializedName("Notes")
    val notes: String,

    @SerializedName("Working_Hours")
    val working_hours: String,

    @SerializedName("Day_Name")
    val day_name: String,

    @SerializedName("checoutLoc")
    val checoutLoc: String,

    @SerializedName("checkinLoc")
    val checkinLoc: String,

    @SerializedName("Day")
    val day: Int,

    @SerializedName("flag")
    val flag: Int,

    @SerializedName("Month")
    val month : String,

    @SerializedName("Year" )
    val year: Int,

    @SerializedName("sl_no" )
    val sl_no: Int,

    ): Parcelable