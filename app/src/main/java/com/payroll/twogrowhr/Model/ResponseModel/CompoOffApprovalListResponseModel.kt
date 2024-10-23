package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CompoOffApprovalListResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Head")
    var head: List<UnApproveCompoOffData>

): Parcelable


@Keep
@Parcelize
data class UnApproveCompoOffData (


    @SerializedName("chckin")
    val chckin : String,

    @SerializedName("chkout")
    val chkout : String,

    @SerializedName("emp_id")
    val empId : String,

    @SerializedName("Emp_Code")
    val empCode : String,

    @SerializedName("date")
    val date : String,

    @SerializedName("dates")
    val dates : String,

    @SerializedName("weekday")
    val weekDay : String,

    @SerializedName("Divid")
    val divId : String,

    @SerializedName("emp_name")
    val empName : String,

    @SerializedName("Rate_per_hr")
    val ratePerHour : String,

    @SerializedName("CRHr_Per_Day")
    val CRHr_Per_Day : String,

    @SerializedName("CRRate_Per_Hr" )
    val ratePerHourCR : String,

    @SerializedName("CRWoff_Hr")
    val weekOffHourCR : String,

    @SerializedName("CRFestive_Hr" )
    val festiveHourCR : String,

    @SerializedName("CRNat_Holi_Hr")
    val natHolidayHourCR : String,

    @SerializedName("comval" )
    val compoOffValue : String,

    @SerializedName("Hrs")
    val hours : String,

    @SerializedName("hrsTime" )
    val hoursTime : String,

    @SerializedName("checkin")
    val checkIn : String,

    @SerializedName("checkout")
    val checkOut : String,

    @SerializedName("IsCompOffNeed" )
    val isCompOffNeed : String,

    @SerializedName("CompOffHours")
    val compOffHours : String,

    @SerializedName("AdjustHalfDay" )
    val adjustHalfDay : String,

    @SerializedName("AdjustFullDay")
    val adjustFullDay : String,

    @SerializedName("CompOffMiniHours" )
    val compOffMiniHours : String,

    @SerializedName("AdjustMiniHours")
    val adjustMiniHours : String,

    @SerializedName("NonEligible_Days")
    val nonEligibleDays : String,

    @SerializedName("AdjustNonEligible_Days" )
    val adjustNonEligibleDays : String,

    @SerializedName("SalaryCalculation")
    val salaryCalculation : String,

    @SerializedName("SalaryCount" )
    val salaryCount : String,

    @SerializedName("AdjustSalary")
    val adjustSalary : String,

    @SerializedName("CompOffLimit")
    val compOffLimit : String,

    @SerializedName("CompOffLimitValue" )
    val compOffLimitValue : String,

    @SerializedName("AdjustCompOffLimit")
    val adjustCompOffLimit : String,

    @SerializedName("Compenstate" )
    val compenstate : String,

    @SerializedName("st")
    val st : String,

    @SerializedName("Holiday_Type")
    val holidayType : String,

    @SerializedName("Weekly_Off")
    val weeklyOff : String,

    @SerializedName("OtHoli_rate_per_hr" )
    val rphHolidayForOT : String,

    @SerializedName("OtWkend_per_hr")
    val rphWeekendForOT : String,

    @SerializedName("OtMax_hr_holi")
    val maxHolidayHourForOT : String,


    ): Parcelable