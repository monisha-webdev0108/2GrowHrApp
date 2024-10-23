package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OverTimeApprovalListResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Head")
    var head: List<OverTimeData>

): Parcelable


@Keep
@Parcelize
data class OverTimeData (

    @SerializedName("Emp")
    val empCode : String,

    @SerializedName("EmpId")
    val empId : String,

    @SerializedName("EmpName")
    val empName : String,

    @SerializedName("checkin_time")
    val checkin_time : String,

    @SerializedName("checkout_time")
    val checkout_time : String,

    @SerializedName("sft_sTime" )
    val shiftStartTime : String,

    @SerializedName("sft_ETime")
    val shiftEndTime : String,

    @SerializedName("Hr_per_day" )
    val hr_per_day : Int,

    @SerializedName("dd")
    val dd : Int,

    @SerializedName("shiftdiff")
    val shiftdiff : Int,

    @SerializedName("checkdiff")
    val checkdiff : Int,

    @SerializedName("Rate_per_hr")
    val rate_per_hr : Int,

    @SerializedName("OtHoli_rate_per_hr_National")
    val otHoli_rate_per_hr_National : Int,

    @SerializedName("OtMax_hr_holi_Fastival")
    val otMax_hr_holi_Fastival : Int,

    @SerializedName("Hrs")
    val hrs : Double,

    @SerializedName("WorkingHours")
    val workingHours : String,

    @SerializedName("IsMaximumHoursAllowedReq")
    val isMaximumHoursAllowedReq : Int,

    @SerializedName("MaxHoursLimit")
    val maxHoursLimit : Int,

    @SerializedName("Adjustduration")
    val adjustduration : Int,

    @SerializedName("IsMinimumHoursReq")
    val isMinimumHoursReq : Int,

    @SerializedName("MinLimithours")
    val minLimithours : Int,

    @SerializedName("OtWkend_per_hr_Weekly")
    val otWkend_per_hr_Weekly : Int,

    @SerializedName("IsBalanceFlowReq")
    val isBalanceFlowReq : Int,

    @SerializedName("BalanceLaps")
    val balanceLaps : Int,

    @SerializedName("Extra_Hrs")
    val extra_Hrs : Int,

    @SerializedName("IsApprovalFlowReq")
    val isApprovalFlowReq : Int,

    @SerializedName("Approval")
    val approval : Int,

    @SerializedName("Is_HoursExceeds")
    val is_HoursExceeds : Int,

    @SerializedName("HoursExceeds")
    val hoursExceeds : Int,

    @SerializedName("DurationOff")
    val durationOff : Int,

    @SerializedName("GoToApproval")
    val goToApproval : String,

    @SerializedName("Month")
    val month : Int,

    @SerializedName("Year")
    val year : Int,

    @SerializedName("WeekStart")
    val weekStart : String,

    @SerializedName("WeekEnd")
    val weekEnd : String,


    ): Parcelable