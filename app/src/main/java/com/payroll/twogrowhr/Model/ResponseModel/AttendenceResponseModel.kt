package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AttendenceResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val Head: List<AttendenceData>
) : Parcelable
data class AttendenceRegularizedResponseModel(
    @SerializedName("success" ) val success: Boolean,
)

@Keep
@Parcelize
data class AttendenceData (

    @SerializedName("Emp_Id")
    val emp_Id: String,

    @SerializedName("Attendance_Date")
    val attendance_Date: String,

    @SerializedName("Checkin_Time")
    val checkin_Time: String,

    @SerializedName("Checkout_Time")
    val checkout_Time: String,

    @SerializedName("Notes")
    val notes : String,

    @SerializedName("Working_Hours")
    val working_hours: String,

    @SerializedName("Working_Minutes")
    val working_minutes: String,

    @SerializedName("Day_Name")
    val day_name: String,

    @SerializedName("checoutLoc")
    val checoutLoc: String,

    @SerializedName("checkinLoc")
    val checkinLoc: String,

    @SerializedName("Day")
    val day: Int,

    @SerializedName("Month")
    val month : String,

    @SerializedName("Year" )
    val year: Int,

    @SerializedName("Type" )
    val type: String,

    @SerializedName("diff_minutes")
    val diff_minutes: String,

    ): Parcelable