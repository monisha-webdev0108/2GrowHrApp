package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TodayAttendanceLogResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("data")
    var data: List<TodayAttendanceLogData>

): Parcelable


@Keep
@Parcelize
data class TodayAttendanceLogData (

    @SerializedName("login_date")//"2024-04-30T00:00:00",
    val logInDate : String,

    @SerializedName("WrkDate")// "30/04/2024"
    val workDate : String,

    @SerializedName("SFT_Name")//"General Shift"
    val shiftName : String,

    @SerializedName("Shft")//"2024-04-30 01:00:00.00000"
    val shiftStartTime : String,

    @SerializedName("ShftE")//"2024-04-30 01:00:00.00000"
    val shiftEndTime : String,

    @SerializedName("AttTm" )//"12:39PM"
    val checkInTime : String,

    @SerializedName("ET")//""
    val checkOutTime : String,

    @SerializedName("STm" )//"2024-04-30 12:39:02"
    val attendanceStartTime : String,

    @SerializedName("dv")//""
    val dv : String,

    @SerializedName("Rw")//""
    val rw : String,

    @SerializedName("Status")//"In Time"
    val status : String,

    @SerializedName("AttDate")//"30/04/2024"
    val attendanceDate : String,

    @SerializedName("AttDtNm")//"Tuesday"
    val attendanceDay : String,

    @SerializedName("DayStatus")//"In Time"
    val todayStatus : String,

    @SerializedName("StaColor" )//"#ff0000"
    val statusColor : String,

    @SerializedName("GeoIn")//"13.0299913,80.2414351"
    val geoInLocation : String,

    @SerializedName("Extin" )//""
    val extendCheckIn : String,

    @SerializedName("ExtStartTtime")//""
    val extendCheckInStartTime : String,

    @SerializedName("ExtEndtime")//""
    val extendCheckInEndTime : String,

    @SerializedName("GeoOut" )//"0,0"
    val geoOutLocation : String,

    @SerializedName("Extout")//""
    val extendCheckOut : String,

    @SerializedName("Chkin_Loc" )//"No 4, Pasumpon Muthuramalinga Thevar Rd, Nandanam Extension, Nandanam, Chennai, Tamil Nadu 600035, India, Chennai"
    val checkInLocation : String,

    @SerializedName("ChkOut_Loc")//""
    val checkOutLocation : String,

    ): Parcelable
