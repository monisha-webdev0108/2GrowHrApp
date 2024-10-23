package com.payroll.twogrowhr.Model.ResponseModel


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class WorkFromHomeDetailResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Head"    )
    val head: List<WFHListData>

):Parcelable

@Keep
@Parcelize
data class WFHListData (

    @SerializedName("Rule_id")
    val ruleId : Int,

    @SerializedName("Allow_WFH")
    val allowWFH : Int,

    @SerializedName("HD_WorkReq")
    val hdWorkReq : Int,

    @SerializedName("Clock_In_Out")
    val clockInOut : Int,

    @SerializedName("NumberOfDays_WFH")
    val numberOfDaysWFH : Double, //-->Int

    @SerializedName("AllowDays_WFH" )
    val allowDaysWFH : Int,

    @SerializedName("DurationOf_WFH")
    val durationOfWFH : Int,

    @SerializedName("RestrictEmp_WFH")
    val restrictEmpWFH : Int,

    @SerializedName("Restrict_DayOff")
    val restrictDayOff : Int,

    @SerializedName("RestrictEmp_Holiday_Date")
    val restrictEmpHolidayDate : String,

    @SerializedName("RestrictEmp_weeklyOff")
    val restrictEmpWeeklyOff : String,

    @SerializedName("Commend_Req")
    val commentReq : Int,

    @SerializedName("Request_Exceeds")
    val requestExceeds : Int,

    @SerializedName("Request_Times")
    val requestTimes : Double,//-->Int

    @SerializedName("Request_Approval")
    val requestApproval : Int,

    @SerializedName("Auto_Aprove")
    val autoApprove : Int,

    @SerializedName("Aprove_Times")
    val approveTimes : Double,//-->Int

    @SerializedName("Aprove_Duration")
    val approveDuration : Int,

    @SerializedName("Restrict_pastDate")
    val restrictPastDate : Int,

    @SerializedName("Restrictpast_WFHDays")
    val restrictPastWFHDays : Double,

    @SerializedName("WMonCnt")
    val wfhMonCnt : Int,

    @SerializedName("TakeWFHDate")
    val takeWFHDate : String,

    @SerializedName("PastWFH_to_CurrentDateCnt")
    val pastWFHToCurrentDateCnt : Int,

    @SerializedName("WFH_Applied")
    val wfhApplied : Int,

    @SerializedName("WFH_AppApprove")
    val WFHAppApprove : Int,

    @SerializedName("EmpWeb_check_in")
    val empWebCheckIn : Int,

    @SerializedName("Doj")
    val doj : String,

    @SerializedName("WFHAppliyDaysApproveCheckInOut")
    val wfhAppliedDaysApproveCheckInOut : String,

    @SerializedName("WFHAppliyDaysCheckInOut")
    val wfhAppliedDaysCheckInOut : String,

    @SerializedName("WFHDatecnt")
    val wfhDateCnt : Int,

    @SerializedName("ODDatecnt")
    val odDatecnt : Int,
):Parcelable