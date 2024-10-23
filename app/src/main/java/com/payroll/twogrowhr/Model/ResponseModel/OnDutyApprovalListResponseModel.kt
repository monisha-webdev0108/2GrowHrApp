package com.payroll.twogrowhr.Model.ResponseModel


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class OnDutyApprovalListResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Head")
    var head: List<OnDutyData>

):Parcelable


@Keep
@Parcelize
data class OnDutyData (

    @SerializedName("OnDuty_Date")
    val onDutyDate : String,

    @SerializedName("OndutyDt")
    val onDutyDt : String,

    @SerializedName("Emp_Code")
    val empCode : String,

    @SerializedName("Rule_id")
    val ruleId : String,

    @SerializedName("Rule_Name")
    val ruleName : String,

    @SerializedName("emp_name" )
    val empName : String,

    @SerializedName("Emp")
    val emp : String,

    @SerializedName("OnDuty_Reamarks" )
    val onDutyRemarks : String,

    @SerializedName("Day_Type")
    val dayType : String,

    ):Parcelable