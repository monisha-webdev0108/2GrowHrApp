package com.payroll.twogrowhr.Model.ResponseModel


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OnDutyDetailResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Head"    )
    val head: List<OnDutyListData>

):Parcelable

@Keep
@Parcelize
data class OnDutyListData (

    @SerializedName("Rule_id")
    val ruleId : Int,

    @SerializedName("Allow_OnDuty")
    val allowOnDuty : Int,

    @SerializedName("HD_WorkReq")
    val hdWorkReq : Int,

    @SerializedName("Clock_In_Out")
    val clockInOut : Int,

    @SerializedName("NumberOfDays_OnDuty")
    val numberOfDaysOnDuty : Double, //-->Int

    @SerializedName("AllowDays_OnDuty" )
    val allowDaysOnDuty : Int,

    @SerializedName("DurationOf_OnDuty")
    val durationOfOnDuty : Int,

    @SerializedName("RestrictEmp_OnDuty")
    val restrictEmpOnDuty : Int,

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

    @SerializedName("Restrictpast_OnDutyDays")
    val restrictPastOnDutyDays : Double,

    @SerializedName("ODMonCnt")
    val odMonCnt : Int,

    @SerializedName("TakeODDate")
    val takeODDate : String,

    @SerializedName("PastOD_to_CurrentDateCnt")
    val pastODToCurrentDateCnt : Int,

    @SerializedName("OnDuty_Applied")
    val onDutyApplied : Int,

    @SerializedName("OnDuty_AppApprove")
    val onDutyAppApprove : Int,

    @SerializedName("EmpWeb_check_in")
    val empWebCheckIn : Int,

    @SerializedName("Doj")
    val doj : String,

    @SerializedName("ODAppliyDaysApproveCheckInOut")
    val odAppliedDaysApproveCheckInOut : String,

    @SerializedName("ODAppliyDaysCheckInOut")
    val odAppliedDaysCheckInOut : String,

    @SerializedName("ODDatecnt")
    val odDateCnt : Int,

    @SerializedName("WFHDatecnt")
    val wfhDatecnt : Int,

    @SerializedName("ODCheckoutorNot")
    val odCheckOutOrNot : Int,
):Parcelable